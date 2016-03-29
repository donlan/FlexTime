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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.update.BmobUpdateAgent;
import de.greenrobot.event.EventBus;
import dong.lan.flextime.Config;
import dong.lan.flextime.Interface.onItemClickListener;
import dong.lan.flextime.R;
import dong.lan.flextime.adapter.MainTodoAdapter;
import dong.lan.flextime.bean.ToDoEvent;
import dong.lan.flextime.bean.ToDoItem;
import dong.lan.flextime.bean.Todo;
import dong.lan.flextime.dao.ToDoItemDao;
import dong.lan.flextime.dao.TodoDao;
import dong.lan.flextime.db.DBManager;
import dong.lan.flextime.services.WorkService;
import dong.lan.flextime.utils.SP;
import dong.lan.flextime.utils.SortManager;
import dong.lan.flextime.utils.TimeUtil;
import dong.lan.flextime.utils.TodoManager;
import dong.lan.flextime.utils.UserManager;

public class MainActivity extends BaseActivity implements onItemClickListener, BDLocationListener, SwipeRefreshLayout.OnRefreshListener {

    public static final int REFRESH = 1;
    private static final int SWIPE_REFRESH = 2;

    boolean isMain = true;
    int status;
    int mode;
    int levelFirst;
    int levelSecond;
    int level;
    List<Todo> todos;
    List<Todo> timeOutTodos;
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
    public void toUserCenter() {
        startActivity(new Intent(MainActivity.this, UserCenterActivity.class));
    }

