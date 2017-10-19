package xu.ferris.plugintest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.format.DateFormat;
import android.util.AttributeSet;

import java.util.Calendar;
import java.util.Locale;

/**
 * TODO: ferris 处理时钟逻辑
 */
public class ClockView extends ClockProView {
    private String[] months ;
    private String[] weeks ;

    public ClockView(Context context) {
        super(context);
        init();
    }

    public ClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init(){
        super.init();
        setClickable(true);

         months = getContext().getResources().getStringArray(R.array.mounth);
        weeks = getContext().getResources().getStringArray(R.array.week);
        timeChage();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        getContext().registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        getContext().unregisterReceiver(broadcastReceiver);
    }

    //广播的注册，其中Intent.ACTION_TIME_CHANGED代表时间设置变化的时候会发出该广播


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.ACTION_TIME_TICK.equals(intent.getAction())){
                timeChage();//每一分钟更新时间
            }else if(intent.ACTION_TIME_CHANGED.equals(intent.getAction())){
                timeChage();//每一分钟更新时间
            }
        }
    };

    @Override
    protected String getTime() {
        StringBuilder builder = new StringBuilder();
        // 时间
        boolean format24 = DateFormat.is24HourFormat(getContext());
        Calendar c = Calendar.getInstance();
        int hour12 = c.get(Calendar.HOUR);
        int hour24 = c.get(Calendar.HOUR_OF_DAY);

        if (format24) {
            String hour24String= hour24<10?"0"+hour24:hour24+"";
            builder.append(hour24String);
        } else {
            hour12 = hour12 == 0 ? 12 : hour12;
            String hour12String= hour12<10?"0"+hour12:hour12+"";
            builder.append(hour12String);
        }
        builder.append(":");
        String minute = c.get(Calendar.MINUTE)<10?"0"+c.get(Calendar.MINUTE):c.get(Calendar.MINUTE)+"";

        builder.append(minute);
        return builder.toString();
    }
    @Override
    protected String getDate() {
        Calendar c = Calendar.getInstance();
        int week = c.get(Calendar.DAY_OF_WEEK); // 1-7
        int month = c.get(Calendar.MONTH) + 1;  // 1-12
        int day = c.get(Calendar.DAY_OF_MONTH); // 1-30
        return getDate(getMonth(month), getDay(day), getWeek(week));
    }

    private String getDate(String mMonth, String mDay, String mWeek) {
        StringBuilder builder = new StringBuilder();
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (!language.equals("zh")) {
            builder.append(mMonth)
                    .append(mDay)
                    .append(" ")
                    .append(mWeek);
        } else {
            builder.append(mWeek)
                    .append(" ")
                    .append(mMonth)
                    .append(mDay)
                    .append("日");
        }
        return builder.toString();
    }

    public String getMonth(int month) {
        if (month < 1 || month > 12) {
            return "";
        }

        if (months != null && months.length > month) {
            return months[month];
        }

        return "";
    }

    public String getWeek(int week) {
        if (week < 1 || week > 7) {
            return "";
        }

        if (weeks != null && weeks.length > week) {
            return weeks[week];
        }
        return "";
    }

    public String getDay(int day) {
        if (day < 1 || day > 31) {
            return "";
        }
        return String.valueOf(day);
    }

}