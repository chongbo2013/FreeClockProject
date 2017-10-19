package com.free.launcher.clock.plugin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.view.ContextThemeWrapper;

/**
 * 必须用ContextThemeWrapper 不然在LayoutInflater.from加载资源布局或者图片的时候，传入的Context会被舍弃掉，导致图片资源加载不了问题
 * Created by xff on 2017/10/19.
 */
public class PluginContext extends ContextThemeWrapper {

    private final ClassLoader mPluginClassLoader;
    private final Resources mPluginResources;

    public PluginContext(Context base, int themeres, ClassLoader cl, Resources r) {
        super(base, themeres);
        mPluginClassLoader = cl;
        mPluginResources = r;
    }

    @Override
    public ClassLoader getClassLoader() {
        if (mPluginClassLoader != null) {
            return mPluginClassLoader;
        }
        return super.getClassLoader();
    }

    @Override
    public Resources getResources() {
        if (mPluginResources != null) {
            return mPluginResources;
        }
        return super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        if (mPluginResources != null) {
            return mPluginResources.getAssets();
        }
        return super.getAssets();
    }
}
