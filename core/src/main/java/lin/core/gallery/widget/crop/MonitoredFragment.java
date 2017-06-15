/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package lin.core.gallery.widget.crop;

import android.os.Bundle;

import java.util.ArrayList;

import lin.core.gallery.PhotoBaseActivity;
import lin.core.gallery.PhotoBaseFragment;

/*
 * Modified from original in AOSP.
 */
abstract class MonitoredFragment extends PhotoBaseFragment {

    private final ArrayList<LifeCycleListener> listeners = new ArrayList<LifeCycleListener>();

    public static interface LifeCycleListener {
        public void onActivityCreated(MonitoredFragment activity);
        public void onActivityDestroyed(MonitoredFragment activity);
        public void onActivityStarted(MonitoredFragment activity);
        public void onActivityStopped(MonitoredFragment activity);
    }

    public static class LifeCycleAdapter implements LifeCycleListener {
        public void onActivityCreated(MonitoredFragment activity) {}
        public void onActivityDestroyed(MonitoredFragment activity) {}
        public void onActivityStarted(MonitoredFragment activity) {}
        public void onActivityStopped(MonitoredFragment activity) {}
    }

    public void addLifeCycleListener(LifeCycleListener listener) {
        if (listeners.contains(listener)) return;
        listeners.add(listener);
    }

    public void removeLifeCycleListener(LifeCycleListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (LifeCycleListener listener : listeners) {
            listener.onActivityCreated(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (LifeCycleListener listener : listeners) {
            listener.onActivityDestroyed(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        for (LifeCycleListener listener : listeners) {
            listener.onActivityStarted(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        for (LifeCycleListener listener : listeners) {
            listener.onActivityStopped(this);
        }
    }

}
