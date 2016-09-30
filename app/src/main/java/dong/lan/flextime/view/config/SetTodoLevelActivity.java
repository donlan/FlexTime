package dong.lan.flextime.view.config;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import dong.lan.flextime.R;
import dong.lan.flextime.utils.SP;
import dong.lan.flextime.view.BaseActivity;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 2016/2/10  15:57.
 *
 * 日程的两级分布队列容量的设置
 *
 */
public class SetTodoLevelActivity extends BaseActivity {

    @Bind(R.id.todo_level_seekBar1)
    SeekBar seekBar1;
    @Bind(R.id.todo_level_seekBar2)
    SeekBar seekBar2;
    @Bind(R.id.todo_level_text1)
    TextView textView1;
    @Bind(R.id.todo_level_text2)
    TextView textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpView(R.layout.activity_set_todo_level);

        ButterKnife.bind(this);

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView1.setText(getResources().getString(R.string.todo_level1_text) + progress);
                SP.setLevelFirst(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView2.setText(getResources().getString(R.string.todo_level2_text) + progress);
                SP.setLevelSecond(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar1.setProgress(SP.getLevelFirst());
        seekBar2.setProgress(SP.getLevelSecond());
        initBar();
    }

    private void initBar() {
        findViewById(R.id.left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.text)).setText("日程梯度设置");
        findViewById(R.id.right).setVisibility(View.GONE);
    }

}
