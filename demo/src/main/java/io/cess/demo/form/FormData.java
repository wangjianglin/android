package io.cess.demo.form;

import io.cess.util.LocalStorage;

/**
 * @author lin
 * @date 18/01/2017.
 */

public class FormData {

    private Class<?> segueCls;
    private CharSequence segueClsStr;
    private CharSequence rowText;

    private boolean check;

    public Class<?> getSegueCls() {
        return segueCls;
    }

    public void setSegueCls(Class<?> segueCls) {
        this.segueCls = segueCls;
        segueClsStr = segueCls.getName();
    }

    public CharSequence getSegueClsStr() {
        return segueClsStr;
    }

    public void setSegueClsStr(CharSequence segueClsStr) {
        this.segueClsStr = segueClsStr;
    }

    public CharSequence getRowText() {
        return rowText;
    }

    public void setRowText(CharSequence rowText) {
        this.rowText = rowText;
        System.out.println(rowText);
    }

    public boolean isCheck() {
        return LocalStorage.getItem("check",false);
    }

//    @Bindable
    public void setCheck(boolean check) {
        this.check = check;
        LocalStorage.setItem("check",check);
    }
}
