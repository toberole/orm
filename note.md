# ActivityThread 
	ActivityThread有什么作用呢?ActivityThread的作用很多，但最主要的作用是
	根据AMS(ActivityManagerService的要求，通过IApplicationTHread的接口)
	负责调度和执行activities、broadcasts和其它操作。在Android系统中，四大
	组件默认都是运行在主线程上的

# AMS在ActivityThread里面的代理是：
	//	attach方法里面 ActivityThread通过mgr调用AMS
	IActivityManager mgr = ActivityManagerNative.getDefault();
	
# ActivityThread在AMS里面的代理是：
	//	attach方法里面 AMS通过mAppThread控制ActivityThread
	// ApplicationThread mAppThread = new ApplicationThread();
	mgr.attachApplication(mAppThread);
