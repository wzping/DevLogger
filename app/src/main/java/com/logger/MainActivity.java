package com.logger;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import dev.logger.DevLogger;
import dev.logger.DevLoggerUtils;
import dev.logger.LogConfig;
import dev.logger.LogLevel;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 写入权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },1);
            }
        }

        // == 在BaseApplication 中调用 ==

        // 初始化日志配置
        LogConfig lConfig = new LogConfig();
        // 堆栈方法总数(显示经过的方法)
        lConfig.methodCount = 3;
        // 堆栈方法索引偏移(0 = 最新经过调用的方法信息,偏移则往上推,如 1 = 倒数第二条经过调用的方法信息)
        lConfig.methodOffset = 0;
        // 是否输出全部方法(在特殊情况下，如想要打印全部经过的方法，但是不知道经过的总数)
        lConfig.isOutputMethodAll = false;
        // 显示日志线程信息(特殊情况，显示经过的线程信息,具体情况如上)
        lConfig.isDisplayThreadInfo = false;
        // 是否排序日志(格式化后)
        lConfig.isSortLog = false;
        // 日志级别
        lConfig.logLevel = LogLevel.DEBUG;
        // 设置Tag（特殊情况使用，不使用全部的Tag时,如单独输出在某个Tag下）
        lConfig.tag = "BaseLog";
        // 进行初始化配置 => 这样设置后, 默认全部日志都使用改配置, 特殊使用 DevLogger.other(config).d(xxx);
        DevLogger.init(lConfig);
        // 初始化, 设备信息、应用版本等获取存储(必须调用)
        DevLoggerUtils.appInit(getApplicationContext());

        // == 配置结束 ==


        // 发布的时候, 默认不需要打印日志则修改为
//        lConfig.logLevel = LogLevel.NONE;
//        DevLogger.init(lConfig); // 该方法设置全局默认日志配置

        // 还有一种情况，部分日志发布的时候不打印，但是有部分异常信息需要打印, 则单独使用配置
        // logConfig => 自己设置配置
        // DevLoggerUtils.getReleaseLogConfig() => 使用封装好的线上配置都行
        // DevLogger.other(logConfig).e();

        // == 模拟使用 ==

        // 延迟打印防止进入卡顿
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 打印日志
                LogOperate.operate();
            }
        }, 500);
    }
}
