package com.free.launcher.clock.weather;

import android.content.Context;
import android.os.Handler;
import android.view.View;


import com.free.launcher.clock.R;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;

import java.util.Locale;



public class WeatherHelper implements OpenWeatherMapHelper.CurrentWeatherCallback, Runnable {
    private static final String KEY_UNITS = "pref_weather_units";
    private static final String KEY_CITY = "pref_weather_city";
    private static final int DELAY = 30 * 3600 * 1000;
    private boolean mIsImperial;
    private String mUnits;
    private String mCity;
    private String mTemp;
    private OpenWeatherMapHelper mHelper;
    private Handler mHandler;
    private String mIcon;
    private View clockView;
    private WeatherIconProvider iconProvider;
    private boolean stopped = false;

    public WeatherHelper(View clockView, Context context) {
        this.clockView = clockView;
        iconProvider = new WeatherIconProvider(context);
        mHandler = new Handler();
        mHelper = new OpenWeatherMapHelper();
        mHelper.setApiKey(context.getResources().getString(R.string.OPEN_WEATHER_MAP_API_KEY));
        setCity("beijing");
        setUnits( "metric");
    }

    private void refresh() {
        if (!stopped) {
            mHelper.getCurrentWeatherByCityName(mCity, this);
            mHandler.postDelayed(this, DELAY);
        }
    }

    private String makeTemperatureString(String string) {
        return String.format(mIsImperial ? "%s°F" : "%s°C", string);
    }

    @Override
    public void onSuccess(CurrentWeather currentWeather) {
        mTemp = String.format(Locale.US, "%.0f", currentWeather.getMain().getTemp());
        mIcon = currentWeather.getWeatherArray().get(0).getIcon();
        update();
    }

    @Override
    public void onFailure(Throwable throwable) {
        mTemp = (mTemp != null && !mTemp.equals("ERROR")) ? mTemp : "?";
        mIcon = "-1";
        update();
    }

    private void update() {
//        if(clockView!=null)
//        clockView.update(makeTemperatureString(mTemp),iconProvider.getIcon(mIcon));
    }


    private void setCity(String city) {
        mCity = city;
    }

    private void setUnits(String units) {
        mUnits = units;
        mIsImperial = units.equals("imperial");
        mHelper.setUnits(units);
    }
    @Override
    public void run() {
        refresh();
    }

    public void stop() {
        stopped = true;
        mHandler.removeCallbacks(this);
    }

    public void start() {
        stopped = false;
        refresh();
    }
}
