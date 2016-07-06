package com.liujc.droid.myapplication;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * 类名称：${primary_type_name}
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/5/25 11:19
 * 描述：TODO
 * 最近修改时间：2016/5/25 11:19
 * 修改人：Modify by liujc
 */
public class CommonUtil {
    public static String getSDPath() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            return Environment.getExternalStorageDirectory().toString();
        } else {
            return "";
        }
    }
    public static String getFilePath() {
        String sdCardPath = getSDPath();
        if (TextUtils.isEmpty(sdCardPath)) {
            return "";
        } else {
            return sdCardPath + File.separator + "szass"
                    + File.separator + "log";
        }
    }
}
