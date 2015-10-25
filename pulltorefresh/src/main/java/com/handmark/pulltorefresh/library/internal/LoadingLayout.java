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

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.FrameLayout;

import com.handmark.pulltorefresh.library.ILoadingLayout;

@SuppressLint("ViewConstructor")
public abstract class LoadingLayout extends FrameLayout implements ILoadingLayout {

	public LoadingLayout(Context context) {
		super(context);
	}

	abstract public int getContentSize();

	abstract public void refreshing();

	abstract public void pullToRefresh();

	abstract public void reset();

	abstract public void releaseToRefresh();

	abstract public void setWidth(int maximumPullScroll);

	abstract public void setHeight(int maximumPullScroll);

	abstract public void onPull(float scale);

	abstract public void hideAllViews();

	abstract public void showInvisibleViews();


}
