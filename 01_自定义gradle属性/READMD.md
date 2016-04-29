
### 自定义gradle属性

#### 1、在根目录建立config.gradle文件，定义全局通用的变量
```groovy
//全局定义gradle配置
ext {
    android =
            [
                    compileSdkVersion: 23,
                    buildToolsVersion: "23.0.2",
                    applicationId    : "com.example.configgradle",
                    minSdkVersion    : 14,
                    targetSdkVersion : 23,
                    versionCode      : 1,
                    versionName      : "1.0.0"
            ]
    dependencies =
            [
                    "support-v4"              : 'com.android.support:support-v4:23.1.1',
                    "appcompat-v7"            : 'com.android.support:appcompat-v7:23.1.1',
                    "design"                  : 'com.android.support:design:23.1.1',
                    "cardview-v7"             : 'com.android.support:cardview-v7:23.1.1',
                    "recyclerview-v7"         : 'com.android.support:recyclerview-v7:23.1.1',
                    "butterknife"             : 'com.jakewharton:butterknife:7.0.1',
                    "volley"                  : 'com.mcxiaoke.volley:library:1.0.19',
                    "okhttp"                  : 'com.squareup.okhttp:okhttp:2.7.0',
                    "okhttp-urlconnection"    : 'com.squareup.okhttp:okhttp-urlconnection:2.7.0',
                    "leakcanary"              : 'com.squareup.leakcanary:leakcanary-android:1.3.1',
                    "glide"                   : 'com.github.bumptech.glide:glide:3.6.1',
                    "glide-okhttp-integration": 'com.github.bumptech.glide:okhttp-integration:1.3.1',
                    "multidex": 'com.android.support:multidex:1.0.0'
            ]

}
```

#### 2、引入config.gradle文件
在根目录的`build.gradle`中引入，其他module就都可以用了
```groovy
apply from: "$rootDir/config.gradle"
```

####3、引用变量
```groovy
android {
    compileSdkVersion rootProject.ext.android.compileSdkVersion
    buildToolsVersion rootProject.ext.android.buildToolsVersion
}
// ...
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])

    compile rootProject.ext.dependencies['appcompat-v7']
    compile rootProject.ext.dependencies['design']
    compile rootProject.ext.dependencies['recyclerview-v7']
    compile rootProject.ext.dependencies['butterknife']
    compile rootProject.ext.dependencies['multidex']
}
```