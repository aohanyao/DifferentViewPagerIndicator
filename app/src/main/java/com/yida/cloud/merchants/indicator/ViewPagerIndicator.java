package com.yida.cloud.merchants.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.yida.cloud.merchants.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jc on 2018-06-19.
 * Version:1.0
 * Description:
 * ChangeLog:
 * <p>
 * 1. 测量所有标题的最大值的宽度 加上间隙，减去屏幕的宽度，计算出第一个标题的
 * x坐标
 */

public class ViewPagerIndicator extends View {

    private String TAG = "ViewPagerIndicator";


    private Paint mPaint;

    /**
     * 标题数量
     */
    private List<String> mTitles;
    /**
     * 两个标题之间的
     */
    private int mTitleSpaces = 20;

    /**
     * 标题最大字体
     */
    private float mMaxTextSize = 32L;
    /**
     * 标题最小字体
     */
    private float mMixTextSize = 14L;
    /**
     * 宽度
     */
    private int mWidth = 0;
    /**
     * 高度
     */
    private int mHeight = 0;
    private int mStartX = 0;
    private int mCurrentPosition = 0;

    private ViewPager mViewPager;
    /**
     * 最后一次偏移
     */
    float mLastPositionOffset = 0L;
//    float mPositionOffset = 0L;

    /**
     * 最大字体与最小字体的差值
     */
    private float mTitleTextSizeCult;

    /**
     * 所有的字体大小
     */
    private List<Float> mTitleTextSize = new ArrayList<>();
    /**
     * 原始所有最大字体的X坐标
     */
    private List<Integer> mOriginalMaxTextXs = new ArrayList<>();
    /**
     * 最大标题和最小标题之间的差值
     */
    private List<Integer> mOriginalMixTextXs = new ArrayList<>();
    /**
     * 现在用到的字体X坐标
     */
    private List<Integer> mNowTextXs = new ArrayList<>();


    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public List<String> getmTitles() {
        return mTitles;
    }

