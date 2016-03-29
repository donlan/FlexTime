package dong.lan.flextime.activity;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import dong.lan.flextime.Config;
import dong.lan.flextime.Interface.OrderTodoItemClick;
import dong.lan.flextime.R;
import dong.lan.flextime.adapter.MyPagerAdapter;
import dong.lan.flextime.adapter.OrderTodoAdapter;
import dong.lan.flextime.bean.LocDes;
import dong.lan.flextime.bean.ToDoEvent;
import dong.lan.flextime.bean.ToDoItem;
import dong.lan.flextime.bean.Todo;
import dong.lan.flextime.dao.TodoDao;
import dong.lan.flextime.db.DBManager;
import dong.lan.flextime.utils.KeyWordManager;
import dong.lan.flextime.utils.TimeUtil;
import dong.lan.flextime.utils.TodoManager;
import dong.lan.flextime.utils.UserManager;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 3/13/2016  09:51.
 */
public class AddOrderTodoActivity extends BaseActivity implements View.OnClickListener, OrderTodoItemClick, View.OnLongClickListener {

    int year;
    int month;
    int day;
    int h;
    int m;
    int count=1;
    StringBuilder sb = new StringBuilder();
    @Bind(R.id.todoItemList)
    RecyclerView recyclerView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.addTodo_setBestTime)
    TextView setBestTimeText;
    @Bind(R.id.addTodo_setDeadline)
    TextView setDeadlineText;
    @Bind(R.id.addTodo_setNeedTime)
    TextView setNeedTime;
    @Bind(R.id.addTodo_Importance_seekBar)
    SeekBar impoSeekBar;
    @Bind(R.id.addTodo_Urgent_seekBar)
    SeekBar urgentSeekBar;
    @Bind(R.id.addTodo_ImportanceText)
    TextView importanceText;
    @Bind(R.id.addTodo_UrgentText)
    TextView urgentText;
    @Bind(R.id.bar_left)
    TextView barLeft;
    @Bind(R.id.bar_center)
    TextView barCenter;
    @Bind(R.id.bar_right)
    TextView barRight;
    @Bind(R.id.addTodo_setLoc)
    TextView setLocDes;
    @Bind(R.id.seqText)
    TextView seqText;
    @Bind(R.id.addTodo_remindCheck)
    CheckBox remindCheck;
    @Bind(R.id.addTodo_info)
    EditText info;

    @Bind(R.id.level_mid)
    RadioButton levelMid;

    @Bind(R.id.level_high)
    RadioButton levelHigh;

    @Bind(R.id.level_low)
    RadioButton levelLow;

    @Bind(R.id.addTodo_tips)
    TextView todoTips;
    private LocDes loc;
    private int pos;
    private boolean ADD = true;
    private Todo todo;
    private OrderTodoAdapter adapter;
    private ToDoItem curItem;
    Map<String, String> map = new HashMap<>();  //保存获取到的分词结果
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order_todo);
        ButterKnife.bind(this);
        if (getIntent().hasExtra("TODO_ID")) {
            todo = DBManager.getManager().getTodoByID(getIntent().getStringExtra("TODO_ID"));
            pos = getIntent().getIntExtra("POS", 0);
            ADD = false;
        }

        if (todo == null) {
            todo = new Todo(TimeUtil.defaultFormat(System.currentTimeMillis()), TodoDao.TYPE_SINGLE,
                    new SparseArray<ToDoItem>(), UserManager.getManager().getUser(), String.valueOf(System.currentTimeMillis()), TodoDao.FLAG_ON,0);
        }
        count = todo.getTodos().size();
        initView();
    }

    long curTime;
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (System.currentTimeMillis() - curTime > 3000 && info.getText().length() > 1) {
                KeyWordManager.getManager().searchRecom(info.getText().toString(), todoTips);
            }
            curTime = System.currentTimeMillis();

        }
    };


    private void initView() {
        EventBus.getDefault().register(this);
        curItem = todo.getTodos().get(0);
        setBestTimeText.setOnClickListener(this);
        setNeedTime.setOnClickListener(this);
        setDeadlineText.setOnClickListener(this);
        barLeft.setOnClickListener(this);
        barRight.setOnClickListener(this);
        setLocDes.setOnClickListener(this);
        seqText.setOnClickListener(this);
        seqText.setOnLongClickListener(this);
        adapter = new OrderTodoAdapter(this, todo.getTodos());
        adapter.setOnOrderTodoClick(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);


        if (curItem == null) {
            barRight.setText(" 完成 ");
            barCenter.setText("添加日程");
            impoSeekBar.setProgress(3);
            urgentSeekBar.setProgress(2);

        } else {
            barRight.setText("更新");
            barCenter.setText("日程详情");
            TodoManager.get().setTodoItemToView(curItem,info,setNeedTime,setBestTimeText,setDeadlineText,
                    setLocDes,remindCheck,impoSeekBar,urgentSeekBar,levelHigh,levelMid,levelLow);
        }
        impoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                importanceText.setTextColor(Color.rgb(25 * progress, 111 - (10 * progress), 0));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        urgentSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                urgentText.setTextColor(Color.rgb(25 * progress, 111 - (10 * progress), 50));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        

        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        info.addTextChangedListener(watcher);
        levelHigh.setOnClickListener(this);
        levelMid.setOnClickListener(this);
        levelLow.setOnClickListener(this);
        //初始化日期时间
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        h = calendar.get(Calendar.HOUR_OF_DAY);
        m = calendar.get(Calendar.MINUTE);
    }

    PopupWindow pop;

    private void popDatePicker(final TextView timeText, final boolean isDeadline) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialig_pick_time, null);
        View view1 = LayoutInflater.from(this).inflate(R.layout.date_picker, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.time_picker, null);
        final ViewPager pager = (ViewPager) view.findViewById(R.id.pickTimePager);
        List<View> views = new ArrayList<>();
        views.add(view1);
        views.add(view2);
        pager.setAdapter(new MyPagerAdapter(views));
        pop = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, true);
        final TextView right = (TextView) view.findViewById(R.id.right);
        TextView left = (TextView) view.findViewById(R.id.left);
        TextView bar = (TextView) view.findViewById(R.id.center);
        if (isDeadline)
            bar.setText("设置最晚完成的日期与时间");
        else
            bar.setText("设置最佳完成的日期与时间");
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() == 0)
                    pop.dismiss();
                else {
                    pager.setCurrentItem(0);
                    right.setBackgroundResource(R.mipmap.arrow_right);
                }
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() == 0) {
                    pager.setCurrentItem(1);
                    right.setBackgroundResource(R.mipmap.gou);
                } else {
                    sb.delete(0, sb.length());
                    if (isDeadline)
                        sb.append("最晚完成时间-> ");
                    else
                        sb.append("最佳完成时间-> ");
                    sb.append(year);
                    sb.append("年");
                    sb.append(month);
                    sb.append("月");
                    sb.append(day);
                    sb.append("日 ");
                    sb.append(h);
                    sb.append(":");
                    if (m < 10)
                        sb.append("0");
                    sb.append(m);
                    timeText.setText(sb.toString());
                    pop.dismiss();
                }
            }
        });
        DatePicker datePicker = (DatePicker) view1.findViewById(R.id.datePicker);
        TimePicker timePicker = (TimePicker) view2.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        datePicker.init(year, month - 1, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int y, int monthOfYear, int dayOfMonth) {
                year = y;
                month = monthOfYear + 1;
                day = dayOfMonth;
            }
        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                m = minute;
                h = hourOfDay;
            }
        });
        pop.showAsDropDown(findViewById(R.id.bar));
    }

    AlertDialog dialog;

    private void setNeedTimeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_need_time_picker, null);
        final NumberPicker day = (NumberPicker) view.findViewById(R.id.needTimeDay);
        final NumberPicker hour = (NumberPicker) view.findViewById(R.id.needTimeHours);
        final NumberPicker minute = (NumberPicker) view.findViewById(R.id.needTimeMinute);
        day.setMaxValue(365);
        hour.setMaxValue(24);
        minute.setMaxValue(60);
        TextView done = (TextView) view.findViewById(R.id.needTimeDone);
        hour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal == 24) {
                    hour.setValue(0);
                    day.setValue(day.getValue() + 1);
                }
            }
        });
        minute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal == 60) {
                    minute.setValue(0);
                    hour.setValue(hour.getValue() + 1);
                }
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.delete(0, sb.length());
                sb.append("估计时长-> ");
                sb.append(day.getValue());
                sb.append("天 ");
                sb.append(hour.getValue());
                sb.append("小时 ");
                sb.append(minute.getValue());
                sb.append("分");
                setNeedTime.setText(sb.toString());
                dialog.dismiss();
            }
        });
        builder.setView(view);
        dialog = builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.level_high:
            case R.id.level_low:
            case R.id.level_mid:
                setNeedTimeDialog();
                break;
            case R.id.bar_left:
                finish();
                break;
            case R.id.bar_right:
                done();
                break;
            case R.id.addTodo_setBestTime:
                popDatePicker(setBestTimeText, false);
                break;
            case R.id.addTodo_setDeadline:
                popDatePicker(setDeadlineText, true);
                break;
            case R.id.addTodo_setLoc:
                startActivity(new Intent(AddOrderTodoActivity.this, AddMapLocActivity.class));
                break;
            case R.id.seqText:
                AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.6f);
                alphaAnimation.setDuration(500);
                alphaAnimation.setInterpolator(new AccelerateInterpolator());
                alphaAnimation.start();
                ObjectAnimator.ofFloat(seqText, "scale", 1.0f, 1.3f, 1.0f, 0.8f, 1.0f).setDuration(500).start();

                if(!isAddTodo())
                {
                    return;
                }
                String needTime = setNeedTime.getText().toString();
                if (ADD) {
                        ToDoItem item = TodoManager.get().addTodoItem(todo,
                                info.getText().toString(),
                                setBestTimeText.getText().toString(),
                                setDeadlineText.getText().toString(),
                                needTime,
                                levelHigh.isChecked() ? Config.LEVEL_HIGH : (levelMid.isChecked() ? Config.LEVEL_NORMAL : Config.LEVEL_LOW),
                                loc,
                                impoSeekBar.getProgress(),
                                urgentSeekBar.getProgress(), 0, remindCheck.isChecked());
                        todo.addTodoItem(item);
                        adapter.add(item);
                    /*
                    保存当前日程内容的分词结果到数据库
                     */
                    for (String key : map.keySet()) {
                        DBManager.getManager().addKeyword(key, String.valueOf(TimeUtil.getLongNeedTime(needTime.substring(needTime.lastIndexOf(">") + 2))), impoSeekBar.getProgress(), urgentSeekBar.getProgress());
                    }
                    count++;
                    seqText.setText(String.valueOf(count));
                } else {
                        if(curItem!=null)
                        {
                            curItem = TodoManager.get().modefyTodoItem(curItem,
                                    info.getText().toString(),
                                    setBestTimeText.getText().toString(),
                                    setDeadlineText.getText().toString(),
                                    needTime,
                                    levelHigh.isChecked() ? Config.LEVEL_HIGH : (levelMid.isChecked() ? Config.LEVEL_NORMAL : Config.LEVEL_LOW),
                                    loc,
                                    impoSeekBar.getProgress(),
                                    urgentSeekBar.getProgress(), 0, remindCheck.isChecked());
                        }
                }

                clearInput();
                break;
        }
    }

    private void clearInput()
    {
        info.setText("");
        setBestTimeText.setText("最佳完成时间->");
        setDeadlineText.setText("截至完成时间->");
        setNeedTime.setText("估计时长-> ");
        loc = null;
        urgentSeekBar.setProgress(3);
        impoSeekBar.setProgress(2);
        setLocDes.setText("");
    }
    private void done() {
        if(count<=1 && !isAddTodo())
            return;
        if(ADD) {
            todo.setType(TodoDao.TYPE_ORDER);
            TodoManager.get().addOrderTodo(todo, pos);
        }else{
            TodoManager.get().updateTodo(todo);
            EventBus.getDefault().post(new ToDoEvent(todo,ToDoEvent.EVENT_ORDER_UPDATE,pos));
        }
        finish();
    }


    public void onEventMainThread(LocDes ld) {
        setLocDes.setText(ld.getDes());
        loc = ld;
    }

    public boolean isAddTodo()
    {
        if (info.getText().toString().equals("")) {
            Show("没有记录事件");
            return false;
        }
        if (setBestTimeText.getText().length() < 10 && setDeadlineText.getText().length() < 10) {
            Show("最佳,最晚完成时间至少需要设置一个 !!");
            return false;
        }
        if (setNeedTime.getText().toString().equals(getResources().getString(R.string.set_need_time))) {
            Show("请设置需要完成此事的预期时间");
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemClick(ToDoItem item, int index) {
        
        TodoManager.get().setTodoItemToView(item,info,setNeedTime,setBestTimeText,setDeadlineText,
                setLocDes,remindCheck,impoSeekBar,urgentSeekBar,levelHigh,levelMid,levelLow);
        curItem = item;
        drawer.closeDrawer(GravityCompat.START);
        seqText.setText(String.valueOf(index+1));
    }

    @Override
    public boolean onLongClick(View v) {
        clearInput();
        count++;
        seqText.setText(String.valueOf(count));
        return true;
    }
}
