package dong.lan.flextime.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;

import java.util.ArrayList;
import java.util.List;

import dong.lan.flextime.R;
import dong.lan.flextime.adapter.BaseListAdapter;

/**
 * Created by 桂栋 on 2015/8/4.
 */
public class OfflineMapActivity  extends  BaseActivity implements MKOfflineMapListener, View.OnClickListener, AdapterView.OnItemClickListener {

    private MKOfflineMap mOffline = null;
    private TextView bar_left,bar_center;
    private List<City> allCity = new ArrayList<City>();
    private LinearLayout downloadLayout;
    private TextView status;
    private int ID;
    private EditText searchName;
    private ProgressBar searchPro;
    /**
     * 已下载的离线地图信息列表
     */
    private ArrayList<MKOLUpdateElement> localMapList = null;
    private LocalMapAdapter lAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        mOffline = new MKOfflineMap();
        mOffline.init(this);
        initView();
    }
    private void initView() {

        bar_center = (TextView) findViewById(R.id.bar_center);
        bar_left = (TextView) findViewById(R.id.bar_left);
        downloadLayout = (LinearLayout) findViewById(R.id.offline_download_layout);
        bar_left.setOnClickListener(this);
        bar_center.setText("离线地图");
        searchName = (EditText) findViewById(R.id.searchName);
        searchPro = (ProgressBar) findViewById(R.id.search_pro);
        searchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if(searchName.getText().toString().equals("") && downloadLayout!=null && ID==-1) {
//                    downloadLayout.setVisibility(View.GONE);ID=-1;
//                }
//                else if(!searchName.getText().toString().equals("") && downloadLayout!=null && ID!=-1)
//                    downloadLayout.setVisibility(View.VISIBLE);
            }
        });

        ListView allCityList = (ListView) findViewById(R.id.allcitylist);
        // 获取所有支持离线地图的城市
        ArrayList<String> allCities = new ArrayList<String>();
        ArrayList<MKOLSearchRecord> records2 = mOffline.getOfflineCityList();
            for (MKOLSearchRecord r : records2) {
                City city = new City(r.cityName,r.cityID,r.size);
                allCity.add(city);
//                allCities.add(r.cityName + "(" + r.cityID + ")" + "   --"
//                        + this.formatDataSize(r.size));
            }
        allCityAdapter adapter = new allCityAdapter(this,allCity);
        allCityList.setAdapter(adapter);
        allCityList.setOnItemClickListener(this);

        LinearLayout cl = (LinearLayout) findViewById(R.id.citylist_layout);
        LinearLayout lm = (LinearLayout) findViewById(R.id.localmap_layout);
        lm.setVisibility(View.GONE);
        cl.setVisibility(View.VISIBLE);

        // 获取已下过的离线地图信息
        localMapList = mOffline.getAllUpdateInfo();
        if (localMapList == null) {
            localMapList = new ArrayList<MKOLUpdateElement>();
        }

        ListView localMapListView = (ListView) findViewById(R.id.localmaplist);
        lAdapter = new LocalMapAdapter();
        localMapListView.setAdapter(lAdapter);
    }

    /**
     * 切换至城市列表
     *
     * @param view
     */
    public void clickCityListButton(View view) {
        LinearLayout cl = (LinearLayout) findViewById(R.id.citylist_layout);
        LinearLayout lm = (LinearLayout) findViewById(R.id.localmap_layout);
        lm.setVisibility(View.GONE);
        cl.setVisibility(View.VISIBLE);

    }

    /**
     * 切换至下载管理列表
     *
     * @param view
     */
    public void clickLocalMapListButton(View view) {
        LinearLayout cl = (LinearLayout) findViewById(R.id.citylist_layout);
        LinearLayout lm = (LinearLayout) findViewById(R.id.localmap_layout);
        lm.setVisibility(View.VISIBLE);
        cl.setVisibility(View.GONE);
    }

    /**
     * 搜索离线需市
     *
     * @param view
     */

    public void search(View view) {
        searchPro.setVisibility(View.VISIBLE);
        ArrayList<MKOLSearchRecord> records = mOffline.searchCity(searchName
                .getText().toString());
        if (records == null || records.size() != 1) {
            Show("搜索失败");
            searchPro.setVisibility(View.GONE);
            return;
        }
        ID=records.get(0).cityID;
        Show(searchName.getText().toString()+"搜索成功~");
        searchPro.setVisibility(View.GONE);
        if(downloadLayout!=null)
            downloadLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 开始下载
     *
     */
    public void start(int id) {
        mOffline.start(id);
        downloadLayout.setVisibility(View.VISIBLE);
        status = (TextView) findViewById(R.id.state);
        ID=id;
        clickLocalMapListButton(null);
        Toast.makeText(this, "开始下载离线地图", Toast.LENGTH_SHORT)
                .show();
        updateView();
    }

    /**
     * 暂停下载
     *
     */
    public void stop(int id) {
        mOffline.pause(id);
        Toast.makeText(this, "暂停下载离线地图. cityid: " + id, Toast.LENGTH_SHORT)
                .show();
        updateView();
    }

    /**
     * 删除离线地图
     *
     */
    public void remove(int id) {
        mOffline.remove(id);
        Toast.makeText(this, "删除离线地图. cityid: " + id, Toast.LENGTH_SHORT)
                .show();
        updateView();
        downloadLayout.setVisibility(View.GONE);
        ID=-1;
    }

    /**
     * 从SD卡导入离线地图安装包
     *
     * @param view
     */
    public void importFromSDCard(View view) {
        int num = mOffline.importOfflineData();
        String msg = "";
        if (num == 0) {
            msg = "没有导入离线包，这可能是离线包放置位置不正确，或离线包已经导入过";
        } else {
            msg = String.format("成功导入 %d 个离线包，可以在下载管理查看", num);
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        updateView();
    }

    /**
     * 更新状态显示
     */
    public void updateView() {
        localMapList = mOffline.getAllUpdateInfo();
        if (localMapList == null) {
            localMapList = new ArrayList<MKOLUpdateElement>();
        }
        lAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        MKOLUpdateElement temp = mOffline.getUpdateInfo(ID);
        if (temp != null && temp.status == MKOLUpdateElement.DOWNLOADING) {
            mOffline.pause(ID);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public String formatDataSize(int size) {
        String ret = "";
        if (size < (1024 * 1024)) {
            ret = String.format("%dK", size / 1024);
        } else {
            ret = String.format("%.1fM", size / (1024 * 1024.0));
        }
        return ret;
    }

    @Override
    protected void onDestroy() {
        /**
         * 退出时，销毁离线地图模块
         */
        mOffline.destroy();
        super.onDestroy();
    }

    @Override
    public void onGetOfflineMapState(int type, int state) {
        switch (type) {
            case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
                MKOLUpdateElement update = mOffline.getUpdateInfo(state);
                // 处理下载进度更新提示
                if (update != null) {
                    status.setText(String.format("%s : %d%%", update.cityName,
                            update.ratio));
                    updateView();
                }
            }
            break;
            case MKOfflineMap.TYPE_NEW_OFFLINE:
                // 有新离线地图安装
                Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
                break;
            case MKOfflineMap.TYPE_VER_UPDATE:
                 //版本更新提示
                // MKOLUpdateElement e = mOffline.getUpdateInfo(state);

                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bar_left:
                finish();
                break;

        }
    }


    public void startDown(View view) {
        start(ID);
    }

    public void pauseDown(View view) {
        stop(ID);
    }

    public void cancelDown(View view) {
        remove(ID);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        new android.app.AlertDialog.Builder(OfflineMapActivity.this,R.style.DialogMDStyle).setTitle("下载")
                .setMessage("下载："+allCity.get(position).cityName+"?")
                .setNegativeButton("取消",null)
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        start(allCity.get(position).cityID);
                    }
                }).setCancelable(true).show();

    }

    /**
     * 离线地图管理列表适配器
     */
    public class LocalMapAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return localMapList.size();
        }

        @Override
        public Object getItem(int index) {
            return localMapList.get(index);
        }

        @Override
        public long getItemId(int index) {
            return index;
        }

        @Override
        public View getView(int index, View view, ViewGroup arg2) {
            MKOLUpdateElement e = (MKOLUpdateElement) getItem(index);
            view = View.inflate(OfflineMapActivity.this,
                    R.layout.offline_localmap_list, null);
            initViewItem(view, e);
            return view;
        }

        void initViewItem(View view, final MKOLUpdateElement e) {
            Button display = (Button) view.findViewById(R.id.display);
            Button remove = (Button) view.findViewById(R.id.remove);
            TextView title = (TextView) view.findViewById(R.id.title);
            TextView update = (TextView) view.findViewById(R.id.update);
            TextView ratio = (TextView) view.findViewById(R.id.ratio);
            ratio.setText(e.ratio + "%");
            title.setText(e.cityName);
            if (e.update) {
                update.setText("可更新");
            } else {
                update.setText("最新");
            }
            if (e.ratio != 100) {
                display.setEnabled(false);
            } else {
                display.setEnabled(true);
            }
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mOffline.remove(e.cityID);
                    updateView();
                }
            });
            display.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("x", e.geoPt.longitude);
                    intent.putExtra("y", e.geoPt.latitude);
                    intent.setClass(OfflineMapActivity.this, BaseMapActivity.class);
                    startActivity(intent);
                }
            });
        }

    }
    public class allCityAdapter extends BaseListAdapter<City>
    {

        public allCityAdapter(Context context, List<City> list) {
            super(context, list);
        }

        @Override
        public View bindView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_offline_citylist, null);
            }
            final City city = allCity.get(position);
            if(city==null)
                return  null;
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.cityID = (TextView) convertView.findViewById(R.id.city_id);
            viewHolder.cityName = (TextView) convertView.findViewById(R.id.city_name);
            viewHolder.citySize = (TextView) convertView.findViewById(R.id.city_size);
            viewHolder.cityID.setText(city.cityID+"");
            viewHolder.cityName.setText(city.cityName);
            viewHolder.citySize.setText(getFormatDataSize(city.citySize));
            return convertView;
        }
    }

    private String getFormatDataSize(int si)
    {
        return  this.formatDataSize(si);
    }
    private static class City
    {

        public City(String cityName,int ID,int citySize)
        {
            this.cityName =cityName;
            this.cityID =ID;
            this.citySize = citySize;
        }
        public String cityName;
        public int cityID;
        public int citySize;
    }
    public static class ViewHolder
    {
        TextView cityName;
        TextView cityID;
        TextView citySize;
    }
}
