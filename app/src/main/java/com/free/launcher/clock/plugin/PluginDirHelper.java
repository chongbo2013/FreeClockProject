package com.free.launcher.clock.plugin;

import android.content.Context;

import java.io.File;

/**
 * 插件的安装目录
 * Created by xff on 2017/10/19.
 */

public class PluginDirHelper {
    private static File sBaseDir = null;

    private static void init(Context context) {
        if (sBaseDir == null) {
            sBaseDir = new File(context.getCacheDir().getParentFile(), "Plugin");
            enforceDirExists(sBaseDir);
        }
    }

    public static String getPluginApkFile(Context context, String pluginInfoPackageName) {
        return new File(getPluginApkDir(context, pluginInfoPackageName), "base-1.apk").getPath();
    }

    public static String getPluginApkDir(Context context, String pluginInfoPackageName) {
        return enforceDirExists(new File(makePluginBaseDir(context, pluginInfoPackageName), "apk"));
    }

    public static String getPluginDir(Context context, String pluginInfoPackageName) {
        return enforceDirExists(new File(makePluginBaseDir(context, pluginInfoPackageName)));
    }

    private static String enforceDirExists(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath();
    }

    public static String makePluginBaseDir(Context context, String pluginInfoPackageName) {
        init(context);
        return enforceDirExists(new File(sBaseDir, pluginInfoPackageName));
    }
}
