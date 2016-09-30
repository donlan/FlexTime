package dong.lan.flextime.view.constom;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import java.util.Date;

import dong.lan.flextime.BuildConfig;
import dong.lan.flextime.R;
import dong.lan.flextime.utils.TimeUtil;

/**
 * Created by 梁桂栋 on 2016年09月02日 21:24.
 * Email:760625325@qq.com
 * GitHub: https://gitbub.com/donlan
 * description:
 */
public class DateTimePicker extends AlertDialog {

    ViewFlipper switcher;
    View view;
    DatePicker datePicker;
    TimePicker timePicker;
    Date date;
    RadioGroup dateCheck;

    CallBack callBack;

    int index = 0;


    public DateTimePicker(Context context, CallBack callBackListener) {
        super(context);
        this.callBack = callBackListener;
        date = new Date(System.currentTimeMillis());
        view = LayoutInflater.from(context).inflate(R.layout.date_time_picker, null);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        datePicker.init(date.getYear() + 1900, date.getMonth(), date.getDate(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                date.setYear(i-1900);
                date.setMonth(i1);
                date.setDate(i2);
            }
        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                date.setHours(i);
                date.setMinutes(i1);
            }
        });
        TextView back = (TextView) view.findViewById(R.id.left);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateTimePicker.this.dismiss();
            }
        });
        TextView ok = (TextView) view.findViewById(R.id.right);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTime(callBack);
                dismiss();
            }
        });
        switcher = (ViewFlipper) view.findViewById(R.id.switcher);
        dateCheck = (RadioGroup) view.findViewById(R.id.dateTimeCheck);
        dateCheck.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if (i == R.id.dateCheck && index == 1) {
                            switcher.showPrevious();
                            index = 0;
                        } else if (i == R.id.timeCheck && index == 0) {
                            switcher.showNext();
                            index = 1;
                        }
                    }
                }
        );

        setView(view);
    }



    public DateTimePicker getTime(CallBack callBack) {
        callBack.onClose(date.getTime());
        return this;
    }
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        view = null;
        datePicker = null;
        timePicker = null;
        switcher = null;
        date = null;
        callBack =null;
        if (BuildConfig.DEBUG) Log.d("DateTimePicker", "onDetachedFromWindow");
    }
    public interface CallBack{
        void onClose(long time);
    }
}
