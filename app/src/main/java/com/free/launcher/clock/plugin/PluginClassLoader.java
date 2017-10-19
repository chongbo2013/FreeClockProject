package com.free.launcher.clock.plugin;

/**
 * Created by xff on 2017/10/19.
 */


import dalvik.system.DexClassLoader;

/**
 * 避免加载重复的类，导致报错
 * Created by xff 2017-10-19 14:26:14
 */
public class PluginClassLoader extends DexClassLoader {

    public PluginClassLoader(String apkfile, String optimizedDirectory, String libraryPath, ClassLoader systemClassLoader) {
        super(apkfile, optimizedDirectory, libraryPath, systemClassLoader);
    }

    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        //如果vm已经加载了,返回该类,否则返回null
        Class<?> clazz = findLoadedClass(className);
        if (clazz == null) {
            //如果vm没有加载让该类的父加载器加载
            try {
                clazz = this.getParent().loadClass(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (clazz == null) {
                //当前加载器加载
                clazz = findClass(className);
            }
        }
        return clazz;
    }
}