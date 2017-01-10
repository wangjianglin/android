package lin.core.ptr.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import lin.core.ptr.PtrUIHandler;
import lin.core.ptr.PtrView;
import lin.core.ptr.indicator.PtrIndicator;
import lin.core.R;

public class PtrDefaultHeader extends FrameLayout implements PtrUIHandler {

    private static SimpleDateFormat sDataFormat = new SimpleDateFormat("MM-dd HH:mm");
    private int mRotateAniTime = 150;
    private RotateAnimation mFlipAnimation;
    private RotateAnimation mReverseFlipAnimation;
    private TextView mTitleTextView;
    private View mRotateView;
    private View mProgressBar;
    private long mLastUpdateTime = -1;
    private TextView mLastUpdateTextView;
    private boolean mShouldShowLastUpdate;

    private LastUpdateTimeUpdater mLastUpdateTimeUpdater = new LastUpdateTimeUpdater();

    public PtrDefaultHeader(Context context) {
        super(context);
        initViews(null);
    }

    public PtrDefaultHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs);
    }

    public PtrDefaultHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(attrs);
    }

    protected void initViews(AttributeSet attrs) {

        buildAnimation();
        View header = LayoutInflater.from(getContext()).inflate(R.layout.lin_core_ptr_default_header, this);

        mRotateView = header.findViewById(R.id.lin_core_ptr_default_header_rotate_view);

        mTitleTextView = (TextView) header.findViewById(R.id.lin_core_ptr_default_header_rotate_view_header_title);
        mLastUpdateTextView = (TextView) header.findViewById(R.id.lin_core_ptr_default_header_rotate_view_header_last_update);
        mProgressBar = header.findViewById(R.id.lin_core_ptr_default_header_rotate_view_progressbar);

        resetView();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mLastUpdateTimeUpdater != null) {
            mLastUpdateTimeUpdater.stop();
        }
    }


    private void buildAnimation() {
        mFlipAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mFlipAnimation.setDuration(mRotateAniTime);
        mFlipAnimation.setFillAfter(true);

        mReverseFlipAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setDuration(mRotateAniTime);
        mReverseFlipAnimation.setFillAfter(true);
    }

    private void resetView() {
        hideRotateView();
        mProgressBar.setVisibility(INVISIBLE);
    }

    private void hideRotateView() {
        mRotateView.clearAnimation();
        mRotateView.setVisibility(INVISIBLE);
    }

    @Override
    public void onUIReset(PtrView ptr) {

        resetView();
        mShouldShowLastUpdate = true;
        tryUpdateLastUpdateTime();
    }

    @Override
    public void onUIPrepare(PtrView ptr) {

        mShouldShowLastUpdate = true;
        tryUpdateLastUpdateTime();
        mLastUpdateTimeUpdater.start();

        mProgressBar.setVisibility(INVISIBLE);

        mRotateView.setVisibility(VISIBLE);
        mTitleTextView.setVisibility(VISIBLE);
        mTitleTextView.setText(getResources().getString(R.string.lin_core_ptr_pull_down_to_refresh));
    }

    @Override
    public void onUIBegin(PtrView ptr) {

        mShouldShowLastUpdate = false;
        hideRotateView();
        mProgressBar.setVisibility(VISIBLE);
        mTitleTextView.setVisibility(VISIBLE);
        mTitleTextView.setText(R.string.lin_core_ptr_refreshing);

        tryUpdateLastUpdateTime();
        mLastUpdateTimeUpdater.stop();
    }

    @Override
    public void onUIComplete(PtrView ptr) {

        hideRotateView();
        mProgressBar.setVisibility(INVISIBLE);

        mTitleTextView.setVisibility(VISIBLE);
        mTitleTextView.setText(getResources().getString(R.string.lin_core_ptr_refresh_complete));

        // update last update time
        mLastUpdateTime = new Date().getTime();
    }

    private void tryUpdateLastUpdateTime() {
        if (!mShouldShowLastUpdate) {
            mLastUpdateTextView.setVisibility(GONE);
        } else {
            String time = getLastUpdateTime();
            if (TextUtils.isEmpty(time)) {
                mLastUpdateTextView.setVisibility(GONE);
            } else {
                mLastUpdateTextView.setVisibility(VISIBLE);
                mLastUpdateTextView.setText(time);
            }
        }
    }

    private String getLastUpdateTime() {

        if (mLastUpdateTime == -1) {
            return null;
        }
        long diffTime = new Date().getTime() - mLastUpdateTime;
        int seconds = (int) (diffTime / 1000);
        if (diffTime < 0) {
            return null;
        }
        if (seconds <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getContext().getString(R.string.lin_core_ptr_last_update));

        if (seconds < 60) {
            sb.append(seconds + getContext().getString(R.string.lin_core_ptr_seconds_ago));
        } else {
            int minutes = (seconds / 60);
            if (minutes > 60) {
                int hours = minutes / 60;
                if (hours > 24) {
                    Date date = new Date(mLastUpdateTime);
                    sb.append(sDataFormat.format(date));
                } else {
                    sb.append(hours + getContext().getString(R.string.lin_core_ptr_hours_ago));
                }

            } else {
                sb.append(minutes + getContext().getString(R.string.lin_core_ptr_minutes_ago));
            }
        }
        return sb.toString();
    }

    @Override
    public void onUIPositionChange(PtrView ptr, boolean isUnderTouch, PtrView.Status status, PtrIndicator ptrIndicator) {

        final int mOffsetToRefresh = ptrIndicator.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();

        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrView.Status.PREPARE) {
                crossRotateLineFromBottomUnderTouch(ptr);
                if (mRotateView != null) {
                    mRotateView.clearAnimation();
                    mRotateView.startAnimation(mReverseFlipAnimation);
                }
            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrView.Status.PREPARE) {
                crossRotateLineFromTopUnderTouch(ptr);
                if (mRotateView != null) {
                    mRotateView.clearAnimation();
                    mRotateView.startAnimation(mFlipAnimation);
                }
            }
        }
    }

    private void crossRotateLineFromTopUnderTouch(PtrView ptr) {
        if (!ptr.isPullToRefresh()) {
            mTitleTextView.setVisibility(VISIBLE);
            mTitleTextView.setText(R.string.lin_core_ptr_release_to_refresh);
        }
    }

    private void crossRotateLineFromBottomUnderTouch(PtrView ptr) {
        mTitleTextView.setVisibility(VISIBLE);
        mTitleTextView.setText(getResources().getString(R.string.lin_core_ptr_pull_down_to_refresh));
    }

    private class LastUpdateTimeUpdater implements Runnable {

        private boolean mRunning = false;

        private void start() {
            mRunning = true;
            run();
        }

        private void stop() {
            mRunning = false;
            removeCallbacks(this);
        }

        @Override
        public void run() {
            tryUpdateLastUpdateTime();
            if (mRunning) {
                postDelayed(this, 1000);
            }
        }
    }
}
