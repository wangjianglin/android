package lin.demo.form;

/**
 * Created by lin on 18/01/2017.
 */

public class FormData {

    private Class<?> segueCls;
    private CharSequence segueClsStr;
    private CharSequence rowText;

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
}
