package dong.lan.flextime.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.SeekBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import dong.lan.flextime.R;
import dong.lan.flextime.utils.SP;
import dong.lan.flextime.utils.SortManager;

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
        EventBus.getDefault().post("1");
        finish();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_define_factor);
        ButterKnife.bind(this);
        defineImportance.setProgress((int) SP.getImp());
        defineUrgent.setProgress((int) SP.getUrg());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
