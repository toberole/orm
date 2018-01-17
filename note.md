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



# View绘制

 绘制需要调用draw方法，总共分为六个步骤：

1. 绘制背景
2. 如果需要，保存canvas的层次准备边缘淡化。
3. 绘制view的内容
4. 绘制子view
5. 如果需要，绘制淡化的边缘并存储图层。
6. 绘制装饰部分，例如滚动条等。
    

# 测量的模式

　　1.EXACTLY，精确值模式：将layout_width或layout_height属性指定为具体数值或者match_parent。

　　2.AT_MOST，最大值模式：将layout_width或layout_height指定为wrap_content。

　　3.UNSPECIFIED： View想多大就多大
# 注意：  
View类默认的onMeasure()方法只支持EXACTLY模式，如果要支持其它模式，就必须重写onMeasure()  

重写onMeasure()模版代码：
 
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 或者直接调用父类的setMeasuredDimension(),因为父类的onMeasure()最终调用了setMeasuredDimension()
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    /**
     * 测量View的width
     *
     * @param measureSpec MeasureSpec对象
     * @return View的width
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 200;// 默认值
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 测量View的height
     *
     * @param measureSpec MeasureSpec对象
     * @return View的height
     */
    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 200;// 默认值
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


# 实现自定义View的三种常用方法：

	1）通过重写onDraw()对原生控件进行扩展；

	2）通过组合实现新的控件，通常集成一个合适的额ViewGoup，再通过addView()给它添加指定功能的控件，从而组合成新的复合控件。

	3）重写View实现全新的控件，通过重写onDraw()，onMeasure()实现绘制逻辑，重写onTouchEvent()实现交互逻辑。



# 自定义ViewGroup

	1）onMeasure()：对子View进行测量；

	2）onLayout()：设置子View的位置；

	3）onTouchEvent()：设置触摸交互事件。


# Android系统启动流程
1 首先Bootloader引导程序启动完Linux内核后，会加载各种驱动和数据结构，当有了驱动以后，开始启动Android系统，同时会加载用户级别的第一个进程init(system\core\init.c),该进程会首先加载一个init.rc配置文件。
代码：
<pre>
	int main(int argc, char **argv){
    
        // 创建文件夹 挂载
        mount("tmpfs", "/dev", "tmpfs", 0, "mode=0755");
        mkdir("/dev/pts", 0755);
       
        // 打开日志
        log_init();
        
        INFO("reading config file\n");
        // 加载init.rc配置文件
		init_parse_config_file("/init.rc");
	}

</pre> 
2 init.rc配置文件会进行很多的配置，创建很多的文件夹及文件，然后初始化一些Android驱动器，之后该配置文件最重要的一个任务就是启动一个Zygote(孵化器)进程，此进程是Android系统的一个母进程，用来启动Android的其他服务进程.
代码：

<pre>

# Mount filesystems and start core system services.
on late-init
    trigger early-fs

    # Mount fstab in init.{$device}.rc by mount_all command. Optional parameter
    # '--early' can be specified to skip entries with 'latemount'.
    # /system and /vendor must be mounted by the end of the fs stage,
    # while /data is optional.
    trigger fs
    trigger post-fs

    # Mount fstab in init.{$device}.rc by mount_all with '--late' parameter
    # to only mount entries with 'latemount'. This is needed if '--early' is
    # specified in the previous mount_all command on the fs stage.
    # With /system mounted and properties form /system + /factory available,
    # some services can be started.
    trigger late-fs

    # Now we can mount /data. File encryption requires keymaster to decrypt
    # /data, which in turn can only be loaded when system properties are present.
    trigger post-fs-data

    # Now we can start zygote for devices with file based encryption
    trigger zygote-start

    # Load persist properties and override properties (if enabled) from /data.
    trigger load_persist_props_action

    # Remove a file to wake up anything waiting for firmware.
    trigger firmware_mounts_complete

    trigger early-boot
    trigger boot
</pre>

3 Zygote会执行一个app_process下面的 app_main.cpp可执行文件，在这个文件中首先添加了Android运行时环境，在Android运行时中调用了ZygoteInit.java，这就从c++代码跳到了java代码。
<pre>
// 从c++代码跳到了java代码
runtime.start("com.android.internal.os.ZygoteInit",
                startSystemServer ? "start-system-server" : "");
