### 1、compile, provided, apk
在`File→Project Structure→Dependencies`可配置
http://stackoverflow.com/questions/28472785/compile-provided-apk-android-dependency-scope

#### compile 参与编译和打包
1. **compile**
   所有的*build_type*以及*flavors*参与编译并且打包的apk中去。运行时需要。
* `compile fileTree(include: '*.jar', dir: 'libs')`  编译打包libs下所有的jar包
* `compile files('libs/picasso-2.4.0.jar')` 编译打包picasso-2.4.0.jar包
* `compile project(':common')` 编译打包common module

1. **debugCompile**
   所有debug的flavors参与编译并且打包到apk中去


2. **releaseCompile**
   所有release的flavors参与编译并且打包到apk中去


3. **testCompile**
   仅仅是针对单元测试代码的编译编译以及最终打包测试apk时有效，而对正常的debug或者release apk包不起作用。


4. **xxxCompile**
   所有xxx的flavors参与编译并且打包到apk中去,如releaseLogCompile

#### provided 只参与编译不参与打包
Provided是对所有的build type以及favlors只在编译时使用，类似eclipse中的external-libs,只参与编译，不打包到最终apk。

#### apk 只参与打包不参与编译
只会打包到apk文件中，而不参与编译，所以不能再代码中直接调用jar中的类或方法，否则在编译时会报错
