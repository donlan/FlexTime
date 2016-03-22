package dong.lan.flextime.activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.update.BmobUpdateAgent;
import de.greenrobot.event.EventBus;
import dong.lan.flextime.Config;
import dong.lan.flextime.R;
import dong.lan.flextime.adapter.MainTodoAdapter;
import dong.lan.flextime.bean.ToDo;
import dong.lan.flextime.bean.ToDoEvent;
import dong.lan.flextime.bean.User;
import dong.lan.flextime.dao.ToDoDao;
import dong.lan.flextime.db.DBManager;
import dong.lan.flextime.utils.SP;
import dong.lan.flextime.utils.SortManager;
import dong.lan.flextime.utils.TimeUtil;
import dong.lan.flextime.utils.UserManager;

public class MainActivity extends BaseActivity implements MainTodoAdapter.onItemClickListener, BDLocationListener, SwipeRefreshLayout.OnRefreshListener {

    public static final int REFRESH = 1;
    private static final int SWIPE_REFRESH = 2;
    public static final int DYNA_FRESH = 3;
    public static final int SWAP = 4;
    int REFRESH_DELAY = 60000;
    int SCAN_DELAY = 600000;
    boolean isMain = true;
    int status;
    int mode;
    int levelFirst;
    int levelSecond;
    int level;
    int timeDelay;
    List<ToDo> toDos;
    List<ToDo> timeOutTodos;
    MyHandle handle = new MyHandle();
    @Bind(R.id.bar_add)
    TextView barAdd;
    @Bind(R.id.bar_add_seq)
    TextView barAddSeq;
    @Bind(R.id.outTime_recycleList)
    RecyclerView otRecyclerView;
    @Bind(R.id.main_recycleList)
    RecyclerView recyclerView;
    @Bind(R.id.bar_filter)
    TextView filter;
    @Bind(R.id.drawer_offline_map)
    TextView offlineMap;
    @Bind(R.id.main_swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.drawer_reset_weight)
    TextView resetWeight;

    @Bind(R.id.status_group)
    RadioGroup statusGroup;
    @Bind(R.id.mode_group)
    RadioGroup modeGroup;

    @Bind(R.id.status_bad)
    RadioButton statusBad;
    @Bind(R.id.status_good)
    RadioButton statusGood;

    @Bind(R.id.mode_normal)
    RadioButton modeNormal;
    @Bind(R.id.mode_busy)
    RadioButton modeBusy;
    @Bind(R.id.mode_work)
    RadioButton modeWork;

    @Bind(R.id.user_head)
    ImageView head;

    @Bind(R.id.user_info)
    TextView userInfo;

