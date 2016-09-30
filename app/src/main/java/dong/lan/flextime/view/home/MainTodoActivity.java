package dong.lan.flextime.view.home;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.update.BmobUpdateAgent;
import dong.lan.flextime.*;
import dong.lan.flextime.bean.Todo;
import dong.lan.flextime.event.TodoEvent;
import dong.lan.flextime.view.handleTodo.AddOrderTodoActivity;
import dong.lan.flextime.view.handleTodo.AddTodoActivity;
import dong.lan.flextime.view.config.CustomSortActivity;
import dong.lan.flextime.view.config.DefineFactorActivity;
import dong.lan.flextime.view.login.LoginActivity;
import dong.lan.flextime.view.map.OfflineMapActivity;
import dong.lan.flextime.view.config.SettingActivity;
import dong.lan.flextime.view.UserCenterActivity;
import dong.lan.flextime.utils.SP;
import dong.lan.flextime.utils.SortManager;
import dong.lan.flextime.utils.TodoManager;
import dong.lan.flextime.utils.UserManager;
import dong.lan.flextime.view.BaseView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MainTodoActivity extends dong.lan.flextime.view.BaseActivity {


    @Bind(R.id.bar_add)
    TextView barAdd;
    @Bind(R.id.bar_add_seq)
    TextView barAddSeq;
    @Bind(R.id.bar_filter)
    TextView filter;

    @Bind(R.id.login_out)
    TextView loginOut;

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






    int status;
    int mode;
    int level;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Fragment[] pager = new Fragment[2];
    private ViewPager mViewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpView(R.layout.activity_main_todo);

        Toolbar toolbar = (Toolbar) findView(R.id.toolbar);
        setSupportActionBar(toolbar);

        EventBus.getDefault().register(this);

        mViewPager = (ViewPager) findView(R.id.container);
        tabLayout = (TabLayout) findView(R.id.tabs);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //changeAndRefresh();
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        
        Observable.create(new Observable.OnSubscribe<Fragment>() {
            @Override
            public void call(Subscriber<? super Fragment> subscriber) {
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                subscriber.onNext(MainTodoFragment.newInstance("MainTodo",0));
                subscriber.onNext(OverTodoFragment.newInstance("OverTodo",1));
                subscriber.onCompleted();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Fragment>() {
                    @Override
                    public void onCompleted() {
                        mViewPager.setAdapter(mSectionsPagerAdapter);
                        tabLayout.setupWithViewPager(mViewPager);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        show(throwable.getMessage());
                    }

                    @Override
                    public void onNext(Fragment frag) {
                        pager[frag.getArguments().getInt(BaseView.VIEW_INDEX)] = frag;
                    }
                });


        ButterKnife.bind(this);
        BmobUpdateAgent.update(this);

        init();
    }


    private void init(){
        barAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainTodoActivity.this, AddTodoActivity.class));
            }
        });
        barAddSeq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainTodoActivity.this, AddOrderTodoActivity.class));
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //resortTodos();
            }
        });
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
                    startActivity(new Intent(MainTodoActivity.this, UserCenterActivity.class));
                } else {
                    startActivity(new Intent(MainTodoActivity.this, LoginActivity.class));
                }
            }
        });

    }

    private void initFactor() {
        SortManager.init(SP.getImp(), SP.getUrg());
        TodoManager.get().initLevel(SP.getLevelFirst(),SP.getLevelSecond());
    }

    private void changeAndRefresh() {
        if (status != SP.getStatus() || mode != SP.getMode()) {
//            resortTodos();
//            adapter.setGoodStatus(status==Config.GOOD);
        }
//        if (todos == null)
//            return;
        switch (mode) {
            case Config.WORK:
                level = TodoManager.get().getLevelSecond();
//                if (level > todos.size())
//                    level = todos.size();
                break;
            case Config.NORMAL:
                //level = todos.size();
                break;
            case Config.BUSY:
//                level = TodoManager.get().getLevelFirst();
//                if (level > todos.size())
//                    level = todos.size();
                break;
        }
        Config.LEVEL = level;
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
                level = TodoManager.get().getLevelSecond();
//                if (level > todos.size())
//                    level = todos.size();
                break;
            case Config.NORMAL:
                modeNormal.setChecked(true);
//                level = todos.size();
                break;
            case Config.BUSY:
                modeBusy.setChecked(true);
                level = TodoManager.get().getLevelFirst();
//                if (level > todos.size())
//                    level = todos.size();
                break;
        }
        Config.LEVEL = level;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                startActivity(new Intent(MainTodoActivity.this, SettingActivity.class));
                break;
            case R.id.action_sort:
                startActivity(new Intent(MainTodoActivity.this, CustomSortActivity.class));
                break;
            case R.id.action_offline_map:
                startActivity(new Intent(MainTodoActivity.this, OfflineMapActivity.class));
                break;
            case R.id.action_statistics:
                startActivity(new Intent(MainTodoActivity.this, UserCenterActivity.class));
                break;
            case R.id.action_sync:
                dialog("It is working ....");
                break;
            case R.id.action_weight_factor:
                startActivity(new Intent(MainTodoActivity.this, DefineFactorActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBusEvent(TodoEvent event){
        int type = event.getEventType();
        switch (type){
            case TodoEvent.EVENT_SINGLE_ADD:
            case TodoEvent.EVENT_MUTIL_ADD:
            case TodoEvent.EVENT_MUTIL_UPDATE:
            case TodoEvent.EVENT_SINGLE_UPDATE:
                ((MainTodoFragment)pager[0]).refresh();
                break;

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return pager[position];
        }

        @Override
        public int getCount() {
            return pager.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pager[position].getArguments().getString(BaseView.VIEW_TITTLE);
        }
    }
}
