package dong.lan.flextime.bean;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by 梁桂栋 on 2015/12/11.
 */
public class LocDes {
    private LatLng latLng;
    private String des;
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
