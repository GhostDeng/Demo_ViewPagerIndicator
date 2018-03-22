package com.ghost.demo.viewpagerindicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * 作者：Ghost
 * 时间：2018/3/18
 * 功能：绘制ViewPager的指示器
 */

public class ViewPagerIndicator extends LinearLayout {

    //画笔
    private Paint mPaint;
    //三角形的路径
    private Path mPath;
    //三角形的宽度
    private int mTriangleWidth;
    //三角形的高
    private int mTriangleHeight;
    //设置三角形所占子控件的比例
    private static final float RADIO_TRIANGLE_WIDTH = 1 / 6f;
    //初始化三角形的位置
    private int mInitTranslationX;
    //改变后三角形的位置
    private int mTranslationX;

    private int mTabVisibleCount;

    private static final int COUNT_DEFAULT_TAB = 5;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //获取可见Tab的数量
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.ViewPagerIndicator);

        mTabVisibleCount = array.getInt(R.styleable.ViewPagerIndicator_visible_tab_count,
                COUNT_DEFAULT_TAB);

        if (mTabVisibleCount < 0) {
            mTabVisibleCount = COUNT_DEFAULT_TAB;
        }

        array.recycle();

        //初始化画笔
        mPaint = new Paint();
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        //设置画笔的颜色
        mPaint.setColor(Color.parseColor("#ffffff"));
        //设置画笔的风格
        mPaint.setStyle(Paint.Style.FILL);
        //不让三角形的角度过于尖锐，设置一点幅度
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    //当控件的大小发生改变时都会调用的方法
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //三角形底边的宽度，因为是3个条目，所以三角形底边的宽度为条目的宽度乘以三角形所占子控件的比例
        mTriangleWidth = (int) (w / mTabVisibleCount * RADIO_TRIANGLE_WIDTH);
        //初始化偏移量   子控件的宽度的1/2 - 三角形底边宽度的1/2 = 初始的偏移量
        mInitTranslationX = w / mTabVisibleCount / 2 - mTriangleWidth / 2;
        //初始化三角形
        initTriangle();

    }

    private void initTriangle() {
        //因为在正三角形，所有高度为底边的宽度的1/2
        mTriangleHeight = mTriangleWidth / 2;
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth, 0);
        mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
        mPath.close();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        //对画布进行保存
        canvas.save();

        canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 2);
        canvas.drawPath(mPath, mPaint);

        canvas.restore();
        super.dispatchDraw(canvas);
    }

    /**
     * 指示器跟随手指滑动
     *
     * @param position 当前所选Tab的位置
     * @param Offset   偏移量
     */
    public void scroll(int position, float Offset) {

        int tabWidth = getWidth() / mTabVisibleCount;
        mTranslationX = (int) (tabWidth * (Offset + position));

        //容器移动，在tab出移动至最后一个时
        if (position >= (mTabVisibleCount - 3) && Offset > 0 &&
                getChildCount() > mTabVisibleCount) {

            if (mTabVisibleCount!=1){
                this.scrollTo(
                        (position-(mTabVisibleCount-3))*tabWidth+(int) (tabWidth*Offset),0);
            }else {
                this.scrollTo(position*tabWidth+(int) (tabWidth*Offset),0);
            }


        }



        //说明移动到了第4个~第7个
//        if (position > 2 && position <= (getChildCount() - 2)) {
//            this.scrollBy(tabWidth, 0);
//        }

        invalidate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int count = getChildCount();
        if (count == 0) return;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LinearLayout.LayoutParams lp = (LayoutParams) child.getLayoutParams();
            lp.weight = 0;
            lp.width = getScreenWidth() / mTabVisibleCount;
            child.setLayoutParams(lp);
        }
    }

    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        return outMetrics.widthPixels;
    }
}
