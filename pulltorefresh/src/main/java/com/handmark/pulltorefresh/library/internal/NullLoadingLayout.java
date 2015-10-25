/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.handmark.pulltorefresh.library.ILoadingLayout;

public class NullLoadingLayout extends LoadingLayout{


	private Orientation mScrollDirection;
	public NullLoadingLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs) {
		super(context);
		mScrollDirection = scrollDirection;
	}

	public final void setHeight(int height) {
		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
		lp.height = height;
		requestLayout();
	}

	public final void setWidth(int width) {
		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
		lp.width = width;
		requestLayout();
	}

	public final int getContentSize() {
		switch (mScrollDirection) {
			case HORIZONTAL:
				return 100;
			case VERTICAL:
			default:
				return 100;
		}
	}

	@Override
	public void setLastUpdatedLabel(CharSequence label) {
		
	}

	@Override
	public void setLoadingDrawable(Drawable drawable) {
		
	}

	@Override
	public void setPullLabel(CharSequence pullLabel) {
		
	}

	@Override
	public void setRefreshingLabel(CharSequence refreshingLabel) {
		
	}

	@Override
	public void setReleaseLabel(CharSequence releaseLabel) {
		
	}

	@Override
	public void setTextTypeface(Typeface tf) {
		
	}

	@Override
	public void refreshing() {
		
	}

	@Override
	public void pullToRefresh() {
		
	}

	@Override
	public void reset() {
		
	}

	@Override
	public void releaseToRefresh() {
		
	}

	@Override
	public void onPull(float scale) {
		
	}

	@Override
	public void hideAllViews() {
		
	}

	@Override
	public void showInvisibleViews() {
		
	}
}
