package io.cess.demo.main;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * @author lin
 * @date 28/11/2016.
 */

public class Data extends BaseObservable {

    private String name;
    private Object intent;
    private String group;

    public Data(){
    }

    public Data(String name){
        this.name = name;
    }

    public Data(String name, Intent intent){
        this.name = name;
        this.intent = intent;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getIntent() {
        return intent;
    }

    public void setIntent(Object intent) {
        this.intent = intent;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
