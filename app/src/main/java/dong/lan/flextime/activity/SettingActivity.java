package dong.lan.flextime.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import dong.lan.flextime.BuildConfig;
import dong.lan.flextime.R;
import dong.lan.flextime.utils.SP;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 2016/2/3  14:06.
 */
public class SettingActivity extends BaseActivity {

    @Bind(R.id.setting_alert_sound)
    LinearLayout settingSound;
    @Bind(R.id.setting_alert_set_delay)
    LinearLayout setAlertDelay;
    @Bind(R.id.setting_set_todo_level)
    LinearLayout setTodoLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initBar();
        initClick();
    }

    private void initClick() {
        settingSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "选择提示声音"), 1);
                } catch (android.content.ActivityNotFoundException e) {
                    Show("请安装文件管理器（File Manager）");
                }
            }
        });
        setAlertDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDelayDialog();
            }
        });

        setTodoLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this,SetTodoLevelActivity.class));
            }
        });

    }

    private void showAlertDelayDialog()
    {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_set_alert_delay,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialogStyleTop);
        final TextView delayText = (TextView) view.findViewById(R.id.dialog_set_delay_timeText);
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.dialog_set_delay_seekBar);
        delayText.setText("日程提前 "+SP.getAlertDelay()+" 分提醒");
        seekBar.setProgress(SP.getAlertDelay());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SP.setAlertDelay(progress);
                delayText.setText("日程提前 " + progress + " 分提醒");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        builder.setView(view);
        builder.show();
    }
    private void initBar() {
        findViewById(R.id.left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.text)).setText("设置");
        findViewById(R.id.right).setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK)
        {
            Uri uri = data.getData();
            SP.writeSoundPath(uri.getPath());
            if (BuildConfig.DEBUG) Log.d("SettingActivity", Uri.decode(uri.getPath()));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
