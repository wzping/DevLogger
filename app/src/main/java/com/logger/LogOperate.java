package com.logger;

import android.os.Environment;
import android.util.Log;

import dev.logger.DevLogger;
import dev.logger.DevLoggerUtils;
import dev.logger.LogConfig;
import dev.logger.LogLevel;

/**
 * 日志操作 - 使用方法
 */
class LogOperate {

    private LogOperate() {
    }

    // 日志Tag
    private final static String LOG_TAG = LogOperate.class.getSimpleName();
    /** 日志文件夹路径 */
    public static final String LOG_SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Logger/";

    /**
     * 操作方法 - 防止测试Activity 代码过多
     */
    public static void operate() {
        // 测试打印Log所用时间
        textTime();

        // try, catch 保存异常日志
        exLog();

        // 使用日志操作
        tempLog();
    }

    /**
     * 测试打印Log所用时间
     */
    private static void textTime() {
        // 拼接字符串
        StringBuffer sBuffer = new StringBuffer();
        // 日志Tag
        final String tag = "CALCTIME";
        // --
        // 遍历次数
        int count = 1000;
        // 设置开始时间
        long sTime = System.currentTimeMillis();
        // 开始遍历
        for (int i = 0; i < count; i++) {
            Log.d(tag, "A:" + (i + 1));
        }
        // 拼接时间信息
        textTimeMosaic(sBuffer, "正常系统Log耗时记录", sTime, System.currentTimeMillis());

        // =======================================
        // 设置开始时间
        sTime = System.currentTimeMillis();
        // 开始遍历
        for (int i = 0; i < count; i++) {
            // DevLogger.d("B:" + (i + 1));
            DevLogger.dTag(tag, "B:" + (i + 1));
        }
        // 拼接时间信息
        textTimeMosaic(sBuffer, "Logger耗时记录", sTime, System.currentTimeMillis());

        // =======================================
        // 初始化日志配置
        LogConfig lConfig = new LogConfig();
        // 显示日志线程信息(特殊情况，显示经过的线程信息,具体情况如上)
        lConfig.isDisplayThreadInfo = true;
        // 是否排序日志(格式化后)
        lConfig.isSortLog = true;
        // 日志级别
        lConfig.logLevel = LogLevel.DEBUG;
        // 设置开始时间
        sTime = System.currentTimeMillis();
        // 开始遍历
        for (int i = 0; i < count; i++) {
            DevLogger.other(lConfig).dTag(tag, "C:" + (i + 1));
        }
        // 拼接时间信息
        textTimeMosaic(sBuffer, "Logger耗时记录 - 使用自定义日志配置", sTime, System.currentTimeMillis());
        // 打印时间
        Log.d(LOG_TAG, sBuffer.toString());
    }

    /**
     * 打印异常日志
     */
    private static void exLog() {
        // =================== 保存异常保存日志  ====================
        try {
            String s = null;
            s.indexOf("c");
        } catch (NullPointerException e) {
            // 打印格式化后的日志信息
            DevLogger.other(DevLoggerUtils.getSortLogConfig("LogPro")).e(e, "s = null");
            // 保存的路径
            String fName = LOG_SD_PATH + System.currentTimeMillis() + ".log";
            // 保存日志信息
            DevLoggerUtils.saveErrorLog(e, fName, true);
            // --
            // 保存自定义头部、底部信息
            DevLoggerUtils.saveErrorLog(e, "头部", "底部", LOG_SD_PATH, System.currentTimeMillis() + "_存在头部_底部.log", true);
            // --
            // 自定义(无设备信息、失败信息获取失败) - 正常不会出现，所以其实这个可以不用
            String[] eHint = new String[]{"DeviceInfo = 获取设备信息失败", "获取失败"};
            // 保存的路径
            fName = LOG_SD_PATH + System.currentTimeMillis() + "_orgs.log";
            // 保存日志信息
            DevLoggerUtils.saveErrorLog(e, fName, true, eHint);
        }
    }

