package com.yida.cloud.merchants.indicator.bean;

/**
 * Created by jc on 2018-06-23.
 * Version:1.0
 * Description:存放标题数据
 * ChangeLog:
 */

public class IndicatorBean {
    /**
     * 标题文字
     */
    private String text;

    /**
     * 标题指示器
     */
    private float textSize = 14L;
    /**
     * 最大指示器的X坐标
     */
    private int maxTextX;
    /**
     * 指示器的最大宽度
     */
    private int textMaxWidth;
    /**
     * 指示器的最小宽度
     */
    private int textMixWidth;
    /**
     * 最小指示器的X坐标
     */
    private float mixTextX;

    /**
     * 最大指示器的X坐标和最小指示器X坐标的差值
     */
    private int titleTextDifferenceX;
    /**
     * 当前能使用的x坐标
     */
    private int drawTextXs;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getMaxTextX() {
        return maxTextX;
    }

    public void setMaxTextX(int maxTextX) {
        this.maxTextX = maxTextX;
    }

    public float getMixTextX() {
        return mixTextX;
    }

    public void setMixTextX(float mixTextX) {
        this.mixTextX = mixTextX;
    }


    public void setTitleTextDifferenceX(int titleTextDifferenceX) {
        this.titleTextDifferenceX = titleTextDifferenceX;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getDrawTextXs() {
        return drawTextXs;
    }

    public void setDrawTextXs(int nowTextXs) {
        this.drawTextXs = nowTextXs;
    }

    public int getTextMaxWidth() {
        return textMaxWidth;
    }

    public void setTextMaxWidth(int textMaxWidth) {
        this.textMaxWidth = textMaxWidth;
    }

    public int getTextMixWidth() {
        return textMixWidth;
    }

    public void setTextMixWidth(int textMixWidth) {
        this.textMixWidth = textMixWidth;
    }

    public int getTitleTextDifferenceX() {
        return titleTextDifferenceX;
    }

    /**
     * 计算 绘制的时候文字的X坐标
     *
     * @param positionOffset viewPager的偏移量
     */
    public void calculationDrawTextX(float positionOffset) {
        setDrawTextXs((int) (maxTextX - (titleTextDifferenceX * positionOffset)));
    }

    public boolean calculationClick(float x, float y) {


        //1. 最大
        if (x >= maxTextX && x <= (maxTextX + textMaxWidth) /*|| x <= (maxTextX + textMixWidth)*/) {

            return true;
        }
        return false;
    }
}