    public void setTitles(List<String> mTitles) {
        this.mTitles = mTitles;
//        calculationProperty();
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public void setViewPager(ViewPager viewPager, int pos) {
        this.mViewPager = viewPager;
//        mCurrentPosition = pos;
        setViewPagerListener();
    }

    /**
     * ③ 监听viewPager
     */
    private void setViewPagerListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
//                mCurrentPosition = position;
//                //计算最大文字大小和最小文字大小的比例值
//                float mTitleTextSizeProportion = mTitleTextSizeCult * positionOffset;
//                // ⑤ 恢复默认初始值字体大小
//                int titleTextSize = mTitleTextSize.size();
//                mTitleTextSize.clear();
//                for (int i = 0; i < titleTextSize; i++) {
//                    //默认字体大小
//                    mTitleTextSize.add(mMixTextSize);
//                }
//                //恢复默认的标题间距
//                mNowTextXs.clear();
//                mNowTextXs.addAll(mOriginalMaxTextXs);
//
//
//                // 判断ViewPager的滑动方向
//                if (mLastPositionOffset >= positionOffset && positionOffset != 0) {
//                    //右滑  这样滑动的  <<<<<-----
//                    if (position >= 0 && positionOffset <= 1.0f) {
//                        //---------------------------计算字体大小 start-------
//                        // 当前这个不断的缩小
//                        mTitleTextSize.add(position, mMaxTextSize - mTitleTextSizeProportion);
//                        // 当前这个不断的缩小
//                        mTitleTextSize.add(position + 1, mMixTextSize + mTitleTextSizeProportion);
//                        //---------------------------计算字体大小end-------
//                    }
//
//                } else if (mLastPositionOffset <= positionOffset && positionOffset != 0) {
//                    //左滑  这样滑动的 ---->>>>
//                    //没有大于边界
//                    if (position < mViewPager.getChildCount() && positionOffset <= 1.0f) {
//                        //---------------------------计算字体大小 start-------
//                        // 当前这个不断的缩小
//                        mTitleTextSize.add(position, mMaxTextSize - mTitleTextSizeProportion);
//                        //当前这个不断的缩小
//                        mTitleTextSize.add(position + 1, mMixTextSize + mTitleTextSizeProportion);
//                        //---------------------------计算字体大小end-------
//
//                        //---------------------------计算间距start-------
//                        // 获取当前标题的X坐标
//                        // 下一个的X坐标
//                        Integer mNextTitleX = mNowTextXs.get(position + 1);
//                        // 计算两个坐标之间的差值
//                        float v = (mNextTitleX - mOriginalMixTextXs.get(position + 1)) * positionOffset;
////                        mNowTextXs.add(position + 1, (int) v);
//                         mNowTextXs.add(position + 1, (int) (mNextTitleX - (v * positionOffset)));
//                        //---------------------------计算间距end---------
//
//
//                    }
//                } else {
//                    //回滚回去了，默认大标题
//                    mTitleTextSize.add(position, mMaxTextSize);
//                }
//
//                mLastPositionOffset = positionOffset;
//
//                //重新绘制
//                postInvalidate();

            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });
        // 设置当前页
        mViewPager.setCurrentItem(mCurrentPosition);
    }

    /**
     * ① 计算开始的x坐标，计算字体的大小
     * ------------------------------
     * -
     * -  *****
     * -  *   *   ****   ****
     * -  *****   ****   ****
     * -
     * ------------------------------
     */
    private void calculationProperty() {
        //清除数据
        mTitleTextSize.clear();
        //最大文字的总长度
        int sumTtilewidth = 0;
        //最大文字
        mPaint.setTextSize(mMaxTextSize);
        int titleSize = mTitles.size();
        for (int i = 0; i < titleSize; i++) {
            sumTtilewidth += measureTextWidth(mTitles.get(i), mPaint);
            //是否加上间隙 最后一个不加上间隙
//            if (i < titleSize) {
//                sumTtilewidth += mTitleSpaces;
//            }
            //将字体大小存放到集合中
            if (i == mCurrentPosition) {
                mTitleTextSize.add(mMaxTextSize);
            } else {
                mTitleTextSize.add(mMixTextSize);
            }
        }
        Log.e(TAG, "calculationProperty: sumTtilewidth:" + sumTtilewidth + "    mWidth:" + mWidth);
        //计算初始X坐标
        //view宽度减去标题总宽度
        mStartX = (mWidth - sumTtilewidth) / 2;
        //刷新绘制
        postInvalidate();
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mMaxTextSize = DensityUtils.dp2px(getContext(), mMaxTextSize);
        mMixTextSize = DensityUtils.dp2px(getContext(), mMixTextSize);

        // 标题字体之间的差值
        mTitleTextSizeCult = (mMaxTextSize - mMixTextSize);
    }

    private int mSpaces = 30;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText2(canvas);
    }


    /**
     * ④ 加入ViewPager 监听偏移量
     *
     * @param canvas
     */
    private void drawText2(Canvas canvas) {

        int titleSize = mTitles.size();

        //
        for (int i = 0; i < titleSize; i++) {
            //取出文字大小
            Float textSize = mTitleTextSize.get(i);
            mPaint.setTextSize(textSize);
            //绘制文字
            String title = mTitles.get(i);
            //绘制文字
            canvas.drawText(title, mNowTextXs.get(i), mHeight - 10, mPaint);
        }
    }


    //-------------------相关工具类 start-------------------


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        calculationProperty();
        calculationTextX();
        postInvalidate();
    }

    /**
     * ③ 计算每个标题的X轴坐标
     */
    private void calculationTextX() {
        int titleSize = mTitles.size();
        //最大字体X轴
        int maxTextX = mStartX;

        mOriginalMaxTextXs.clear();
        mOriginalMixTextXs.clear();
        for (int i = 0; i < titleSize; i++) {
            //添加到最大字体
            mOriginalMaxTextXs.add(maxTextX);
            //todo 应该增加一个差值
            // 最大值 与最小值的差值
//            if (mCurrentPosition == i) {
//                mPaint.setTextSize(mMaxTextSize);
//            } else {
            mPaint.setTextSize(mMixTextSize);
//            }
            //取出文字
            String title = mTitles.get(i);

            //初始X坐标 加上 上个文字的宽度 加上 间距
            int maxTitleWidth = measureTextWidth(title, mPaint);
            //最小X坐标值
            mPaint.setTextSize(mMixTextSize);
            int mixTitleWidth = measureTextWidth(title, mPaint);

            //最大标题和最小标题之间的差值
            int titleWidthDifference = maxTitleWidth - mixTitleWidth;
            mOriginalMixTextXs.add(titleWidthDifference);


            maxTextX += maxTitleWidth + mTitleSpaces;
            if (i > mCurrentPosition) {
                maxTextX += titleWidthDifference;
                Log.e(TAG, "增加: "+titleWidthDifference );
//                mOriginalMaxTextXs.add(0, maxTextX);
            }

        }

        mNowTextXs.clear();
        mNowTextXs.addAll(mOriginalMaxTextXs);
    }


    /**
     * 测量文字的宽度
     *
     * @param str
     * @param paint
     * @return
     */
    private int measureTextWidth(String str, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        int w = rect.width();

        return w;
        //-------------------相关工具类 end-------------------

    }
}