    @OnClick(R.id.drawer_statistic)
    public void toUserCenter()
    {
        startActivity(new Intent(MainActivity.this,UserCenterActivity.class));
    }
    private MainTodoAdapter adapter;
    private MainTodoAdapter rAdapter;
    private Toolbar toolbar;
    private LocationClient mLocClient;
    private ContentValues values;
    private boolean START = true;
    int TIMES = 0;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        BmobUpdateAgent.update(this);
        EventBus.getDefault().register(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        barAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddTodoActivity.class));
            }
        });
        barAddSeq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddOrderTodoActivity.class));
            }
        });
        resetWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,DefineFactorActivity.class));
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resortTodos();
            }
        });
        offlineMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OfflineMapActivity.class));
            }
        });
        findViewById(R.id.drawer_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
            }
        });
        otRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        refreshLayout.setColorSchemeResources(R.color.md_green_400, R.color.md_blue_400, R.color.md_yellow_400,
                R.color.md_red_400
        );
        refreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        refreshLayout.setOnRefreshListener(this);
        recyclerView.setHasFixedSize(true);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                changeAndRefresh();
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        modeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                changeMode(checkedId);
            }
        });

        statusGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                changeStatus(checkedId);
            }
        });

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserManager.getManager(getApplicationContext()).isLogin()){
                    startActivity(new Intent(MainActivity.this,UserCenterActivity.class));
                    userInfo.setText(BmobUser.getCurrentUser(MainActivity.this, User.class).toString());
                }else {
                    userInfo.setText("登录便可可同步日程信息");
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
            }
        });
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(false);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(SCAN_DELAY);
        option.setIsNeedAddress(true);
        option.setTimeOut(3000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        initTodo();
        setStatusAndMode();
        initFactor();

    }


    private void initFactor() {
        SortManager.init(SP.getImp(), SP.getUrg());
        levelFirst = SP.getLevelFirst();
        levelSecond = SP.getLevelSecond();
        timeDelay = SP.getAlertDelay()*60000;
    }

    private void changeAndRefresh() {
        if (status != SP.getStatus() || mode != SP.getMode()) {
            resortTodos();
        }
        switch (mode) {
            case Config.WORK:
                level=levelSecond;
                if(level>toDos.size())
                    level = toDos.size();
                break;
            case Config.NORMAL:
                level = levelFirst;
                if(level>toDos.size())
                    level = toDos.size();
                break;
            case Config.BUSY:
                level = toDos.size();
                break;
        }
    }

    private void setStatusAndMode() {
        status = SP.getStatus();
        mode = SP.getMode();
        if (status == Config.BAD)
            statusBad.setChecked(true);
        else
            statusGood.setChecked(true);
        switch (mode) {
            case Config.WORK:
                modeWork.setChecked(true);
                level=levelSecond;
                if(level>toDos.size())
                    level = toDos.size();
                break;
            case Config.NORMAL:
                modeNormal.setChecked(true);
                level = levelFirst;
                if(level>toDos.size())
                    level = toDos.size();
                break;
            case Config.BUSY:
                modeBusy.setChecked(true);
                level = toDos.size();
                break;
        }

    }

    private void changeStatus(int checkedId) {
        switch (checkedId) {
            case R.id.status_good:
                SP.setStatus(Config.GOOD);
                Config.STATUS = Config.GOOD;
                break;
            case R.id.status_bad:
                SP.setStatus(Config.BAD);
                Config.STATUS = Config.BAD;
                break;
        }
    }

    private void changeMode(int checkedId) {
        switch (checkedId) {
            case R.id.mode_busy:
                SP.setMode(Config.BUSY);
                Config.MODE = Config.BUSY;
                break;
            case R.id.mode_normal:
                SP.setMode(Config.NORMAL);
                Config.MODE = Config.NORMAL;
                break;
            case R.id.mode_work:
                SP.setMode(Config.WORK);
                Config.MODE = Config.WORK;
                break;
        }
    }



    private void resortTodos() {
        Collections.sort(toDos);
        adapter.notifyDataSetChanged();
    }


    public void setDynaFresh(ToDo toDo) {
        if (toDo != null) {
            Intent notifyIntent =
                    new Intent(this, AddTodoActivity.class);
            notifyIntent.putExtra("TODO", toDo);
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent notifyPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            toDo.getCreateTime().hashCode(),
                            notifyIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel(toDo.getCreateTime().hashCode());
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.logo)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.logo))
                    .setOnlyAlertOnce(true)
                    .setContentTitle("有新的日程可进行")
                    .setContentText(toDo.getInfo());
            NotificationCompat.InboxStyle inboxStyle =
                    new NotificationCompat.InboxStyle();
            String[] events = new String[5];
            events[0] = "最晚结束时间 " + toDo.getDeadline();
            events[1] = "最佳结束时间 " + toDo.getFinishTime();
            events[2] = TimeUtil.getRemainTime(toDo.getFinishTime());
            events[3] = "开始时间 " + TimeUtil.longToString(Long.valueOf(toDo.getStartTime()), TimeUtil.FORMAT_DATA_TIME_SECOND);
            if (toDo.getPoint() != null)
                events[4] = toDo.getLoc();
            inboxStyle.setBigContentTitle(toDo.getInfo());
            for (String event : events) {

                inboxStyle.addLine(event);
            }
            mBuilder.setStyle(inboxStyle);
            mBuilder.setSound(Uri.parse(SP.getAlertSoundPath()));
            mBuilder.setContentIntent(notifyPendingIntent);
            mBuilder.setAutoCancel(true);
            mBuilder.setVibrate(new long[]{1000, 500, 1000});
            mNotificationManager.notify(toDo.getCreateTime().hashCode(), mBuilder.build());
        }
    }

    public void  onEventMainThread(String type){
        if(type.equals("1"))
        {
            for (ToDo t :
                    toDos) {
                t.setWeight(SortManager.getSortWeight(t.getImportant(), t.getUrgent(), t.getStatus()));
                values.clear();
                values.put(ToDoDao.COLUMN_WEIGHT, String.valueOf(t.getWeight()));
                DBManager.getManager().updateTodo(values, t.getCreateTime());
            }
            for (ToDo t :
                    timeOutTodos) {
                t.setWeight(SortManager.getSortWeight(t.getImportant(), t.getUrgent(), t.getStatus()));
                values.clear();
                values.put(ToDoDao.COLUMN_WEIGHT, String.valueOf(t.getWeight()));
                DBManager.getManager().updateTodo(values, t.getCreateTime());
            }
            if (adapter != null) {
                Collections.sort(toDos);
                adapter.notifyDataSetChanged();
            }
            if (rAdapter != null)
                rAdapter.notifyDataSetChanged();
            drawer.closeDrawer(GravityCompat.START);
        }
    }
    public void onEventMainThread(ToDoEvent toDoEvent) {
        ToDo toDo = toDoEvent.getToDo();
        if (adapter == null) {
            adapter = new MainTodoAdapter(this, toDos, true);
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);
        } else {
            if (toDoEvent.getType() == 0) {
                adapter.addTodo(0, toDo);
            } else if (toDoEvent.getType() == 1) {
                adapter.updateTodo(toDo, toDoEvent.getPos());
                if (rAdapter != null && !isMain) {
                    if (TimeUtil.stringToLong(toDo.getFinishTime(), TimeUtil.FORMAT_DATA_TIME_SECOND)
                            - System.currentTimeMillis() > 0) {
                        values.clear();
                        values.put(ToDoDao.COLUMN_LEVEL, "1");
                        DBManager.getManager().updateTodo(values, toDo.getCreateTime());
                        adapter.addTodo(0, toDo);
                        rAdapter.deleteTodo(toDoEvent.getPos());
                    }
                    rAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void initTodo() {
        toDos = DBManager.getManager().getAllTimeOnTodos();
        timeOutTodos = DBManager.getManager().getAllTimeOutTodos();
        if (toDos == null) {
            Show("没有待办记录");
        } else {
            Collections.sort(toDos);
            adapter = new MainTodoAdapter(this, toDos, true);
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);
        }
        if (timeOutTodos != null) {
            rAdapter = new MainTodoAdapter(this, timeOutTodos, false);
            rAdapter.setOnItemClickListener(this);
            otRecyclerView.setAdapter(rAdapter);
        }
        values = new ContentValues();
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        Thread.sleep(REFRESH_DELAY);
                        TIMES++;
                        if (TIMES >= 10 && !START) {
                            START = true;
                            TIMES = 0;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (START) {
                        START = false;
                        if (toDos != null) {
                            int c = 0;
                            for (ToDo toDo : toDos) {

                                if(c>=level)
                                {
                                    break;
                                }
                                if (Math.abs(TimeUtil.getStartTime(toDo.getNeedTime(), toDo.getFinishTime())
                                        - System.currentTimeMillis()) < timeDelay) {
                                    values.clear();
                                    values.put(ToDoDao.COLUMN_WEIGHT, String.valueOf((toDo.getWeight() * 2)));
                                    values.put(ToDoDao.COLUMN_LEVEL, "0");
                                    DBManager.getManager().updateTodo(values, toDo.getCreateTime());
                                    Message msg = new Message();
                                    msg.what = DYNA_FRESH;
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("TODO", toDo);
                                    msg.setData(bundle);
                                    handle.sendMessage(msg);
                                    c++;
                                }
                                if (TimeUtil.stringToLong(toDo.getFinishTime(), TimeUtil.FORMAT_DATA_TIME_SECOND)
                                        - System.currentTimeMillis() < 0) {
                                    Message msg = new Message();
                                    msg.what = SWAP;
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("TODO", toDo);
                                    msg.setData(bundle);
                                    handle.sendMessage(msg);
                                }
                            }
                            START = c != toDos.size();
                        }
                    }
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void itemClick(ToDo toDo, int pos, int type, boolean isMain) {

        this.isMain = isMain;
        switch (type)
        {
            case MainTodoAdapter.CLICK:
                Intent intent = new Intent(MainActivity.this, AddTodoActivity.class);
                intent.putExtra("TODO", toDo);
                intent.putExtra("POS", pos);
                startActivity(intent);
                break;
            case MainTodoAdapter.LONG_CLICK:
                if (isMain)
                    showDelete(toDo, pos);
                else {
                    values.clear();
                    values.put(ToDoDao.COLUMN_LEVEL, "1");
                    DBManager.getManager().updateTodo(values, toDo.getCreateTime());
                    adapter.addTodo(0, toDo);
                    rAdapter.deleteTodo(pos);
                }
                break;
            case MainTodoAdapter.DRAG:
                values.clear();
                values.put(ToDoDao.COLUMN_LEVEL, "-1");
                DBManager.getManager().updateTodo(values, toDo.getCreateTime());
                toDos.remove(toDo);
                adapter.notifyDataSetChanged();
                break;
        }
    }


    private void showDelete(final ToDo toDo, final int pos) {
        new AlertDialog.Builder(this, R.style.MyDialogStyleTop)
                .setTitle("你确定删除此个日程安排吗？")
                .setMessage("删除后将不能恢复！！")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager.getManager().deleteTodo(toDo.getCreateTime());
                        adapter.deleteTodo(pos);
                        adapter.notifyItemRemoved(pos);
                    }
                })
                .setNegativeButton("否", null)
                .show();
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation != null) {
            LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            if (adapter != null) {
                List<ToDo> toDos = adapter.getToDos();
                if (toDos == null || toDos.size() == 0) {
                    mLocClient.stop();
                } else {
                    for (ToDo t : toDos) {
                        if (t.getPoint() != null) {
                            if (DistanceUtil.getDistance(latLng, new LatLng(t.getPoint().getLatitude(), t.getPoint().getLongitude())) < 1000) {
                                setDynaFresh(t);
                                Show("当前位置附近有待办事件：" + t.getInfo());
                            }
                        }
                    }
                }
            }
        }
    }

    private void refreshTodos() {
        if (adapter != null) {
            Collections.sort(adapter.getToDos());
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onRefresh() {
        handle.sendEmptyMessageDelayed(REFRESH, 1000);
        refreshLayout.setRefreshing(false);
    }


    class MyHandle extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESH:
                    refreshTodos();
                    break;
                case SWIPE_REFRESH:
                    refreshTodos();
                    refreshLayout.setRefreshing(false);
                    break;
                case DYNA_FRESH:
                    setDynaFresh((ToDo) msg.getData().getSerializable("TODO"));
                    break;
                case SWAP:
                    values.clear();
                    values.put(ToDoDao.COLUMN_LEVEL, "0");
                    ToDo toDo = (ToDo) msg.getData().getSerializable("TODO");
                    if (rAdapter == null)
                        rAdapter = new MainTodoAdapter(MainActivity.this, timeOutTodos, false);
                    DBManager.getManager().updateTodo(values, toDo.getCreateTime());
                    adapter.deleteTodo(toDo);
                    rAdapter.addTodo(0, toDo);
                    break;
            }
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        resortTodos();
    }

    @Override
    protected void onDestroy() {
        mLocClient.stop();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
