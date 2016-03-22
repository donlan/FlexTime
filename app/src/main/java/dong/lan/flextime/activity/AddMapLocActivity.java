package dong.lan.flextime.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import dong.lan.flextime.R;
import dong.lan.flextime.bean.LocDes;

/**
 * Created by 梁桂栋 on 2015/12/11.
 */
public class AddMapLocActivity extends BaseActivity implements OnGetGeoCoderResultListener, BDLocationListener {
    @Bind(R.id.bmapView)
    MapView mapView;
    @Bind(R.id.addTodo_mapDes)
    TextView locDes;
    @Bind(R.id.addTodo_backUp)
    TextView back;
    BaiduMap baiduMap;
    private BitmapDescriptor bitmap;
    private MarkerOptions option;
    private Marker marker;
    private GeoCoder geoCoder;
    private String Des="";
    private LocationClient mLocClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo_map);
        ButterKnife.bind(this);
        baiduMap = mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //构建Marker图标
        bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_gcoding);
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
        baiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                if (marker == null) {
                    option = new MarkerOptions()
                            .position(point)
                            .icon(bitmap);
                    //在地图上添加Marker，并显示
                    marker = (Marker) baiduMap.addOverlay(option);
                } else {
                    marker.setPosition(point);
                }
                geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(point));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (marker == null || marker.getPosition() == null || Des.equals("")) {
                    Show("获取位置信息失败");
                } else
                    EventBus.getDefault().post(new LocDes(marker.getPosition(), locDes.getText().toString()));
                finish();
            }
        });

        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        baiduMap.setMapStatus(msu);
        baiduMap.setMyLocationEnabled(true);
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(2000);
        option.setIsNeedAddress(true);
        option.setTimeOut(3000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bitmap.recycle();
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            locDes.setText("未能反查询到位置描述,请重试");
            Des = "";
        } else {
            locDes.setText((Des = result.getAddress()));
        }
    }


    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation == null) {
            return;
        }
        LatLng loc = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(loc);
        baiduMap.animateMapStatus(u);
        baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, bitmap));
        option = new MarkerOptions().position(loc).icon(bitmap)
                .zIndex(9).draggable(true);
        if (marker == null) {
            marker = (Marker) baiduMap.addOverlay(option);
        }
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(loc));
        mLocClient.stop();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            if (marker == null || marker.getPosition() == null || Des.equals("")) {
                Show("获取位置信息失败");
            } else
                EventBus.getDefault().post(new LocDes(marker.getPosition(), Des));
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
