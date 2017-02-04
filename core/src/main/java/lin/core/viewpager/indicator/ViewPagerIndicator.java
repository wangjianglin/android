package lin.core.viewpager.indicator;

/**
 * Created by lin on 08/01/2017.
 */

public class ViewPagerIndicator {
    public final static int POS_START = 0;

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
    private int mPressedPos = 0; //ACTION_DOWN 时的 mCurrentPos



    public void onPressDown(){
        mPressedPos = mCurrentPos;
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

    public boolean hasMovedAfterPressedDown() {
        return mCurrentPos != mPressedPos;
    }

    public boolean isInStartPosition() {
        return mCurrentPos == POS_START;
    }

    public boolean isAlreadyHere(int to) {
        return mCurrentPos == to;
    }

    public boolean willOverTop(int to) {
        return to < POS_START;
    }
}