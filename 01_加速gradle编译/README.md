###1、使用多线程编译
在gradle.properties，开启gradle单独的守护进程

同时这些参数也可以配置到前面的用户目录下的gradle.properties文件里，那样就不是针对一个项目生效，而是针对所有项目生效。

配置文件主要就是做， 增大gradle运行的java虚拟机的大小，让gradle在编译的时候使用独立进程，让gradle可以平行的运行。

```java
# Project-wide Gradle settings.

# IDE (e.g. Android Studio) users:
# Settings specified in this file will override any Gradle settings
# configured through the IDE.

# For more details on how to configure your build environment visit
# http://www.gradle.org/docs/current/userguide/build_environment.html

# The Gradle daemon aims to improve the startup and execution time of Gradle.
# When set to true the Gradle daemon is to run the build.
# TODO: disable daemon on CI, since builds should be clean and reliable on servers
org.gradle.daemon=true

# Specifies the JVM arguments used for the daemon process.
# The setting is particularly useful for tweaking memory settings.
# Default value: -Xmx10248m -XX:MaxPermSize=256m
org.gradle.jvmargs=-Xmx2048m -XX:MaxPermSize=512m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8

# When configured, Gradle will run in incubating parallel mode.
# This option should only be used with decoupled projects. More details, visit
# http://www.gradle.org/docs/current/userguide/multi_project_builds.html#sec:decoupled_projects
org.gradle.parallel=true

# Enables new incubating mode that makes Gradle selective when configuring projects. 
# Only relevant projects are configured which results in faster builds for large multi-projects.
# http://www.gradle.org/docs/current/userguide/multi_project_builds.html#sec:configuration_on_demand
org.gradle.configureondemand=true
```
###1.1跳过lint检测

```groovy
tasks.whenTaskAdded { task ->
    if (task.name.equals("lint")) {
        task.enabled = false
    }
}
```

###2、打开dex增量编译
这还是一个实验性的功能，但是还是推荐打开试试
在项目主Module下build.grade的Android中加入

```
dexOptions {
        incremental true
}

```

这是官方的速度对比，据说下一代编译速度更快

###3、使用最新的Gradle
Gradle对执行性能有很大的优化，但Android Studio现在默认使用的是Gradle 2.2,
所以我们需要手动让Android Studio使用Gradle，在项目根目录下的 build.grade中加入，如加入2.8

```
task wrapper(type: Wrapper) {
    gradleVersion = '2.8'
}

```

然后打开终端执行 ./gradlew wrapper，就可以下载Gradle 2.8了，下载完成后，我们需要在Android Studio让我们的项目使用Gradle 2.8。

注意gradle和android gradle for android plugin[版本对应](http://tools.android.com/tech-docs/new-build-system/version-compatibility)

###4、as配置
![](https://github.com/hacket/gradle-config/tree/master/01_%E5%8A%A0%E9%80%9Fgradle%E7%BC%96%E8%AF%91/img/gradle_config1.png)

![](https://github.com/hacket/gradle-config/tree/master/01_%E5%8A%A0%E9%80%9Fgradle%E7%BC%96%E8%AF%91/img/gradle_config2.png)


###Reference
 1. https://medium.com/the-engineering-team/speeding-up-gradle-builds-619c442113cb#.ng7ym9n50
 2. http://tools.android.com/tech-docs/new-build-system/version-compatibility
