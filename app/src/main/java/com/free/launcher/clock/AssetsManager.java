package com.free.launcher.clock;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by x002 on 2017/10/19.
 */

public class AssetsManager {
    public static final String TAG = "AssetsApkLoader";



    //文件结尾过滤
    public static final String FILE_FILTER = ".apk";


    /**
     * 将资源文件中的apk文件拷贝到私有目录中
     *
     * @param context
     */
    public static void copyAllAssetsApk(Context context,String sdcard) {

        AssetManager assetManager = context.getAssets();
        long startTime = System.currentTimeMillis();
        try {
            File dex = new File(sdcard);
            dex.mkdirs();
            String []fileNames = assetManager.list("");
            for(String fileName:fileNames){
                if(!fileName.endsWith(FILE_FILTER)){
                    continue;
                }
                InputStream in = null;
                OutputStream out = null;
                in = assetManager.open(fileName);
                File f = new File(dex, fileName);
                if (f.exists() && f.length() == in.available()) {
                    Log.i(TAG, fileName+"no change");
                    continue;
                }
                Log.i(TAG, fileName+" chaneged");
                out = new FileOutputStream(f);
                byte[] buffer = new byte[2048];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
                Log.i(TAG, fileName+" copy over");
            }
            Log.i(TAG,"###copyAssets time = "+(System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}