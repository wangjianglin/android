package lin.core.ptr;

import lin.core.ptr.indicator.PtrIndicator;

/**
 *
 */
public interface PtrUIHandler {

    /**
     * When the content view has reached top and refresh has been completed, view will be reset.
     *
     * @param frame
     */
    public void onUIReset(PtrView ptr);

    /**
     * prepare for loading
     *
     * @param frame
     */
    public void onUIPrepare(PtrView ptr);

    /**
     * perform refreshing UI
     */
    public void onUIBegin(PtrView ptr);

    /**
     * perform UI after refresh
     */
    public void onUIComplete(PtrView ptr);

    public void onUIPositionChange(PtrView ptr, boolean isUnderTouch, PtrView.Status status, PtrIndicator ptrIndicator);
}
