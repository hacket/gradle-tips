###dex突破65535的限制
随着项目的一天天变大，慢慢的都会遇到单个dex最多65535个方法数的瓶颈，如果是ANT构建的项目就会比较麻烦，但是Gradle已经帮我们处理好了，而添加的方法也很简单，总共就分三步 :

1.首先是在defaultConfig节点使能多DEX功能

```groovy
android {
    defaultConfig {
        // dex突破65535的限制
        multiDexEnabled true
    }
}
```

2.然后就是引入multidex库文件

```groovy
dependencies {
    compile 'com.android.support:multidex:1.0.0'
}
```

3.最后就是你的BaseApplication继承一下MultiDexApplication或者在Application的onCreate()方法加入这句：
```java
MultiDex.install(this);
```

###Reference
[Building Apps with Over 64K Methods](http://developer.android.com/intl/zh-cn/tools/building/multidex.html "http://developer.android.com/intl/zh-cn/tools/building/multidex.html")
[关于『65535问题』的一点研究与思考](http://blog.csdn.net/zhaokaiqiang1992/article/details/50412975 "http://blog.csdn.net/zhaokaiqiang1992/article/details/50412975")