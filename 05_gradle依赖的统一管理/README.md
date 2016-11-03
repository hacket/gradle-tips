# Gradle依赖的统一管理

## 1、 在项目根目录添加config.gradle

```groovy
// 全局配置的gradle
ext {

    android = [compileSdkVersion: 23,
               buildToolsVersion: "23.0.2",
               applicationId    : "com.example.configgradle",
               minSdkVersion    : 14,
               targetSdkVersion : 22,
               versionCode      : 1,
               versionName      : "1.0.0"]

    dependencies = ["support-v4"               : 'com.android.support:support-v4:23.1.1',
                    "appcompat-v7"             : 'com.android.support:appcompat-v7:23.1.1',
                    "design"                   : 'com.android.support:design:23.1.1',
                    "cardview-v7"              : 'com.android.support:cardview-v7:23.1.1',
                    "recyclerview-v7"          : 'com.android.support:recyclerview-v7:23.1.1',
                    "multidex"                 : "com.android.support:multidex:1.0.+",
                    "butterknife"              : 'com.jakewharton:butterknife:7.0.1',
                    "volley"                   : 'com.mcxiaoke.volley:library:1.0.19',
                    "okhttp"                   : 'com.squareup.okhttp:okhttp:2.7.0',
                    "okhttp-urlconnection"     : 'com.squareup.okhttp:okhttp-urlconnection:2.7.0',
                    "leakcanary"               : 'com.squareup.leakcanary:leakcanary-android:1.3.1',
                    "glide"                    : 'com.github.bumptech.glide:glide:3.6.1',
                    "glide-okhttp-integration" : 'com.github.bumptech.glide:okhttp-integration:1.3.1',
                    "foldable-layout"          : 'com.alexvasilkov:foldable-layout:1.0.1',
                    "etsy-grid"                : 'com.etsy.android.grid:library:1.0.5']
}
```
## 2、 在项目根目录的build.gradle全局apply config.gradle，在其他module中不需要导入了，直接使用

```groovy
// Top-level build file where you can add configuration options common to all sub-projects/modules.
apply from: "config.gradle"

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:1.5.0'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        jcenter()
    }
}
```
## 3、使用config.gradle配置
* application使用 `app/build.gradle` 

```groovy
apply plugin: 'com.android.application'

android {
    compileSdkVersion rootProject.ext.android.compileSdkVersion
    buildToolsVersion rootProject.ext.android.buildToolsVersion

    defaultConfig {
        applicationId rootProject.ext.android.applicationId
        minSdkVersion rootProject.ext.android.minSdkVersion
        targetSdkVersion rootProject.ext.android.targetSdkVersion
        versionCode rootProject.ext.android.versionCode
        versionName rootProject.ext.android.versionName
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])

    compile rootProject.ext.dependencies['support-v4']
    compile rootProject.ext.dependencies["design"]
}
```

* library使用 `mylib/build.gradle`

```groovy
apply plugin: 'com.android.library'

android {
    compileSdkVersion rootProject.ext.android.compileSdkVersion
    buildToolsVersion rootProject.ext.android.buildToolsVersion

    defaultConfig {
        minSdkVersion rootProject.ext.android.minSdkVersion
        targetSdkVersion rootProject.ext.android.targetSdkVersion
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile rootProject.ext.dependencies['support-v4']
}
```

**参考：**http://mp.weixin.qq.com/s?__biz=MzA4NTQwNDcyMA==&mid=402733201&idx=1&sn=052e12818fe937e28ef08331535a179e#rd
or
https://github.com/stormzhang/9GAG
