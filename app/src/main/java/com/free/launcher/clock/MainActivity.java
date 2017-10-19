package com.free.launcher.clock;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.free.launcher.clock.plugin.PluginManager;

import java.lang.reflect.Constructor;

/**
 * 插件化实现 加载View
 * openweathermaphelper实现天气时钟
 * 2017-10-17  ferris.xu
 */
public class MainActivity extends Activity {
   FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout=findViewById(R.id.clockViewGroup);
        AssetsManager.copyAllAssetsApk(this, Environment.getExternalStorageDirectory().getAbsolutePath());
        PluginManager pluginManager=new PluginManager();
        try {
            boolean isSuccess=pluginManager.installPackage(this,Environment.getExternalStorageDirectory().getAbsolutePath()+"/plugintest-debug.apk");
            if(isSuccess){
                ClassLoader classLoader=pluginManager.getClassloader();
                try {
                    Class<?> mLoadClass = classLoader.loadClass("xu.ferris.plugintest.ClockViewGroup");
                    if(mLoadClass!=null){
                        Constructor constructor = mLoadClass.getConstructor(Context.class);
                        Object  obj =  constructor.newInstance(pluginManager.getPluginBaseContextWrapper());
                        if(obj!=null){
                            Toast.makeText(this,"加载成功",Toast.LENGTH_SHORT).show();
                            frameLayout.addView((ViewGroup)obj,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        }
                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this,"加载成功",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"加载失败",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
