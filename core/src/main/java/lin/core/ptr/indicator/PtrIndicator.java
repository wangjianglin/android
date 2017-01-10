package lin.core.ptr.indicator;

import android.content.Intent;
import android.graphics.PointF;

/**
 * Created by lin on 08/01/2017.
 */

public class PtrIndicator {
    public final static int POS_START = 0;

    protected int mOffsetToRefresh = 10000;

    //当前位置，用于表示 mContent 与 top 之间的距离
    //当 mCurrentPos > 0 表示处于下拉状态，当 mCurrentPos < 0 表示处于上拉状态
    //当mCurentPos == 0 表示处于其他状态
    //
    //           --------
    //          |        |
    //      ----|--------|-----   top
    //          |        |
    //          |        |
    //          |        |
    //          |        |
    //          |        |
    //      ----|--------|-----   bottom
    //          |        |
    //           --------
    private int mCurrentPos = 0;
    private int mLastPos = 0;//上一次的 mCurrentPos
    private int mHeaderHeight;//header view 的高度
    private int mPressedPos = 0; //ACTION_DOWN 时的 mCurrentPos

    private float mRatioOfHeaderHeightToRefresh = 1.5f;//下拉或上拉的高度达到 view height 的多少倍后 进入 放开 刷新 或 加载 更多状态

    private int mOffsetToKeepHeaderWhileLoading = -1;
    // record the refresh complete position
    private int mRefreshCompleteY = 0;



    public void onPressDown(){
        mPressedPos = mCurrentPos;
    }

    public void onUIRefreshComplete() {
        mRefreshCompleteY = mCurrentPos;
    }

    public boolean goDownCrossFinishPosition() {
        return mCurrentPos >= mRefreshCompleteY;
    }

    public void setRatioOfHeaderHeightToRefresh(float ratio) {
        mRatioOfHeaderHeightToRefresh = ratio;
        this.updateHeight();
    }

    public float getRatioOfHeaderToHeightRefresh() {
        return mRatioOfHeaderHeightToRefresh;
    }

    public int getOffsetToRefresh() {
        return mOffsetToRefresh;
    }

    public void setOffsetToRefresh(int offset) {
        mRatioOfHeaderHeightToRefresh = mHeaderHeight * 1f / offset;
        mOffsetToRefresh = offset;
    }



    public int getLastPosY() {
        return mLastPos;
    }

    public int getCurrentPosY() {
        return mCurrentPos;
    }

    /**
     * Update current position before update the UI
     */
    public final void setCurrentPos(int current) {
        mLastPos = mCurrentPos;
        mCurrentPos = current;
        onUpdatePos(current, mLastPos);
    }

    protected void onUpdatePos(int current, int last) {

    }

    public int getHeaderHeight() {
        return mHeaderHeight;
    }

    public void setHeaderHeight(int height) {
        mHeaderHeight = height;
        updateHeight();
    }

    protected void updateHeight() {
        if(mHeaderHeight == 0){
            mOffsetToRefresh = Integer.MAX_VALUE;
        }else {
            if(mRatioOfHeaderHeightToRefresh < 1){
                mOffsetToRefresh = mHeaderHeight;
            }else {
                mOffsetToRefresh = (int) (mRatioOfHeaderHeightToRefresh * mHeaderHeight);
            }
        }
    }

    public void convertFrom(PtrIndicator ptrSlider) {
        mCurrentPos = ptrSlider.mCurrentPos;
        mLastPos = ptrSlider.mLastPos;
        mHeaderHeight = ptrSlider.mHeaderHeight;
    }

    public boolean hasLeftStartPosition() {
        return mCurrentPos > POS_START;
    }

    public boolean hasJustLeftStartPosition() {
        return mLastPos == POS_START && hasLeftStartPosition();
    }

    public boolean hasJustBackToStartPosition() {
        return mLastPos != POS_START && isInStartPosition();
    }

    public boolean isOverOffsetToRefresh() {
        return mCurrentPos >= getOffsetToRefresh();
    }

    public boolean hasMovedAfterPressedDown() {
        return mCurrentPos != mPressedPos;
    }

    public boolean isInStartPosition() {
        return mCurrentPos == POS_START;
    }

    public boolean crossRefreshLineFromTopToBottom() {
        return mLastPos < getOffsetToRefresh() && mCurrentPos >= getOffsetToRefresh();
    }

    public boolean hasJustReachedHeaderHeightFromTopToBottom() {
        return mLastPos < mHeaderHeight && mCurrentPos >= mHeaderHeight;
    }

    public boolean isOverOffsetToKeepHeaderWhileLoading() {
        return mCurrentPos > getOffsetToKeepHeaderWhileLoading();
    }

    public void setOffsetToKeepHeaderWhileLoading(int offset) {
        mOffsetToKeepHeaderWhileLoading = offset;
    }

    public int getOffsetToKeepHeaderWhileLoading() {
        return mOffsetToKeepHeaderWhileLoading >= 0 ? mOffsetToKeepHeaderWhileLoading : mHeaderHeight;
    }

    public boolean isAlreadyHere(int to) {
        return mCurrentPos == to;
    }

    public float getLastPercent() {
        final float oldPercent = mHeaderHeight == 0 ? 0 : mLastPos * 1f / mHeaderHeight;
        return oldPercent;
    }

    public float getCurrentPercent() {
        final float currentPercent = mHeaderHeight == 0 ? 0 : mCurrentPos * 1f / mHeaderHeight;
        return currentPercent;
    }

    public boolean willOverTop(int to) {
        return to < POS_START;
    }
}