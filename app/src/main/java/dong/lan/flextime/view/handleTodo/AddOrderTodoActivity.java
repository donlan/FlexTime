package dong.lan.flextime.view.handleTodo;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;


import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import dong.lan.flextime.Config;
import dong.lan.flextime.Interface.OrderTodoItemClick;
import dong.lan.flextime.R;
import dong.lan.flextime.model.handleTodo.AddTodoModel;
import dong.lan.flextime.presenter.handleTodo.OrderTodoPresenter;
import dong.lan.flextime.view.map.AddMapLocActivity;
import dong.lan.flextime.adapter.OrderTodoAdapter;
import dong.lan.flextime.bean.RealmToDoItem;
import dong.lan.flextime.bean.Todo;
import dong.lan.flextime.utils.TodoManager;
import dong.lan.flextime.view.BaseActivity;
import io.realm.Realm;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 3/13/2016  09:51.
 */
public class AddOrderTodoActivity extends BaseActivity implements IOrderTodo, View.OnClickListener, OrderTodoItemClick, View.OnLongClickListener {

    int count = 1;
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

    long needTime;
    Date bestTime;
    Date deadTime;

    private boolean ADD = true;
    private Todo todo;
    private OrderTodoAdapter adapter;
    private RealmToDoItem curItem;
    private Realm realm;


    private OrderTodoPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpView(R.layout.activity_add_order_todo);
        ButterKnife.bind(this);
        presenter = new OrderTodoPresenter(this, new AddTodoModel(this));
        todo = presenter.getTodo(getIntent().getStringExtra("TODO_ID"));
        if (getIntent().hasExtra("TODO_ID")) {
            int pos = getIntent().getIntExtra("POS", 0);
            ADD = false;
        }
        count = todo.items == null ? 0 : todo.items.size();
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
        seqText.setOnClickListener(this);
        seqText.setOnLongClickListener(this);
        adapter = new OrderTodoAdapter(this, todo.items);
        adapter.setOnOrderTodoClick(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        if (ADD) {
            barRight.setText(" 完成 ");
            barCenter.setText("添加日程");
            impoSeekBar.setProgress(3);
            urgentSeekBar.setProgress(2);

        } else {
            barRight.setText("更新");
            barCenter.setText("日程详情");
            curItem = todo.items.get(0);
            setTodoToView(todo);
        }

        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                adapter.notifyDataSetChanged();
            }
        });

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
                startActivity(new Intent(AddOrderTodoActivity.this, AddMapLocActivity.class));
                break;
            case R.id.seqText:
                if (ADD) {
                    presenter.addOne();
                } else {
                    if (curItem != null && count <= todo.items.size()) {
                        presenter.updateOne(curItem);
                    } else if (count == todo.items.size() + 1) {
                        presenter.addOne();
                    } else {
                        throw new IllegalStateException("Count is:" + count + " but size is:" + todo.items.size());
                    }
                }
                presenter.saveAndTipKeyword(info.getText().toString(), todoTips);
                break;
        }
    }

    private void clearInput() {
        info.setText("");
        setBestTimeText.setText("最佳完成时间:");
        setDeadlineText.setText("截至完成时间:");
        setNeedTime.setText("估计时长: ");
        urgentSeekBar.setProgress(3);
        impoSeekBar.setProgress(2);
        setLocDes.setText("");
        presenter.setLongTime(null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }

    @Override
    public void onItemClick(RealmToDoItem item, int index) {
        curItem = item;
        TodoManager.getTodoManager().setTodoItemToView(curItem, info, setNeedTime, setBestTimeText, setDeadlineText,
                setLocDes, remindCheck, impoSeekBar, urgentSeekBar, levelHigh, levelMid, levelLow);
        drawer.closeDrawer(GravityCompat.START);
        presenter.setLongTime(curItem);
        updateCountView(index + 1);
    }

    @Override
    public boolean onLongClick(View v) {
        clearInput();
        count++;
        seqText.setText(String.valueOf(count));
        if (count <= todo.items.size()) {
            curItem = todo.items.get(count - 1);
            TodoManager.get().setTodoItemToView(curItem, info, setNeedTime, setBestTimeText, setDeadlineText,
                    setLocDes, remindCheck, impoSeekBar, urgentSeekBar, levelHigh, levelMid, levelLow);
        }
        return true;
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

    @Override
    public void updateCountView(int count) {
        this.count = count;
        seqText.setText(String.valueOf(count));
    }

    @Override
    public int getCount() {
        return count;
    }

}
