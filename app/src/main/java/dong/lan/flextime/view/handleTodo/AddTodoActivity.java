package dong.lan.flextime.view.handleTodo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import dong.lan.flextime.Config;
import dong.lan.flextime.R;
import dong.lan.flextime.model.handleTodo.AddTodoModel;
import dong.lan.flextime.presenter.handleTodo.AddTodoPresenter;
import dong.lan.flextime.view.map.AddMapLocActivity;
import dong.lan.flextime.event.LocEvent;
import dong.lan.flextime.bean.RealmToDoItem;
import dong.lan.flextime.bean.Todo;
import dong.lan.flextime.utils.TodoManager;
import dong.lan.flextime.view.BaseActivity;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 2015/12/11  14:52.
 */
public class AddTodoActivity extends BaseActivity implements View.OnClickListener, IAddTodo {


    @Bind(R.id.addTodo_setBestTime)
    TextView setBestTimeText;
    @Bind(R.id.addTodo_setDeadline)
    TextView setDeadlineText;
    @Bind(R.id.addTodo_setNeedTime)
    TextView setNeedTime;
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
    @Bind(R.id.addTodo_tips)
    TextView todoTips;

    @Bind(R.id.addTodo_remindCheck)
    CheckBox remindCheck;

    @Bind(R.id.addTodo_info)
    EditText info;

    @Bind(R.id.addTodo_Importance_seekBar)
    SeekBar impoSeekBar;
    @Bind(R.id.addTodo_Urgent_seekBar)
    SeekBar urgentSeekBar;

    @Bind(R.id.level_mid)
    RadioButton levelMid;
    @Bind(R.id.level_high)
    RadioButton levelHigh;
    @Bind(R.id.level_low)
    RadioButton levelLow;

    long needTime;
    Date bestTime;
    Date deadTime;
    private LocEvent loc;
    private int pos;
    private boolean ADD = true;   //判断是添加日程，还是更新日程信息

    private AddTodoPresenter presenter;
    private Todo todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpView(R.layout.activity_add_todo);
        ButterKnife.bind(this);
        presenter = new AddTodoPresenter(this, new AddTodoModel(this));
        todo = presenter.getTodo(getIntent().getStringExtra("TODO_ID"));
        if (getIntent().hasExtra("TODO_ID")) {
            pos = getIntent().getIntExtra("POS", 0);
            ADD = false;
        }
        initView();
    }

    long curTime;

    /*
    监听日程内容的输入，获取筛选到的关键词的历史平均用时
     */
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
                presenter.getKeyWordRecommand(info.getText().toString(), todoTips);
            }
            curTime = System.currentTimeMillis();
        }
    };


    private void initView() {

        setBestTimeText.setOnClickListener(this);
        setNeedTime.setOnClickListener(this);
        setDeadlineText.setOnClickListener(this);
        barLeft.setOnClickListener(this);
        barRight.setOnClickListener(this);
        setLocDes.setOnClickListener(this);
        if (ADD) {
            barRight.setText(" 完成 ");
            barCenter.setText("添加日程");
            impoSeekBar.setProgress(3);
            urgentSeekBar.setProgress(2);
        } else {
            setTodoToView(todo);
        }

        info.addTextChangedListener(watcher);
        levelHigh.setOnClickListener(this);
        levelMid.setOnClickListener(this);
        levelLow.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.level_high:
            case R.id.level_low:
            case R.id.level_mid:
                presenter.setNeedTime();
                break;
            case R.id.bar_left:
                finish();
                break;
            case R.id.bar_right:
                presenter.done(ADD);
                break;
            case R.id.addTodo_setBestTime:
                presenter.setFinishTime();
                break;
            case R.id.addTodo_setDeadline:
                presenter.setDeadTime();
                break;
            case R.id.addTodo_setLoc:
                startActivity(new Intent(AddTodoActivity.this, AddMapLocActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void setTodoToView(Todo todo) {
        RealmToDoItem curItem = todo.items.get(0);
        impoSeekBar.setProgress(curItem.important);
        urgentSeekBar.setProgress(curItem.urgent);
        barRight.setText("更新");
        barCenter.setText("日程详情");
        TodoManager.get().setTodoItemToView(curItem, info, setNeedTime, setBestTimeText, setDeadlineText,
                setLocDes, remindCheck, impoSeekBar, urgentSeekBar, levelHigh, levelMid, levelLow);
    }

    @Override
    public void setNeedTime(String text) {
        setNeedTime.setText(text);
    }

    @Override
    public void setDeadTime(String text) {
        setDeadlineText.setText(text);
    }

    @Override
    public void setFinishTime(String text) {
        setBestTimeText.setText(text);
    }

    @Override
    public String getInfo() {
        return info.getText().toString();
    }

    @Override
    public int getStatus() {
        return levelHigh.isChecked() ? Config.LEVEL_HIGH : (levelMid.isChecked() ? Config.LEVEL_NORMAL : Config.LEVEL_LOW);
    }

    @Override
    public int getImportant() {
        return impoSeekBar.getProgress();
    }

    @Override
    public int getUrgent() {
        return urgentSeekBar.getProgress();
    }

    @Override
    public int getRemind() {
        return remindCheck.isChecked() ? 1 : 0;
    }
}
