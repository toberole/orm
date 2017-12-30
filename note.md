# ActivityThread 
	ActivityThread有什么作用呢?ActivityThread的作用很多，但最主要的作用是
	根据AMS(ActivityManagerService的要求，通过IApplicationTHread的接口)
	负责调度和执行activities、broadcasts和其它操作。在Android系统中，四大
	组件默认都是运行在主线程上的

# **AMS ActivityThread Activity**

# AMS在ActivityThread里面的代理是：
	// ActivityThread通过mgr调用AMS
	IActivityManager mgr = ActivityManagerNative.getDefault();
	
# ActivityThread在AMS里面的代理是：
	//	attach方法里面 AMS通过mAppThread控制ActivityThread
	// ApplicationThread mAppThread = new ApplicationThread();
	mgr.attachApplication(mAppThread);

<pre>

ActivityThread与AMS的交互是一次IPC调用，当然这里要搞清楚些，AMS调用
ActivityThread是通过ApplicationThreadProxy对象，而ActivityThread调
用AMS的方法却是ActivityManagerProxy

Activity的里面的生命周期方法其实是由AMS调度，ActivityThread执行,再到Activity本身。

Android系统是依靠消息分发机制来实现系统的运转的


发送广播其实也是利用AMS的

</pre>

### 四大组件都是反射创建，并执行生命周期方法，而调度是由AMS负责，ActivityThread主线程负责执行,所以说AMS是调度者，主线程是执行者

	

# 通过adb shell命令的方式测量应用的启动时间，命令为：

adb shell am start -W [packageName]/[packageName.MainActivity]

	执行成功后将返回三个测量到的时间：
	1、ThisTime:一般和TotalTime时间一样，除非在应用启动时开了一个透明的Activity预先处理一些事再显示出主Activity，这样将比TotalTime小。
	2、TotalTime:应用的启动时间，包括创建进程+Application初始化+Activity初始化到界面显示。
	3、WaitTime:一般比TotalTime大点，包括系统影响的耗时。

