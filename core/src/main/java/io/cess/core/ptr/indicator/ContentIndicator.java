package io.cess.core.ptr.indicator;

import android.graphics.PointF;
import android.view.MotionEvent;

/**
 * Created by lin on 08/01/2017.
 */

public class ContentIndicator {
    private PointF mPtLastMove = new PointF();//上一次 Motion 的位置，默认为(0,0)
    private float mOffsetX;//基于上一次Motion的 x 方向偏移量
    private float mOffsetY;//基于上一次Motion的 y 方向偏移量
    private boolean mIsUnderTouch = false;//表示当前是否处理 touch 状态，ACTION_DOWN 开始，ACTION_UP 或 ACTION_CANCEL 结束
//    private float mResistance = 1.0f;//下拉或上拉是的阻尼系数
    private MotionEvent mLastMoveEvent;

    public void onPressDown(float x, float y) {//设置当 ACTION_DOWN 事件时的 Motion 的位置
        mIsUnderTouch = true;
        mPtLastMove.set(x, y);
    }

    protected void processOnMove(float currentX, float currentY, float offsetX, float offsetY) {
        setOffset(offsetX, offsetY);
    }

    public void onMove(MotionEvent event){
        mLastMoveEvent = event;
        onMove(event.getX(),event.getY());
    }
    private void onMove(float x, float y) {
        float offsetX = x - mPtLastMove.x;
        float offsetY = (y - mPtLastMove.y);
        processOnMove(x, y, offsetX, offsetY);
        mPtLastMove.set(x, y);
    }

    protected void setOffset(float x, float y) {
        mOffsetX = x;
        mOffsetY = y;
    }

    public float getOffsetX() {
        return mOffsetX;
    }

    public float getOffsetY() {
        return mOffsetY;
    }
    public boolean isUnderTouch() {
        return mIsUnderTouch;
    }

//    public float getResistance() {
//        return mResistance;
//    }
//
//    public void setResistance(float resistance) {
//        mResistance = resistance;
//    }

    public void onRelease() {
        mIsUnderTouch = false;
    }

    public MotionEvent getLastMoveEvent() {
        return mLastMoveEvent;
    }
}