    private MainTodoAdapter adapter;
    private MainTodoAdapter rAdapter;
    private LocationClient mLocClient;
    private ContentValues values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        BmobUpdateAgent.update(this);
        EventBus.getDefault().register(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                startActivity(new Intent(MainActivity.this, AddOrderTodoActivity.class));
            }
        });
        resetWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DefineFactorActivity.class));
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
                TodoManager.get().changeMode(checkedId);
            }
        });

        statusGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                TodoManager.get().changeStatus(checkedId);
            }
        });

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserManager.getManager().isLogin()) {
                    startActivity(new Intent(MainActivity.this, UserCenterActivity.class));
                    //userInfo.setText(BmobUser.getCurrentUser(MainActivity.this, User.class).toString());
                } else {
                    userInfo.setText("登录便可可同步日程信息");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(false);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(Config.SCAN_DELAY);
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
    }

    private void changeAndRefresh() {
        if (status != SP.getStatus() || mode != SP.getMode()) {
            resortTodos();
            adapter.setGoodStatus(status==Config.GOOD);
        }
        if (todos == null)
            return;
        switch (mode) {
            case Config.WORK:
                level = levelSecond;
                if (level > todos.size())
                    level = todos.size();
                break;
            case Config.NORMAL:
                level = levelFirst;
                if (level > todos.size())
                    level = todos.size();
                break;
            case Config.BUSY:
                level = todos.size();
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
        if (todos == null)
            return;
        switch (mode) {
            case Config.WORK:
                modeWork.setChecked(true);
                level = levelSecond;
                if (level > todos.size())
                    level = todos.size();
                break;
            case Config.NORMAL:
                modeNormal.setChecked(true);
                level = levelFirst;
                if (level > todos.size())
                    level = todos.size();
                break;
            case Config.BUSY:
                modeBusy.setChecked(true);
                level = todos.size();
                break;
        }
        Config.LEVEL = level;

    }

    private void resortTodos() {
        if (todos == null)
            return;
        Collections.sort(todos);
        adapter.notifyDataSetChanged();
    }


    public void setDynaFresh(Todo todo) {
        if (todo != null) {
            ToDoItem toDoItem = todo.getTodos().get(0);
            Intent notifyIntent =
                    new Intent(this, AddTodoActivity.class);
            notifyIntent.putExtra("TODO_ID", todo.getId());
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent notifyPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            todo.getCreateTime().hashCode(),
                            notifyIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel(todo.getCreateTime().hashCode());
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.logo)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo))
                    .setOnlyAlertOnce(true)
                    .setContentTitle("有新的日程可进行")
                    .setContentText(toDoItem.getInfo());
            NotificationCompat.InboxStyle inboxStyle =
                    new NotificationCompat.InboxStyle();
            String[] events = new String[5];
            events[0] = "最晚结束时间 " + TimeUtil.defaultFormat(toDoItem.getDeadline());
            events[1] = "最佳结束时间 " + TimeUtil.defaultFormat(toDoItem.getFinishTime());
            events[2] = TimeUtil.getRemainTime(toDoItem.getFinishTime());
            events[3] = "开始时间 " + TimeUtil.longToString(toDoItem.getStartTime(), TimeUtil.FORMAT_DATA_TIME_SECOND);
            if (toDoItem.getPoint() != null)
                events[4] = toDoItem.getLoc();
            inboxStyle.setBigContentTitle(toDoItem.getInfo());
            for (String event : events) {

                inboxStyle.addLine(event);
            }
            mBuilder.setStyle(inboxStyle);
            mBuilder.setSound(Uri.parse(SP.getAlertSoundPath()));
            mBuilder.setContentIntent(notifyPendingIntent);
            mBuilder.setAutoCancel(true);
            mBuilder.setVibrate(new long[]{1000, 500, 1000});
            mNotificationManager.notify(toDoItem.getCreateTime().hashCode(), mBuilder.build());
        }
    }


    public void onEventMainThread(ToDoEvent toDoEvent) {
        Todo todo = toDoEvent.getTodo();
        switch (toDoEvent.getType()) {
            case ToDoEvent.EVENT_ORDER_UPDATE:
                adapter.notifyItemChanged(toDoEvent.getPos());
                break;
            case ToDoEvent.EVENT_ORDER_DONE:
                adapter.addTodo(0,todo);
                break;
            case ToDoEvent.REFRESH_TODO:
                adapter.notifyDataSetChanged();
                rAdapter.notifyDataSetChanged();
                break;
            case ToDoEvent.EVENT_ADD:
                adapter.addTodo(0, todo);
                break;
            case ToDoEvent.EVENT_UPDATE:
                adapter.updateTodo(todo, toDoEvent.getPos());
                if (rAdapter != null && !isMain) {
                    if (TodoManager.get().isTodoTimeOut(todo)) {
                        values.clear();
                        values.put(ToDoItemDao.FLAG, TodoDao.FLAG_TIME_OUT);
                        DBManager.getManager().updateTodo(values, todo.getId());
                        adapter.addTodo(0, todo);
                        rAdapter.deleteTodo(toDoEvent.getPos());
                    }
                    rAdapter.notifyDataSetChanged();
                }
                break;
            case ToDoEvent.TODO_NOTIFY:
                setDynaFresh(toDoEvent.getTodo());
                break;
            case ToDoEvent.ONTIME_TO_TIMEOUT:
                Todo t = toDoEvent.getTodo();
                values.clear();
                t.setFlag(TodoDao.FLAG_TIME_OUT);
                values.put(TodoDao.FLAG,TodoDao.FLAG_TIME_OUT);
                DBManager.getManager().updateTodo(values, t.getId());
                adapter.deleteTodo(t);
                rAdapter.addTodo(0, t);
                break;
        }
    }

    private void initTodo() {
        todos = DBManager.getManager().getAllTimeOnTodos();
        timeOutTodos = DBManager.getManager().getAllTimeOutTodos();

        startService(new Intent(this, WorkService.class));

        if (todos == null) {
            Show("没有待办记录");
            todos = new ArrayList<>();
            adapter = new MainTodoAdapter(this, todos, true);
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);

        } else {
            TodoManager.get().setTodos(todos);
            Collections.sort(todos);
            adapter = new MainTodoAdapter(this, todos, true);
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);
        }
        if (timeOutTodos == null) {
            timeOutTodos = new ArrayList<>();
            rAdapter = new MainTodoAdapter(MainActivity.this, timeOutTodos, false);
            rAdapter.setOnItemClickListener(this);
            otRecyclerView.setAdapter(rAdapter);
        } else {
            TodoManager.get().setTimeoutTodos(timeOutTodos);
            rAdapter = new MainTodoAdapter(this, timeOutTodos, false);
            rAdapter.setOnItemClickListener(this);
            otRecyclerView.setAdapter(rAdapter);
        }
        values = new ContentValues();

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
    public void itemClick(Todo todo, int pos, int type, boolean isMain) {

        this.isMain = isMain;
        switch (type) {
            case MainTodoAdapter.CLICK:
                if(todo.getType()==TodoDao.TYPE_SINGLE){

                    Intent intent = new Intent(MainActivity.this, AddTodoActivity.class);
                    intent.putExtra("TODO_ID", todo.getId());
                    intent.putExtra("POS", pos);
                    startActivity(intent);
                }else if(todo.getType()==TodoDao.TYPE_ORDER){
                    Intent intent = new Intent(MainActivity.this, AddOrderTodoActivity.class);
                    intent.putExtra("TODO_ID", todo.getId());
                    intent.putExtra("POS", pos);
                    startActivity(intent);
                }
                break;
            case MainTodoAdapter.LONG_CLICK:
                if (isMain)
                    showDelete(todo, pos);
                else {
                    values.clear();
                    values.put(TodoDao.FLAG, TodoDao.FLAG_DONE);
                    DBManager.getManager().updateTodoLable(values, todo.getId());
                    adapter.addTodo(0, todo);
                    rAdapter.deleteTodo(pos);
                }
                break;
            case MainTodoAdapter.DRAG:
                values.clear();
                values.put(ToDoItemDao.FLAG, TodoDao.FLAG_SWAP);
                DBManager.getManager().updateTodoLable(values, todo.getId());
                todos.remove(todo);
                adapter.notifyDataSetChanged();
                break;
        }
    }


    private void showDelete(final Todo todo, final int pos) {
        new AlertDialog.Builder(this, R.style.MyDialogStyleTop)
                .setTitle("你确定删除此个日程安排吗？")
                .setMessage("删除后将不能恢复！！")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager.getManager().deleteTodo(todo.getId());
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
                List<Todo> todos = adapter.getTodos();
                if (todos == null || todos.size() == 0) {
                    mLocClient.stop();
                } else {
                    for (Todo t : todos) {

                        for (int i = 0; i < t.getTodos().size(); i++) {
                            ToDoItem item = t.getTodos().get(i);
                            if (item.getPoint() != null) {
                                if (DistanceUtil.getDistance(latLng, new LatLng(item.getPoint().getLatitude(), item.getPoint().getLongitude())) < 1000) {
                                    setDynaFresh(t);
                                    Show("当前位置附近有待办事件：" + item.getInfo());
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    private void refreshTodos() {
        if (adapter != null) {
            Collections.sort(adapter.getTodos());
            adapter.notifyDataSetChanged();
        }
        rAdapter.notifyDataSetChanged();
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
