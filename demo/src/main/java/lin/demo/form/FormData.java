package lin.demo.form;

/**
 * Created by lin on 18/01/2017.
 */

public class FormData {

    private Class<?> segueCls;
    private String segueClsStr;
    private String rowText;

    public Class<?> getSegueCls() {
        return segueCls;
    }

    public void setSegueCls(Class<?> segueCls) {
        this.segueCls = segueCls;
        segueClsStr = segueCls.getName();
    }

    public String getSegueClsStr() {
        return segueClsStr;
    }

    public void setSegueClsStr(String segueClsStr) {
        this.segueClsStr = segueClsStr;
    }

    public String getRowText() {
        return rowText;
    }

    public void setRowText(String rowText) {
        this.rowText = rowText;
        System.out.println(rowText);
    }
}