    /**
     * 打印临时日志
     */
    private static void tempLog() {
        // =================== 打印零散数据  ====================
        TestData.ShareMsgVo sMsgVo = new TestData.ShareMsgVo();
        sMsgVo.sTitle = "分享Blog";
        sMsgVo.sText = null;
        sMsgVo.sImagePath = "http://t.jpg";
        sMsgVo.sTitleUrl = "http://www.test.com";

        TestData.UserInfoVo uInfoVo = new TestData.UserInfoVo();
        uInfoVo.uName = "BlogRecord";
        uInfoVo.uPwd = "log_pwd";
        uInfoVo.uAge = 100;

        // 打印分享数据
        DevLogger.d(LogTools.getShareMsgVoData(sMsgVo));
        // 打印用户数据
        DevLogger.d(LogTools.getUserInfoVoData(uInfoVo));
        // 打印零散数据
        DevLogger.d(LogTools.getScatteredData(uInfoVo.uName, sMsgVo.sTitle, uInfoVo.uAge));

        // =================== 打印测试数据  ====================
        // 日志TAG
        final String tag = LOG_TAG;
        // ====== 使用 BaseApplication 默认配置  ======
        // JSON数组
        DevLogger.json("[" + TestData.JSON_WITH_NO_LINE_BREAK + "," + TestData.JSON_WITH_NO_LINE_BREAK + "]");
        // JSON对象
        DevLogger.json(TestData.SMALL_SON_WITH_NO_LINE_BREAK);
        // XML数据
        DevLogger.xml(TestData.XML_DATA);
        // =========== 其他 ===========
        DevLogger.v("测试数据 - v");
        DevLogger.d("测试数据 - d");
        DevLogger.i("测试数据 - i");
        DevLogger.w("测试数据 - w");
        DevLogger.e("错误 - e");
        DevLogger.wtf("测试数据 - wtf");
        // --
        DevLogger.vTag(tag, "测试数据 - v");
        DevLogger.vTag(tag, "测试数据 - d");
        try {
            Class clazz = Class.forName("asdfasd");
        } catch (ClassNotFoundException e) {
            DevLogger.e(e, "发生异常");
        }
        // 占位符(其他类型，一样)
        DevLogger.d("%s测试占位符数据 - d%s", new Object[]{"1.", " - Format"});
        // --
        DevLogger.dTag(tag, "%s测试占位符数据 - d%s", new Object[]{"1.", " - Format"});


        // ====== 使用自定义临时配置  ======
        // 自定义配置, 如下使用方式
        // DevLogger.other(lConfig).d(message);
        // DevLogger.other(lConfig).dTag(tag, message);
        // 打印不换行的日志信息
        DevLogger.other(DevLoggerUtils.getDebugLogConfig(tag)).vTag("Temp", "测试数据 - v");
        DevLogger.other(DevLoggerUtils.getDebugLogConfig(tag)).d("测试数据 - d");
        DevLogger.other(DevLoggerUtils.getDebugLogConfig(tag)).i("测试数据 - i");
        DevLogger.other(DevLoggerUtils.getDebugLogConfig(tag)).w("测试数据 - w");
        DevLogger.other(DevLoggerUtils.getDebugLogConfig(tag)).e("错误 - e");
        DevLogger.other(DevLoggerUtils.getDebugLogConfig(tag)).wtf(tag, "测试数据 - wtf");
        // --
        DevLogger.other(DevLoggerUtils.getDebugLogConfig(tag, LogLevel.DEBUG)).json(TestData.SMALL_SON_WITH_NO_LINE_BREAK);

        // ----
        // 初始化日志配置
        LogConfig lConfig = new LogConfig();
        // 堆栈方法总数(显示经过的方法)
        lConfig.methodCount = 3;
        // 堆栈方法索引偏移(0 = 最新经过调用的方法信息,偏移则往上推,如 1 = 倒数第二条经过调用的方法信息)
        lConfig.methodOffset = 0;
        // 是否输出全部方法(在特殊情况下，如想要打印全部经过的方法，但是不知道经过的总数)
        lConfig.isOutputMethodAll = false;
        // 显示日志线程信息(特殊情况，显示经过的线程信息,具体情况如上)
        lConfig.isDisplayThreadInfo = true;
        // 是否排序日志(格式化后)
        lConfig.isSortLog = true;
        // 日志级别
        lConfig.logLevel = LogLevel.DEBUG;
        // 设置Tag（特殊情况使用，不使用全部的Tag时,如单独输出在某个Tag下）
        lConfig.tag = "SAD";
        // 打印不换行的日志信息
        DevLogger.other(lConfig).e("new Config - e");

        // ----
        // 使用方法
        LogConfig tLConfig = new LogConfig();
        // 堆栈方法总数(显示经过的方法)
        tLConfig.methodCount = 10;
        // 堆栈方法索引偏移(0 = 最新经过调用的方法信息,偏移则往上推,如 1 = 倒数第二条经过调用的方法信息)
        tLConfig.methodOffset = 0;
        // 是否输出全部方法(在特殊情况下，如想要打印全部经过的方法，但是不知道经过的总数)
        tLConfig.isOutputMethodAll = false;
        // 显示日志线程信息(特殊情况，显示经过的线程信息,具体情况如上)
        tLConfig.isDisplayThreadInfo = true;
        // 是否排序日志(格式化后)
        tLConfig.isSortLog = true;
        // 日志级别
        tLConfig.logLevel = LogLevel.DEBUG;
        // 设置Tag（特殊情况使用，不使用全部的Tag时,如单独输出在某个Tag下）
        tLConfig.tag = "SAD";
        try {
            String s = null;
            s.indexOf("tLConfig");
        } catch (Exception e) {
            // 打印不换行的日志信息
            DevLogger.other(tLConfig).e(e, "new Config - e");
        }
    }

    /**
     * 测试时间拼接公用方法
     * @param buffer
     * @param title 标题
     * @param sTime 开始时间
     * @param eTime 结束时间
     */
    private static void textTimeMosaic(StringBuffer buffer, String title, long sTime, long eTime) {
        // 使用时间
        long uTime = eTime - sTime;
        // 计算时间
        buffer.append("\n" + title);
        buffer.append("\n开始时间：" + sTime);
        buffer.append("\n结束时间：" + eTime);
        buffer.append("\n所用时间：" + uTime);
    }
}
