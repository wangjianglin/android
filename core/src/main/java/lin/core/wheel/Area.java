package lin.core.wheel;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */

public class Area {
    private int id;
    private int level;
    private String areaName;
    private List<Area> childs;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getAreaName() {
        return areaName;
    }
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    public List<Area> getChilds() {
        return childs;
    }
    public void setChilds(List<Area> childs) {
        this.childs = childs;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
}
