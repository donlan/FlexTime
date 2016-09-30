package dong.lan.flextime.view.config;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.SeekBar;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dong.lan.flextime.R;
import dong.lan.flextime.utils.SP;
import dong.lan.flextime.utils.SortManager;
import dong.lan.flextime.view.BaseActivity;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 3/13/2016  00:29.
 *
 * 自定义权重因子
 *
 */
public class DefineFactorActivity extends BaseActivity {

    @Bind(R.id.defineImportance)
    SeekBar defineImportance;
    @Bind(R.id.defineUrgent)
    SeekBar defineUrgent;
    @OnClick(R.id.defineDone)
    public void done()
    {
        SortManager.IMP = defineImportance.getProgress() * 0.8;
        SortManager.URG = defineUrgent.getProgress() * 0.8;
        SP.setImp((float) SortManager.IMP);
        SP.setUrg((float) SortManager.URG);
        finish();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpView(R.layout.activity_define_factor);
        ButterKnife.bind(this);
        defineImportance.setProgress((int) SP.getImp());
        defineUrgent.setProgress((int) SP.getUrg());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
