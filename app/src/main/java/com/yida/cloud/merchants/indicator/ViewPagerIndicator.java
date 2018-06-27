package com.yida.cloud.merchants.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.yida.cloud.merchants.indicator.bean.IndicatorBean;
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
    private int mTitleSpaces = 10;

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
    /**
     * 指示器的总长度
     */
    private int mSumIndicatorWidth = 0;
    /**
     * 当前下标
     */
    private int mCurrentPosition = 0;
    /**
     * path构成一个三角形
     */
    private Path mTrianglePath;
    private ViewPager mViewPager;
    /**
     * 最后一次偏移
     */
    float mLastPositionOffset = 0L;

    /**
     * 最大字体与最小字体的差值
     */
    private float mTitleTextSizeCult;

    /**
     * 所有的指示器
     */
    private List<IndicatorBean> mIndicatorBeans = new ArrayList<>();

    // 1. 获取点击的X坐标
    private float mDownX = 0;
    // 2. 获取点击的Y坐标
    private float mDownY = 0;
    private float mTriangleWidth = 0;

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

    public List<String> getTitles() {
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
        setViewPagerListener(pos);
    }

    /**
     * ③ 监听viewPager
     *
     * @param pos
     */
    private void setViewPagerListener(int pos) {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                mCurrentPosition = position;
                //计算最大文字大小和最小文字大小的比例值
                float mTitleTextSizeProportion = mTitleTextSizeCult * positionOffset;
                //当前
                IndicatorBean nowIndicatorBean = mIndicatorBeans.get(position);
                // 判断ViewPager的滑动方向
                if (mLastPositionOffset >= positionOffset && positionOffset != 0) {
                    //右滑  这样滑动的  <<<<<-----
                    if (position >= 0 && positionOffset <= 1.0f) {
                        IndicatorBean nextIndicatorBean = mIndicatorBeans.get(position + 1);
                        //---------------------------计算字体大小 start-------
                        // 当前这个不断的缩小
                        nowIndicatorBean.setTextSize(mMaxTextSize - mTitleTextSizeProportion);
                        // 当前这个不断的缩小
                        nextIndicatorBean.setTextSize(mMixTextSize + mTitleTextSizeProportion);
                        //---------------------------计算字体大小end-------
                        //---------------------------计算间距start-------
                        nextIndicatorBean.calculationDrawTextX(positionOffset);
                        //---------------------------计算间距end-------
                    }

                } else if (mLastPositionOffset <= positionOffset && positionOffset != 0) {
                    //左滑  这样滑动的 ---->>>>
                    //没有大于边界
                    if (position < mViewPager.getChildCount() && positionOffset <= 1.0f) {
                        //下一个
                        IndicatorBean nextIndicatorBean = mIndicatorBeans.get(position + 1);
                        //---------------------------计算字体大小 start-------
                        // 当前这个不断的缩小
                        nowIndicatorBean.setTextSize(mMaxTextSize - mTitleTextSizeProportion);
                        //当前这个不断的缩小
                        nextIndicatorBean.setTextSize(mMixTextSize + mTitleTextSizeProportion);
                        //---------------------------计算字体大小end-------

                        //---------------------------计算间距start-------
                        //计算坐标值
                        nextIndicatorBean.calculationDrawTextX(positionOffset);
                        //---------------------------计算间距end---------
                    }
                } else {
                    //回滚回去了，默认大标题
                    nowIndicatorBean.setTextSize(mMaxTextSize);
                }
                //保存这一次偏移
                mLastPositionOffset = positionOffset;
                //重新绘制
                postInvalidate();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mCurrentPosition = pos;
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
        mIndicatorBeans.clear();
        //最大文字的总长度
        int sumTitleWidth = 0;
        int titleSize = mTitles.size();
        for (int i = 0; i < titleSize; i++) {
            //创建一个实体对象
            IndicatorBean indicatorBean = new IndicatorBean();
            indicatorBean.setText(mTitles.get(i));
            //将字体大小存放到相应的对象中
            if (i == mCurrentPosition) {
                mPaint.setTextSize(mMaxTextSize);
                indicatorBean.setTextSize(mMaxTextSize);
            } else {
                mPaint.setTextSize(mMixTextSize);
                indicatorBean.setTextSize(mMixTextSize);
            }
            //添加到集合中
            mIndicatorBeans.add(indicatorBean);
            //计算字体大小
            sumTitleWidth += measureTextWidth(mTitles.get(i), mPaint);
        }
        //计算初始X坐标
        //view宽度减去标题总宽度
        mStartX = (mWidth - sumTitleWidth) / 2;
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
        mTitleSpaces = DensityUtils.dp2px(getContext(), mTitleSpaces);

        // 标题字体之间的差值
        mTitleTextSizeCult = (mMaxTextSize - mMixTextSize);
        setFocusable(true);
        setClickable(true);

        initTriangle();
    }

    /**
     * 初始化三角形指示器
     */
    private void initTriangle() {
        mTrianglePath = new Path();

        int mTriangleHeight = DensityUtils.dp2px(getContext(), 6);
        mTriangleWidth = DensityUtils.dp2px(getContext(), 12);
        //     *
        //          *
        //
        mTrianglePath.moveTo(0, 0);
        mTrianglePath.lineTo(mTriangleWidth, 0);
        mTrianglePath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
        mTrianglePath.close();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIndicator(canvas);
        if (mCurrentPosition == 0 && mLastPositionOffset == 0)
            drawTriangle(canvas);
    }

    //绘制三角形  特例的，当前需要的
    private void drawTriangle(Canvas canvas) {
        //先初始化
        int triangleX = mStartX + mIndicatorBeans.get(0).getTextMaxWidth() / 2 - mTitleSpaces / 2;
//        //加上对应下标的宽度
//        for (int i = 0; i <= mCurrentPosition; i++) {
//            IndicatorBean indicatorBean = mIndicatorBeans.get(i);
//            if (i == mCurrentPosition) {
//                triangleX += indicatorBean.getTextMaxWidth() / 2;
//
//            } else {
//                triangleX += indicatorBean.getTextMaxWidth();
//                if (i < mIndicatorBeans.size() - 1) {
//                    //加上间距
//                    triangleX += mTitleSpaces;
//                }
//            }
//        }
        canvas.save();
//        Log.e(TAG, "drawTriangle: " + triangleX);
        canvas.translate(triangleX, mHeight - 5/*减少了一个小间距*/);
        mPaint.setColor(Color.WHITE);
        canvas.drawPath(mTrianglePath, mPaint);
        canvas.restore();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //计算点击
//                if (calculationClick(mDownX, mDownY, event)) {
//                    return true;
//                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }


        return super.onTouchEvent(event);
    }


    /**
     * 8 计算坐标点击
     *
     * @param downX
     * @param downY
     * @param event
     * @return
     */
    private boolean calculationClick(float downX, float downY, MotionEvent event) {
        // 抬起了手指，获取到x和y轴的点，判断是不是矩阵当中
        // 在矩阵当中，则表示当前点击的是这个位置
        //TODO 未完成 点击事件
        // 1. 获取抬起的X坐标
        int upX = (int) event.getRawX();
        // 2. 获取抬起的Y坐标
        int upY = (int) event.getRawY();

        //xy移动未超过10个像素的矩阵
//        if (Math.abs(downX - upX) <= 10 && Math.abs(downY - upY) <= 10) {
        // 遍历所有的指示器
        int size = mIndicatorBeans.size();
        for (int i = 0; i < size; i++) {
            //判断x坐标是否在最大宽度和最小宽度之间，就是点击中了
            IndicatorBean indicatorBean = mIndicatorBeans.get(i);

            //创建矩形
            float right = indicatorBean.getDrawTextXs() + indicatorBean.getTextMixWidth();
            Rect mIndicatorRect = new Rect(indicatorBean.getDrawTextXs(),
                    0, (int) right,
                    mHeight);

            //判断点击的点是不是在矩形内
            if (mIndicatorRect.contains(upX, upY)
                    && i != mViewPager.getCurrentItem()) {
                Log.e(TAG, "onTouchEvent: 在点击范围内" + i);

                mViewPager.setCurrentItem(i, true);
                return true;
            }

            Log.e(TAG, "calculationClick: \nupX:" + upX
                    + "\nupY:" + upY + "\nmaxTextX:" + indicatorBean.getMaxTextX());
        }
//        }

        return false;
    }

    /**
     * ④ 加入ViewPager 监听偏移量
     * <p>
     * 绘制指示器文字
     *
     * @param canvas 画布
     */
    private void drawIndicator(Canvas canvas) {

        int titleSize = mTitles.size();

        //遍历所有的文字，绘制
        for (int i = 0; i < titleSize; i++) {
            IndicatorBean indicatorBean = mIndicatorBeans.get(i);
            canvas.save();
            mPaint.setColor(Color.WHITE);
            //取出文字大小
            mPaint.setTextSize(indicatorBean.getTextSize());
            //绘制文字
            String title = indicatorBean.getText();
            //绘制文字
            Integer textX = indicatorBean.getDrawTextXs();
            //绘制文字 应该放出一个属性叫做底部距离
            canvas.drawText(title, textX, mHeight - DensityUtils.dp2px(getContext(), 14), mPaint);
            canvas.restore();

            //绘制消息红点
            canvas.save();
            mPaint.setColor(Color.RED);
            canvas.drawCircle(indicatorBean.getBadgeAxis(), 50, 5, mPaint);
            canvas.restore();
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

        //遍历所有的文字，为标题设置相关的属性
        for (int i = 0; i < titleSize; i++) {
            //去除实体类
            IndicatorBean indicatorBean = mIndicatorBeans.get(i);

            //设置相应的坐标
            indicatorBean.setMaxTextX(maxTextX);
            //设置 绘制时候使用的坐标
            indicatorBean.setDrawTextXs(maxTextX);
            //设置为最大字体
            mPaint.setTextSize(mMaxTextSize);
            //取出文字
            String title = mTitles.get(i);

            //初始X坐标 加上 上个文字的宽度 加上 间距
            int maxTitleWidth = measureTextWidth(title, mPaint);
            indicatorBean.setTextMaxWidth(maxTitleWidth);

            //最小X坐标值
            mPaint.setTextSize(mMixTextSize);
            int mixTitleWidth = measureTextWidth(title, mPaint);
            indicatorBean.setTextMixWidth(mixTitleWidth);

            //最大标题和最小标题之间的差值
            int titleWidthDifference = maxTitleWidth - mixTitleWidth;
            //将差值存放起来
            indicatorBean.setTitleTextDifferenceX(titleWidthDifference);

            // 根据选中 计算不同的X轴坐标
            if (i == mCurrentPosition) {
                maxTextX += maxTitleWidth + mTitleSpaces;
                mSumIndicatorWidth += maxTitleWidth + mTitleSpaces;
            } else {
                maxTextX += mixTitleWidth + mTitleSpaces;
                mSumIndicatorWidth += mixTitleWidth + mTitleSpaces;
            }

            //消息红点的X轴
            indicatorBean.setBadgeAxis(maxTextX - mTitleSpaces / 3);
        }
    }


    /**
     * 测量文字的宽度
     *
     * @param str   需要测量的文字
     * @param paint 画笔
     * @return 文字的宽度
     */
    private int measureTextWidth(String str, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.width();
        //-------------------相关工具类 end-------------------

    }
}
