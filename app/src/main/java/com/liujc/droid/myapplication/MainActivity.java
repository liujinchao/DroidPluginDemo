package com.liujc.droid.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.morgoo.droidplugin.pm.PluginManager;

import java.io.File;
import java.util.ArrayList;

/**
 * 类名称：${primary_type_name}
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/6/7 16:54
 * 描述：TODO
 * 最近修改时间：2016/6/7 16:54
 * 修改人：Modify by liujc
 */
public class MainActivity extends Activity {

    private String path = PluginConsts.path;	//应用程序用到的APK的文件目录
    private String APKnameB = PluginConsts.APKnameB;	//APK文件目录下的APK名
    private String APKnameC = PluginConsts.APKnameC;	//APK文件目录下的APK名
    Button mBGoto; // 跳转插件的按钮
    Button install; //安装插件的按钮
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra(PluginConsts.MASTER_EXTRA_STRING) != null) {
            String words = "say: " + intent.getStringExtra(PluginConsts.MASTER_EXTRA_STRING);
            Toast.makeText(this, words, Toast.LENGTH_SHORT).show();
        }

    }

    private void initView() {
//        getApkFromDownload();
        mBGoto = (Button) findViewById(R.id.main_b_goto);
        mBGoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPlugin(v);
            }
        });
        install = (Button) findViewById(R.id.btn_install);
        install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                int tag =     PluginManager.getInstance().installPackage(path + APKnameC,0);
                    Log.d("TAG",""+tag);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Log.d("TAG", "" + e.getLocalizedMessage().toString());
                }
            }
        });

    }
    // 跳转控件
    private void gotoPlugin(View view) {
        if (isActionAvailable(view.getContext(), PluginConsts.PLUGIN_ACTION_MAIN)) {
//            Intent intent = new Intent(PluginConsts.PLUGIN_ACTION_MAIN);
//            intent.putExtra(PluginConsts.PLUGIN_EXTRA_STRING, "Hello, My Plugin!");
//            startActivity(intent);

            PackageManager pm = getPackageManager();
            String dexPath_plugin = path + APKnameC;
            PackageInfo packageInfo = getPackageInfo(MainActivity.this, dexPath_plugin);
            Intent intent = pm.getLaunchIntentForPackage(packageInfo.packageName);
            intent.putExtra(PluginConsts.PLUGIN_EXTRA_STRING, "Hello, My Plugin!");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(intent);
        } else {
            Toast.makeText(view.getContext(), "跳转失败", Toast.LENGTH_SHORT).show();
        }
    }

    // Action是否允许
    public static boolean isActionAvailable(Context context, String action) {
        Intent intent = new Intent(action);
        return context.getPackageManager().resolveActivity(intent, 0) != null;
    }
    public static PackageInfo getPackageInfo(Context context, String apkFilepath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = null;

        try {
            pkgInfo = pm.getPackageArchiveInfo(apkFilepath, 5);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return pkgInfo;
    }
    ArrayList<ApkItem> apkItems;
    private ArrayList<ApkItem> getApkFromDownload() {
//        File files = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File files = Environment.getExternalStoragePublicDirectory(path);
        PackageManager pm = getPackageManager();
        apkItems = new ArrayList<>();
        for (File file : files.listFiles()) {
            if (file.exists() && file.getPath().toLowerCase().endsWith(".apk")) {
                final PackageInfo info = pm.getPackageArchiveInfo(file.getPath(), 0);
                apkItems.add(new ApkItem(pm, info, file.getPath()));
            }
        }
        return apkItems;
    }
}
