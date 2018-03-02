package io.cess.core.wheel;

import android.content.Context;
import android.util.AttributeSet;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.cess.core.R;
import io.cess.core.ResView;
import io.cess.core.annotation.ResCls;
import io.cess.core.annotation.ResId;
import io.cess.core.annotation.ViewById;
import io.cess.core.wheel.adapters.ArrayWheelAdapter;

/**
 * Created by Administrator on 2017/6/8.
 */
@ResCls(R.class)
@ResId(id="io_cess_core_wheel_totalwheelview")
public class TotalWheelView extends ResView implements OnWheelChangedListener {

    public TotalWheelView(Context context, AttributeSet attrs, int defStyle) {super(context, attrs, defStyle);}
    public TotalWheelView(Context context, AttributeSet attrs) {super(context, attrs);}
    public TotalWheelView(Context context) {super(context);}

    @ViewById(id="io_cess_core_totalwheel_province")
    private WheelView mProvince;
    @ViewById(id="io_cess_core_totalwheel_city")
    private WheelView mCity;
    @ViewById(id="io_cess_core_totalwheel_area")
    private WheelView mArea;

    private JSONObject mJsonObj;
    private List<Area> areaList;

    private String[] mProvinceDatas;
    private Map<String, String[]> mCitisDatasMap;
    private Map<String, String[]> mAreaDatasMap;
    protected Map<String, String> mAreaCodeDatasMap;
    private String mCurrentProviceName="北京市";
    private String mCurrentCityName="市辖区";
    private String mCurrentAreaName="东城区";
    String cityName;
    String areaInfo;
    private int areaCode;


    @Override
    protected void onCreate() {
        initJsonData();
        initDatas();
        mProvince.setViewAdapter(new ArrayWheelAdapter<String>(getContext(),mProvinceDatas));
        mProvince.addChangingListener(this);
        mCity.addChangingListener(this);
        mArea.addChangingListener(this);
        mProvince.setVisibleItems(7);
        mCity.setVisibleItems(7);
        mArea.setVisibleItems(7);
        updateCities();
    }

    private void initJsonData(){
        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = getActivity().getAssets().open("area.txt");

            int len = -1;
            byte[] buf = new byte[1024 * 200];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "gbk"));
            }
            is.close();
            areaList = JSON.parseArray(sb.toString(), Area.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initDatas() {
        mCitisDatasMap = new HashMap<String, String[]>();
        mAreaDatasMap = new HashMap<String, String[]>();
        mProvinceDatas = new String[areaList.size()];
        mAreaCodeDatasMap = new HashMap<String, String>();
        for (int i = 0; i < areaList.size(); i++) {
            Area jsonP = areaList.get(i);// 每个省
            String province = jsonP.getAreaName();// 省名字

            mProvinceDatas[i] = province;

            List<Area> jsonCs = jsonP.getChilds();

            String[] mCitiesDatas = new String[jsonCs.size()];
            for (int j = 0; j < jsonCs.size(); j++) {
                Area jsonCity = jsonCs.get(j);
                String city = jsonCity.getAreaName();// 市名字
                mCitiesDatas[j] = city;
                List<Area> jsonAreas = jsonCity.getChilds();

                String[] mAreasDatas = new String[jsonAreas.size()];// 当前市的所有区
                for (int k = 0; k < jsonAreas.size(); k++) {
                    String area = jsonAreas.get(k).getAreaName();// 区域的名称
                    mAreasDatas[k] = area;
                    mAreaCodeDatasMap.put(jsonAreas.get(k).getAreaName(), String.valueOf(jsonAreas.get(k).getId()));
                }
                mAreaDatasMap.put(city, mAreasDatas);
            }
            mCitisDatasMap.put(province, mCitiesDatas);
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mProvince) {
            updateCities();
        } else if (wheel == mCity) {
            updateAreas();
        } else if (wheel == mArea) {
            mCurrentAreaName = mAreaDatasMap.get(cityName)[newValue];
            areaCode = Integer.parseInt(mAreaCodeDatasMap.get(mAreaDatasMap.get(cityName)[newValue]));
            areaInfo=mCurrentProviceName+mCurrentCityName+mCurrentAreaName;
        }
    }
    private void updateAreas() {
        int pCurrent = mCity.getCurrentItem();

        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];

        if (mCurrentCityName.equals("市辖区") || mCurrentCityName.equals("县")) {
            cityName = mCurrentCityName + "_" + mCurrentProviceName.replaceAll("市", "");
        } else {
            cityName = mCurrentCityName;
        }

        String[] areas = mAreaDatasMap.get(cityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mArea.setViewAdapter(new ArrayWheelAdapter<String>(this.getActivity(), areas));
        mArea.setCurrentItem(0);
        mCurrentAreaName = mAreaDatasMap.get(cityName)[0];
        areaInfo=mCurrentProviceName+mCurrentCityName+mCurrentAreaName;
        areaCode = Integer.parseInt(mAreaCodeDatasMap.get(mCurrentAreaName));
    }
    private void updateCities() {
        int pCurrent = mProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        for (int i = 0; i < cities.length; i++) {
            if (cities[i].contains("_")) {
                cities[i] = changeCity(cities[i]);
            }
        }
        mCity.setViewAdapter(new ArrayWheelAdapter<String>(this.getActivity(), cities));
        mCity.setCurrentItem(0);
        updateAreas();
    }
    private String changeCity(String s) {
        String str = s.substring(0, s.indexOf("_"));

        return str;
    }

    public String getAreaInfo(){
        return areaInfo;
    }
    public int getAreaCode(){
        return areaCode;
    }

}
