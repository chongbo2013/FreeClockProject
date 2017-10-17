package com.free.launcher.clock;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * openweathermaphelper实现天气时钟
 * 2017-10-17  ferris.xu
 */
public class MainActivity extends Activity {
    ClockView mClockView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_clock_view);
        mClockView=(ClockView) findViewById(R.id.mClockView);
        mClockView.setClockListem(new ClockProView.IClockListem() {
            @Override
            public void clickTime() {
                Toast.makeText(MainActivity.this,"clickTime",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void clickDate() {
                Toast.makeText(MainActivity.this,"clickDate",Toast.LENGTH_SHORT).show();
            }
        });
    }


}
