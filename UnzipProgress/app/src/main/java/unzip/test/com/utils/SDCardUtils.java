package unzip.test.com.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;



import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import unzip.test.com.application.MyApp;

/**
 * Created by huagnwenjun on 2017/12/5
 *  SD卡相关的辅助类
 */

public class SDCardUtils {
    private SDCardUtils()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    // is_removable为false时得到的是内置sd卡路径，为true则为外置sd卡路径。
    //写入外置sd卡需要系统权限 打包签名
    public static String getStoragePath( boolean is_removale) {

        StorageManager mStorageManager = (StorageManager) MyApp.getInstance().getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断内置SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable()
    {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }


    /**
     * 获取内置SD卡路径
     *
     * @return
     */
    public static String getSDCardPath()
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return true 外置sd剩余容量      false内置sd剩余容量
     */
    public static long getSDCardAllSize(boolean isExSd)
    {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getStoragePath(isExSd));
            // 获取空闲快的数量
            long availableBlocks ;
            //获取单个数据块的大小（byte）
            long freeBlocks;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
                freeBlocks=stat.getBlockSizeLong();
                availableBlocks=stat.getAvailableBlocksLong();
            }else {
                freeBlocks=stat.getBlockSize();
                availableBlocks=stat.getAvailableBlocks();
            }

            return freeBlocks * availableBlocks;
        }
        return 0;
    }



    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath()
    {
        return Environment.getRootDirectory().getAbsolutePath();
    }


}


