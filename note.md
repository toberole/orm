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