# 引用自带变量和引用属性值

## Project可用的标准属性
```groovy
println "name-->" + name  // app   --项目目录
println "project-->" + project // project ':app'   -- project实例
println "project.name-->" + project.name // app
println "project.path-->" + project.path // :app  --项目绝对路径
println "project.description-->" + project.description // null  --项目描述
println "project.projectDir-->" + project.projectDir // D:\workspace_studio\gradle-config\app  --包含生成脚本的目录
println "project.buildDir-->" + project.buildDir // D:\workspace_studio\gradle-config\app\build  --build目录
println "project.ant-->" + project.ant // org.gradle.api.internal.project.DefaultAntBuilder@372f10ce  --AntBuilder实例
```

## Android for gradle自带变量
```groovy
println '[rootDir]' + rootDir // D:\workspace_studio\gradle-config
println '[buildDir]' + buildDir  // D:\workspace_studio\gradle-config\app\build
println '[projectDir]' + projectDir // D:\workspace_studio\gradle-config\app
println '[project]' + project // project ':app'
println '[project]' + project.getName(); // app
println '[rootProject]' + rootProject // root project 'gradle-config'
```

## 引用配置文件里的变量
```groovy
// 使用 project.hasProperty('sdk.dir') 判断key值是否存在
println '[sdk.dir]:' + project.hasProperty("sdk.dir") // true
println '[sdk.ndk]:' + project.hasProperty("sdk.ndk") // true
println '[sdk.hacket]:' + project.hasProperty("sdk.hacket") // false
// 使用project.properties['sdk.dir']进行key值的value引用
println '[sdk.dir]:' + project.getProperties().get("sdk.dir") // [sdk.dir]:D:\adt-bundle-windows-x86_64-20140702\sdk
```

**gradle.properties**
```groovy
sdk.dir=D\:\\adt-bundle-windows-x86_64-20140702\\sdk
sdk.ndk = D\:\\ndk
```