package dong.lan.flextime.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobGeoPoint;
import de.greenrobot.event.EventBus;
import dong.lan.flextime.BuildConfig;
import dong.lan.flextime.Config;
import dong.lan.flextime.R;
import dong.lan.flextime.adapter.MyPagerAdapter;
import dong.lan.flextime.bean.KeyWord;
import dong.lan.flextime.bean.LocDes;
import dong.lan.flextime.bean.ToDo;
import dong.lan.flextime.bean.ToDoEvent;
import dong.lan.flextime.bean.User;
import dong.lan.flextime.db.DBManager;
import dong.lan.flextime.utils.SortManager;
import dong.lan.flextime.utils.TimeUtil;
import dong.lan.flextime.utils.UserManager;

/**
 * Created by 梁桂栋 on 2015/12/11.
 */
public class AddTodoActivity extends BaseActivity implements View.OnClickListener {

    int year;
    int month;
    int day;
    int h;
    int m;
    StringBuilder sb = new StringBuilder();
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
    private ToDo toDo;
    private int pos;
    private boolean ADD = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        ButterKnife.bind(this);
        if (getIntent().hasExtra("TODO")) {
            toDo = (ToDo) getIntent().getSerializableExtra("TODO");
            pos = getIntent().getIntExtra("POS", 0);
            ADD = false;
        }
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
            if(System.currentTimeMillis()-curTime>5000 && info.getText().length()>1)
            {
                if (BuildConfig.DEBUG) Log.d("AddTodoActivity", "TEXT_CAHNGE");
                searchRecom();
            }
            curTime=System.currentTimeMillis();

        }
    };


    Map<String,String> map = new HashMap<>();
    private void searchRecom()
    {
        String text = info.getText().toString();
        BreakIterator iterator = BreakIterator.getWordInstance(Locale.CHINA);
        iterator.setText(text);
        int start = iterator.first();
        for(int end = iterator.next();end!=BreakIterator.DONE;start=end,end=iterator.next())
        {
            if(text.substring(start, end).length()>1){
                map.put(text.substring(start, end),"");
            }
        }
        if (BuildConfig.DEBUG) Log.d("AddTodoActivity", map.toString());
        todoTips.setVisibility(View.VISIBLE);
        if(!map.isEmpty()) {
            showRecommend(map);
        }

    }

    private void showRecommend(Map<String,String> map)
    {
        StringBuilder sb = new StringBuilder();
        for(String key : map.keySet())
        {
            KeyWord keyword =  DBManager.getManager().getKeyword(key);
            if(keyword!=null)
            {
                sb.append(keyword.getWord());
                sb.append("  平均时长： ");
                sb.append(TimeUtil.longToString(Long.parseLong(keyword.getTime()),"d天 H小时 m分"));
                sb.append("\n");
            }
        }
        todoTips.setText(sb.toString());
    }

    private void initView() {
        EventBus.getDefault().register(this);
        setBestTimeText.setOnClickListener(this);
        setNeedTime.setOnClickListener(this);
        setDeadlineText.setOnClickListener(this);
        barLeft.setOnClickListener(this);
        barRight.setOnClickListener(this);
        setLocDes.setOnClickListener(this);
        if (toDo == null) {
            barRight.setText(" 完成 ");
            barCenter.setText("添加日程");
            impoSeekBar.setProgress(3);
            urgentSeekBar.setProgress(2);
        } else {
            impoSeekBar.setProgress(toDo.getImportant());
            urgentSeekBar.setProgress(toDo.getUrgent());
            barRight.setText("更新");
            barCenter.setText("日程详情");
            sb.append("最佳完成时间-> ");
            if(toDo.getFinishTime().substring(0,4).equals("9999"))
                sb.append("没有设置");
            else
            sb.append(toDo.getFinishTime());
            setBestTimeText.setText(sb.toString());
            sb.delete(0, sb.length());
            sb.append("最晚完成时间-> ");
            if(toDo.getDeadline().substring(0,4).equals("9999"))
                sb.append("没有设置");
            else
            sb.append(toDo.getDeadline());
            setDeadlineText.setText(sb.toString());
            remindCheck.setChecked(toDo.getRemind().equals(1));
            info.setText(toDo.getInfo());
            setLocDes.setText(toDo.getLoc());
            sb.delete(0, sb.length());
            sb.append("所需时长-> ");
            sb.append(toDo.getNeedTime());
            setNeedTime.setText(sb.toString());
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

    private void done() {
        if (info.getText().toString().equals("")) {
            Show("没有记录事件");
            return;
        }
        if (setBestTimeText.getText().length() < 10 && setDeadlineText.getText().length() < 10) {
            Show("最佳,最晚完成时间至少需要设置一个 !!");
            return;
        }
        if (setNeedTime.getText().toString().equals(getResources().getString(R.string.set_need_time))) {
            Show("请设置需要完成此事的预期时间");
            return;
        }
        String endTime = setBestTimeText.getText().toString();
        String needTime = setNeedTime.getText().toString();
        String deadline = setDeadlineText.getText().toString();

        needTime = needTime.substring(needTime.lastIndexOf(">") + 2);

        if (endTime.length() > 10)
            endTime = endTime.substring(endTime.lastIndexOf(">") + 2);
        else
            endTime = "9999年12月31日 23:59";

        if (deadline.length() > 10)
            deadline = deadline.substring(deadline.lastIndexOf(">") + 2);
        else
            deadline = "9999年12月31日 23:59";

        if (toDo == null)
            toDo = new ToDo();
        toDo.setLastDoTime("");
        toDo.setPerContinueTime(30);
        toDo.setDoneOnTime(false);
        User u = UserManager.getManager(this).getUser();
        if(u==null)
            u=new User("Doo");
        toDo.setUser(u);
        toDo.setImportant(impoSeekBar.getProgress());
        toDo.setUrgent(urgentSeekBar.getProgress());
        if (levelHigh.isChecked())
            toDo.setStatus(Config.LEVEL_HIGH);
        else if (levelMid.isChecked())
            toDo.setStatus(Config.LEVEL_NORMAL);
        else
            toDo.setStatus(Config.LEVEL_LOW);
        if (loc != null) {
            toDo.setLoc(loc.getDes());
            toDo.setPoint(new BmobGeoPoint(loc.getLatLng().longitude, loc.getLatLng().latitude));
        }
        toDo.setLevel("1");
        toDo.setInfo(info.getText().toString());
        toDo.setRemind(remindCheck.isChecked() ? 1 : 0);
        toDo.setContinueDo(false);
        String bTime = String.valueOf(TimeUtil.getStartTime(needTime, endTime));
        String dTime = String.valueOf(TimeUtil.getStartTime(needTime, deadline));

        toDo.setStartTime(bTime.compareTo(dTime) >= 0 ? dTime : bTime);
        toDo.setWeight(SortManager.getSortWeight(impoSeekBar.getProgress(), urgentSeekBar.getProgress(), toDo.getStatus()));
        if (ADD) {
            toDo.setFinishTime(endTime);
            toDo.setDeadline(deadline);
            toDo.setNeedTime(needTime);
            String createTime = new SimpleDateFormat(TimeUtil.FORMAT_DATA_TIME_SECOND, Locale.CHINA).format(new Date());
            toDo.setCreateTime(createTime);
            DBManager.getManager().addAToDo(toDo);
            EventBus.getDefault().post(new ToDoEvent(toDo, 0, pos));


            for(String key : map.keySet())
            {
                   DBManager.getManager().addKeyword(key, String.valueOf(TimeUtil.getLongNeedTime(needTime)),impoSeekBar.getProgress(),urgentSeekBar.getProgress());
            }
        } else {
            if (!endTime.equals(toDo.getFinishTime()))
                toDo.setFinishTime(endTime);
            if (!deadline.equals(toDo.getDeadline()))
                toDo.setDeadline(deadline);
            if (!setNeedTime.getText().toString().equals(toDo.getNeedTime()))
                toDo.setNeedTime(needTime);
            DBManager.getManager().updateTodo(toDo);
            EventBus.getDefault().post(new ToDoEvent(toDo, 1, pos));
        }
        finish();
    }


    public void onEventMainThread(LocDes ld) {
        setLocDes.setText(ld.getDes());
        loc = ld;
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
                startActivity(new Intent(AddTodoActivity.this, AddMapLocActivity.class));
                break;
        }
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