</pre>
4 在ZytofeInit.java代码中首先设置了Java虚拟机的堆内存空间，然后启动一个类加载器加载Android启动依赖的类比如Activity等四大组件，dialog等UI的类，然后分出一个子进程启动SystemServer系统服务。
<pre>
	public static void main(String argv[]) {
		registerZygoteSocket();
		preload();
		if (argv[1].equals("start-system-server")) {
			// 启动系统服务
			    /**
     			* Prepare the arguments and fork for the system 
     			* server process.
     			*/
        	startSystemServer();//com.android.server.SystemServer
        }
	}


    static void preload() {
		// 加载 Android依赖的类
        preloadClasses();
		// 加载 Android依赖的Resource
        preloadResources();
		// 加载 OpenGL
        preloadOpenGL();
    }

</pre>

5 在SystemServer.java代码中启动Native世界，启动Android的Framework世界

<pre>
public static void main(String[] args) {
	
        System.loadLibrary("android_servers");

        Slog.i(TAG, "Entered the Android system server!");

        // Initialize native services.启动Native世界
		// Called to initialize native system services.
        nativeInit();

        // This used to be its own separate thread, but now it is
        // just the loop we run on the main thread.
		// 启动Android的Framework世界
        ServerThread thr = new ServerThread();
        thr.initAndLoop();
}
</pre>

6 SystemServer首先调用加载JNI库，启动Native世界。通过System.loadLibrary("android_servers")加载一个类库文件，其对应的源码文件为com_android_server_SystemServer.cpp

7 启动Android的Framework世界 ServerThread,开启了Android中的各种服务比如LightService，PowerManagerService，BatteryService，WindowManagerService等，并将服务添加到ServiceManager中去管理，启动完各种服务后，调用ActivityManagerService.systemReady方法

8 ActivityManagerService.systemReady里面会启动桌面Launch``

# IPC
在利用Binder发起IPC调用的时候，发起方的线程会被挂起 ，等待服务方的返回结果。

注意：如果请求的任务比较的耗时 那么就不能在主线程里面发起请求。

服务方为进程请求分配每个进程的线程池，服务方被调用的方法是运行在服务方的Binder 线程池中的【在执行服务端的方法的时候如果需要给客户端返回结果，那么千万不要再起线程了，这样是为了避免客户端拿不到结果】，且该线程池有最大限制。


多线程使用同一个SqliteDataBase操作数据库的时候 不会存在多线程的问题，但是如果不是使用的同一个SqliteDatabase就会存在问题。


# Application
ActivityThread的attach方法里面会生成ContextImpl 即Context

<pre>
    mInstrumentation = new Instrumentation();
    ContextImpl context = ContextImpl.createAppContext(
            this, getSystemContext().mPackageInfo);
	// Instrumentation.newApplication 回传ContextImpl 赋值给mBase
    mInitialApplication = context.mPackageInfo.makeApplication(true, null);
    mInitialApplication.onCreate();
</pre>


# 系统启动
在系统服务启动时添加PackageManagerService，在这个过程中packagemanager就会对各个app安装目录的apk文件进行扫描解析，manifest就是此时解析的。

# 广播的注册
1、静态注册 在manifest文件里面配置

