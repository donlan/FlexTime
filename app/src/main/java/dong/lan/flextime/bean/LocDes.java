package dong.lan.flextime.bean;

import com.baidu.mapapi.model.LatLng;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 2015/12/11  05:35.
 *
 *
 * 定位信息的封装类，便于在EventBus中传递
 */
public class LocDes {
    private LatLng latLng;      //经纬度
    private String des;         //位置描述信息

    public LocDes(LatLng latLng,String des)
    {
        this.latLng =latLng;
        this.des= des;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
