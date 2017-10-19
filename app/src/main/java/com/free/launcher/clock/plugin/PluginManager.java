package com.free.launcher.clock.plugin;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.RemoteException;
import android.util.DisplayMetrics;
import java.io.File;
import java.lang.reflect.Method;
/**
 * Created by xff on 2017/10/19.
 */

public class PluginManager {
    AssetManager pluginAssetManager;
    Resources pluginResource;
    ClassLoader classloader;
    Context pluginBaseContextWrapper;
    public ClassLoader getClassloader(){
        return classloader;
    }

    public Context getPluginBaseContextWrapper(){
        return pluginBaseContextWrapper;
    }
    public boolean installPackage(Context mContext,String filepath) throws RemoteException{
        String apkfile = null;
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(filepath, 0);
            if (info == null) {
                return false;
            }
            //清理插件
            Utils.deleteDir( PluginDirHelper.getPluginDir(mContext, info.packageName));///data/data/com.free.launcher.clock/Plugin/xu.ferris.plugintest
            //获取插件的路径名/data/data/com.free.launcher.clock/Plugin/xu.ferris.plugintest/apk/base-1.apk
            apkfile = PluginDirHelper.getPluginApkFile(mContext, info.packageName);
            Utils.copyFile(filepath, apkfile);//把插件拷贝到内置存储中
            String cachePath = PluginDirHelper.getPluginApkDir(mContext, info.packageName);///data/data/com.free.launcher.clock/Plugin/xu.ferris.plugintest/apk
             classloader = new PluginClassLoader(apkfile, cachePath, cachePath,mContext.getClassLoader().getParent());
            if(classloader!=null){
                pluginAssetManager=getPluginAssetManager( new File(apkfile));
                pluginResource=getPluginResources(pluginAssetManager,mContext.getResources().getDisplayMetrics(),mContext.getResources().getConfiguration());
                pluginBaseContextWrapper=new PluginContext(mContext, android.R.style.Theme,classloader,pluginResource);
                return true;
            }
        }catch (Exception e) {
            if (apkfile != null) {
                new File(apkfile).delete();
            }
            handleException(e);
            return false;
        }
        return false;
    }

    public static AssetManager getPluginAssetManager(File apk) throws Exception {
        // 字节码文件对象
        Class c = AssetManager.class;
        AssetManager assetManager=(AssetManager) c.newInstance();
        // 获取addAssetPath方法对象
        Method method = c.getDeclaredMethod("addAssetPath", String.class);
        method.invoke(assetManager, apk.getAbsolutePath());
        return assetManager;
    }
    public static Resources getPluginResources(AssetManager assets,
                                               DisplayMetrics metrics, Configuration config) {
        Resources resources = new Resources(assets, metrics, config);
        return resources;
    }
    private void handleException(Exception e) throws RemoteException {
        RemoteException remoteException;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            remoteException = new RemoteException(e.getMessage());
            remoteException.initCause(e);
            remoteException.setStackTrace(e.getStackTrace());
        } else {
            remoteException = new RemoteException();
            remoteException.initCause(e);
            remoteException.setStackTrace(e.getStackTrace());
        }
        throw remoteException;
    }

    public Resources getResource() {
        return pluginResource;
    }
}