2、利用Context注册 最终调用的是ActivityManagerNative.getDefault().registerReceiver(），AMS

# 特别注意
onReceive运行在主线程【不能做耗时的操作】

**静态注册**的广播 每次在接受广播事件的时候，该接受者都是被重新new出来的，出了receiver方法之后 就有可能被kill掉，所以在onreceiver里面new 线程做耗时操作 是不靠谱稳定的。

**动态注册**的BroadcastReceiver. 动态注册的BroadcastReceiver对象的生命其实是受控制的.使用Context.registerReceiver和Context.unregisterReceiver注册的BroadcastReceiver每次收到广播都是使用我们注册时传入的对象处理的。



分为两种，有序广播，首先按照优先级排列，同优先级的动态广播先于静态广播，同优先级的动态广播中先注册的先处理，同优先级的静态广播中先扫描的APP广播先处理。
有序广播则，无视优先级，动态广播先于静态广播，动态广播中先注册的先处理，静态广播中先扫描的APP广播先处理。



1. 普通广播
2. 有序广播
	  广播接受者按照优先级 处理广播，广播可以被修改 可以截断终止 低优先级的广播接受者就接受不到广播了
3. 粘性广播
	  权限<uses-permission android:name="android.permission.BROADCAST_STICKY" />
	  sendStickyBroadcast 发送出去之后 被所有接受者处理完毕之后还存在着 
      除非使用removeStickyBroadcast移除

# 全局广播设置成局部广播

    注册广播时将exported属性设置为false，使得非本App内部发出的此广播不被接收；

    在广播发送和接收时，增设相应权限permission，用于权限验证；

    发送广播时指定该广播接收器所在的包名，此广播将只会发送到此包中的App内与之相匹配的有效广播接收器中。

	使用封装好的LocalBroadcastManager类


# View
## 移动
scrollBy / scrollTo 只能改变view的内容的位置，不能改变View的位置，内容的移动方朝向左上角方向的时候，scrollX 与scrollY取正，否则为复制。

# View动画
view动画只是对view的影像做了移动，并不能改变view的坐标值，在移动之后 点击新位置的view无法响应点击事件【移动的仅仅是view的影像位置】


# 改变布局参数
操作稍微复杂

# View移动总结
1. scrollTo / scrollBy 操作简单 适合对view的内容的滑动
2. 动画 操作简单 主要适用于没有交互的View和实现复杂的动画效果
3. 改变布局参数 操作稍微复杂 适用于有交互的view


# ViewGroup派发Touch事件：
在dispatchTouchEvent方法里面处理 ，首先会调用onInterceptTouchEvent判断是否拦截事件，如果拦截事件了，Touch事件就不会交给子View处理。如果没有拦截就交给子View处理，如果子View都不处理 ，最后事件还是会返回到ViewGroup，此时ViewGroup调用自己的TouchListener里面的onTouch，如果TouchListener为空或者是onTouch方法没有消费touch事件，那么会再调用onTouchEvent方法，在onTouchEvent方法里面会派发longClick或者click。


adb shell下使用getprop可以查看当前系统的属性值

在service_manager.c main方法里面可以看到binder相关的启动
<pre>

int main(int argc, char **argv)
{
    struct binder_state *bs;
    void *svcmgr = BINDER_SERVICE_MANAGER;

    bs = binder_open(128*1024);

    if (binder_become_context_manager(bs)) {
        ALOGE("cannot become context manager (%s)\n", strerror(errno));
        return -1;
    }

    svcmgr_handle = svcmgr;
    binder_loop(bs, svcmgr_handler);
    return 0;
}

</pre>

Linux系统里面有3中不同类型的用户可以对文件或者是目录进行访问：文件所有者、同组用户、其他用户。



AXMLprinter2 是一款将AXML转换为可读的xml文件的工具
java -jar AXMLprinter2.jar  xxx.xml output.xml

反汇编工具 IDA PRO、radare、smiasm

smail语言是对Dalvik虚拟机字节码的一种解释。baksmail可以反编译smail语言

ViewRootImpl是联接WindowManager和DecorView的纽带，View的绘制三大流程都是从ViewRoot触发开始的。

对于DecorView，其MeasureSpec由窗口的尺寸和其自身的LayoutPrams来共同确定；对于普通View，其MeasureSpec由父容器的MesaureSpec和自身的LayoutParams来共同决定。

# 在ViewGroup的getChildMeasureSpec里面可以查看子View的MeasureSpec的生成规则

View的测量宽度和高度在Measure中形成的，View的最终宽高在layout中形成的

# 自定义View的注意事项：

1、让View支持 wrap_content，如果不自己处理 wrap_content,那么即时view使用的是wrap_content，实际的效果也是match_parent

2、如果需要 让view支padding，继承自view的需要在draw方法里面处理，继承自ViewGroup的控件需要在onMeasure和onLayout中考虑padding和子元素的margin对其造成的影响，不然将导致padding和子元素的margin失效。一定要注意padding也是View的内容，padding也会占据view的空间，在计算坐标 draw的时候要注意。

3、尽量不要在view里面使用handler，view内部提供了post系列的方法，可以代替handler的作用。

4、View中如果有线程或者动画，需要及时终止。可以在onDetachedFromWindow是一个很好的机会。当包含此View的Activity退出或者当前View被remove时，View的onDetachedFromWindow方法会被调用，和此方法对应的是onAttachedToWindow,当包含此View的Activity启动时，View的onAttachedToWindow方法会被调用。同时，当View变得不可见时我们也需要停止线程和动画，如果不及时处理这种问题，有可能会造成内存泄漏。

5、View带有滑动嵌套情形时，需要处理滑动冲突。

# 自定义View


























