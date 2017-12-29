# UrlRouter路由框架的设计 

	1、可取代使用startActivity、startActivityForResult跳转的情景，便于协同开发
	2、通过一串url可任意跳转到指定界面，使用应尽可能简单
	3、支持各种类型参数传递、界面转场动画
	4、可获取起跳界面的路径和当前界面路径，以便支持后期埋点等需求
	5、支持从H5到Native，Native到H5，这是Hybrid开发模式中常用到的需求
	6、对于push、浏览器外链跳转等可方便配置化，通过一个url来跳转指定界面

	URL格式：
	scheme://host/path

	scheme：APP内自己定义的，不过这个在H5内跳Native时，需要和前端协商定义好，本地间的跳转可以随自己定义，比如：activity
	host：这个尽可能按各个Activity的所在模块命名
	path：各个Activity的功能名
	
<pre>

public static boolean isIntentAvailable(Context context, Intent intent) {
    if (context == null || intent == null)
        return false;
    PackageManager packageManager = context.getApplicationContext().getPackageManager();
    List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
            PackageManager.MATCH_DEFAULT_ONLY);
    return list.size() > 0;
}

可以通过配置Activity的intent-filter规则来匹配，其中Action、Category、Data需要配置，而Action、Category我们可以固定为VIEW和DEFAULT，而Data则才是用来匹配目标Intent的

</pre>

	如下是匹配刚刚商品详情页的配置：
	<activity android:name=".DetailActivity">
    	<intent-filter>
	        <action android:name="android.intent.action.VIEW"/>
	        <category android:name="android.intent.category.DEFAULT"/>
	
	        <data android:scheme="activity"/>
	        <data android:host="product"/>
	        <data android:path="/detail"/>
    	</intent-filter>
	</activity>

	假如这个界面也需要支持从H5界面唤起，那么在保持native的跳转功能之外，还可以把前端定义的规则一起加进来，比如前端定义的唤起商品详情页的url为：

	h5://webview/goods_detail
	
	同时要支持上面这串url，我们只需在指定的Activity配置相应data即可： 
	<activity android:name=".DetailActivity">
	    <intent-filter>
	        <action android:name="android.intent.action.VIEW"/>
	        <category android:name="android.intent.category.DEFAULT"/>
	
	        <data android:scheme="activity"/>
	        <data android:host="product"/>
	        <data android:path="/detail"/>
	        <data android:scheme="h5"/>
	        <data android:host="webview"/>
	        <data android:path="/goods_detail"/>
	    </intent-filter>
	</activity>

	配置好了Activity，那么Intent就可以这样设置了：

	Intent intent = new Intent();
	intent.setAction(Intent.ACTION_VIEW);
	intent.addCategory(Intent.CATEGORY_DEFAULT);
	intent.setData(Uri.parse("activity://product/detail"));

	不过还没设置目标Activity的信息，所以这就需要通过packageManager.queryIntentActivities()来查询是否正确匹配符合我们url规则的Activity，有则最终我们会得到一个ResolveInfo对象，通过该对象我们可以得到目标Activity的包名、类名等信息，所以再设置给Intent用来start
	参数应怎么传递

	对于参数传递，我相信在使用url的情况下，很容易想到在url后面拼接json、KV或者其它自定义的格式，这种方式在处理普通的int、布尔、字符串确实非常方便，不过在传递对象、List集合情况下就不好处理了，所以，基于这个，可以这样定义，在传递字符串、int、布尔类型时直接使用url拼接形式，涉及到其它复杂的数据类型时使用Bundle传递

	匹配到多个Activity的处理:
	
	对于匹配过程中，可能存在一个Intent匹配到多个Activity，这时候就需要处理了，因为最终我们只需要打开一个Activity就行了，这时候千万别使用设置intent-filter的优先级来处理，也即：
	
	<intent-filter android:priority="10">
	
	这种做法是错误的，因为这个优先级只对有序广播有效，其它情况下获取到的值都为0，所以正确的处理规则如下：
	由于系统在匹配过程中，当匹配到多个时，会依匹配符合程度按循序排序好返回给我们，不过这时候难免会有第三方包的Activity，需优先匹配本应用包中的Activity，本包中没有再返回系统最匹配的，


<pre>

public static ResolveInfo queryActivity(Context context, Intent intent) {
    if (context == null || intent == null)
        return null;
    PackageManager packageManager = context.getApplicationContext().getPackageManager();
    List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent,
            PackageManager.MATCH_DEFAULT_ONLY);
    if (resolveInfoList == null || resolveInfoList.size() == 0)
        return null;
    int size = resolveInfoList.size();
    if (size == 1)
        return resolveInfoList.get(0);
    String appPackageName = context.getApplicationContext().getPackageName();
    for (int i = 0; i < size; i++) {
        ResolveInfo resolveInfo = resolveInfoList.get(i);
        String activityName = resolveInfo.activityInfo.name;
        if (TextUtils.isEmpty(activityName))
            continue;
        if (activityName.startsWith(appPackageName)) {
            return resolveInfo;
        }
    }
    return resolveInfoList.get(0);
}

</pre>


# 外置浏览器跳App内页面的处理

要支持外置浏览器跳App页面，必须在manifest文件中给相应的Activity的intent-filter添加<category android:name="android.intent.category.BROWSABLE"/> 属性，因为从浏览器中发起的intent的Category就是android.intent.category.BROWSABLE，所以要让App中相应的界面接收到浏览器的intent，则必须设置这个Category
	
	下面这个filter就支持浏览器跳App内页面:
	<intent-filter>
		<action android:name="android.intent.action.VIEW"/>
		<category android:name="android.intent.category.DEFAULT"/>
		<category android:name="android.intent.category.BROWSABLE"/>
	</intent-filter>

# UrlRouter框架跳转方式

	最终我们可以封成使用这样的方式来跳转的一个UrlRouter框架：
	
	UrlRouter.from(this).jump("activity://native/login");
	
	当需设置其它params、requestCode、转场动画时，可以这样使用：
	
	UrlRouter.from(this)
	        .params(bundle)
	        .requestCode(REQUEST_LOGIN)
	        .transitionAnim(0,0)
	        .jump("activity://native/login");

	对于需要跳转到主页时，应单独实现一个方法，因为主页的intent的action和category比较特殊，所以主页Activity应这样配置：

	<activity
	    android:name=".MainActivity"
	    android:label="@string/app_name"
	    android:theme="@style/AppTheme.NoActionBar">
	    <intent-filter>
	        <action android:name="android.intent.action.MAIN"/>
	
	        <category android:name="android.intent.category.LAUNCHER"/>
	        <category android:name="android.intent.category.DEFAULT"/>
	
	        <data
	            android:host="native"
	            android:path="/main"
	            android:scheme="activity"/>
	    </intent-filter>
	</activity>
	
	跳转代码为：
	
	UrlRouter.from(this).jumpToMain("activity://native/main");
	
	这样的方式非常简洁，而且维护也非常方便，因为统一都是在manifest文件中配置，最重要的是没有其它UrlRouter框架那么复杂繁重，最终封的框架只有三个类。




