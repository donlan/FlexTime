package dong.lan.flextime.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dong.lan.flextime.R;
import dong.lan.flextime.bean.KeyWord;
import dong.lan.flextime.utils.TimeUtil;
import dong.lan.flextime.view.BaseActivity;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * 项目：FlexTime
 * 作者：梁桂栋
 * 日期： 3/13/2016  09:56.
 */
public class UserCenterActivity extends BaseActivity {
    @OnClick(R.id.left)
    public void close()
    {
        finish();
    }
    @Bind(R.id.text)
    TextView tittle;
    @Bind(R.id.right)
    TextView right;
    @Bind(R.id.user_status_text)
    TextView statusText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setUpView(R.layout.activity_usercenter);
        ButterKnife.bind(this);
        inti();
    }

    private void inti() {
        tittle.setText("关键词");
        right.setVisibility(View.GONE);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<KeyWord> keyWords = realm.where(KeyWord.class).findAll();
        if(keyWords.size()==0)
        {
            statusText.setText("本地没有你的统计数据");
        }else
        {
            StringBuilder sb = new StringBuilder();
            sb.append("<html><body><h1 align='center'>");
            sb.append("你的本地关键词统计");
            sb.append("</h1>");
            int index = 1;
            for(KeyWord keyWord : keyWords)
            {
                sb.append("<h2>");
                sb.append(index);
                sb.append(" . ");
                sb.append(keyWord.getWord());
                sb.append("</h2>");

                sb.append("<h4>平均用时：");
                sb.append(TimeUtil.defaultNeedFormat(keyWord.getTime()));
                sb.append(" </h4><h5>重要性：");
                sb.append(keyWord.getImp());
                sb.append(" 紧急性：");
                sb.append(keyWord.getUrg());
                sb.append("</h5>");
                index++;
            }
            sb.append("</body></html>");
            statusText.setText(Html.fromHtml(sb.toString()));
        }
    }
}
