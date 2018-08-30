# DevLogger
Android 开发日志打印库 - 支持保存文件、设备信息，版本信息等

### Gradle
Step 1. Add the JitPack repository to your build file
```
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```

Step 2. Add the dependency
```
dependencies {
	implementation 'com.github.afkT:DevLogger:1.0'
}
```

### 预览

**XML、JSON 格式化打印**

<img src="https://raw.githubusercontent.com/afkT/DevLogger/master/log_xml_json.png"/>

**打印堆栈信息**

<img src="https://raw.githubusercontent.com/afkT/DevLogger/master/log_default.png"/>

**打印异常信息**

<img src="https://raw.githubusercontent.com/afkT/DevLogger/master/log_error.png"/>

**正常打印**

<img src="https://raw.githubusercontent.com/afkT/DevLogger/master/log_other.png"/>

**保存日志文件**

<img src="https://raw.githubusercontent.com/afkT/DevLogger/master/log_file_error.png"/>


### 配置方法

```java
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
// lConfig.logLevel = LogLevel.NONE;
// DevLogger.init(lConfig); // 该方法设置全局默认日志配置

// 还有一种情况，部分日志发布的时候不打印，但是有部分异常信息需要打印, 则单独使用配置
// DevLoggerUtils.getReleaseLogConfig(TAG) => 使用封装好的线上配置都行
// DevLoggerUtils.getReleaseLogConfig(TAG, LogLevel) => 使用封装好的线上配置都行
// DevLogger.init(DevLoggerUtils.getReleaseLogConfig(TAG));
```

### 使用方法

```java
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
```