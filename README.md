# Gradle Tips

## 实用的Gradle技巧

#### [自定义gradle属性](https://github.com/hacket/gradle-config/tree/master/tips/01_gradle_custom_property  "https://github.com/hacket/gradle-config/tree/master/01_gradle_custom_property")

### [gradle自带变量和引用属性值](https://github.com/hacket/gradle-config/tree/master/tips/02_gradle%E8%87%AA%E5%B8%A6%E5%8F%98%E9%87%8F%E5%92%8C%E5%BC%95%E7%94%A8%E5%B1%9E%E6%80%A7%E5%80%BC "https://github.com/hacket/gradle-config/tree/master/02_gradle%E8%87%AA%E5%B8%A6%E5%8F%98%E9%87%8F%E5%92%8C%E5%BC%95%E7%94%A8%E5%B1%9E%E6%80%A7%E5%80%BC")

### [gradle代理](https://github.com/hacket/gradle-config/tree/master/tips/03_gradle%E4%BB%A3%E7%90%86 "https://github.com/hacket/gradle-config/tree/master/03_gradle%E4%BB%A3%E7%90%86")

### [dex突破65535的限制](https://github.com/hacket/gradle-config/tree/master/tips/04_dex%E7%AA%81%E7%A0%B465535%E7%9A%84%E9%99%90%E5%88%B6 "https://github.com/hacket/gradle-config/tree/master/04_dex%E7%AA%81%E7%A0%B465535%E7%9A%84%E9%99%90%E5%88%B6")

### [gradle依赖的统一管理](https://github.com/hacket/gradle-config/tree/master/tips/05_gradle%E4%BE%9D%E8%B5%96%E7%9A%84%E7%BB%9F%E4%B8%80%E7%AE%A1%E7%90%86 "https://github.com/hacket/gradle-config/tree/master/05_gradle%E4%BE%9D%E8%B5%96%E7%9A%84%E7%BB%9F%E4%B8%80%E7%AE%A1%E7%90%86")

### [加速gradle编译](https://github.com/hacket/gradle-config/tree/master/tips/06_%E5%8A%A0%E9%80%9Fgradle%E7%BC%96%E8%AF%91 "https://github.com/hacket/gradle-config/tree/master/06_%E5%8A%A0%E9%80%9Fgradle%E7%BC%96%E8%AF%91")


## Gradle插件

### [pgyer_dingtalk](plugins/pgyer_dingtalk)
一键打包上传到蒲公英，并发送钉钉消息到群，极大地提升分发包的效率

**特性：**
1. 打包完毕后，上传包到蒲公英
2. 发消息到钉钉，并支持@人
3. 支持配合Jenkins，支持按包类型、分支、代理、@人及32位和64位等功能


**使用：**
```gradle
apply plugin: 'dingPgyer'
dingPgyerConfig {
    pgyerApiKey = "xxx" // 蒲公英ApiKey
    apiToken = "xxx" // 钉钉群机器人toekn
    atMobiles = ['135xxx','136xxx'] // 群@人的手机号
    enableByVariant = { variantName -> // 对哪些flavor开启
        variantName.toLowerCase().contains("dev-preview") || variantName.toLowerCase().contains("product-release")
    }
    changeLog = "${getGitLog(5)}" // build log，获取前5条git log
}
/**
 * 获取git log
 * @param pre 多少条git log
 * @return
 */
private String getGitLog(int pre) {
    def diffLog = new StringBuffer()
    def err = new StringBuffer()
    def workspaceFile = rootDir

    def gitChangeLogScript = new StringBuilder()
    gitChangeLogScript.append("git log --no-merges --pretty=format:\"%s\" -${pre}")
    println(gitChangeLogScript)
    def changeLogProc = gitChangeLogScript.toString().execute(null, workspaceFile)
    changeLogProc.waitForProcessOutput(diffLog, err)

    def diffLogStr = diffLog.toString()
    diffLogStr = diffLogStr.replaceAll("\"", "")
    diffLogStr = diffLogStr.replaceAll("\n", "\n\n > ")

    if (diffLogStr.length() > 1000) {
        diffLogStr = diffLogStr.substring(0, 999)
    }
    return diffLogStr
}
```

**打包发送到钉钉消息效果图：** <br/>
<img src="https://github.com/hacket/gradle-tips/blob/master/imgs/dingtalk_pgyer.jpg" width="385px" height="928px">


### [module_dependency_switch](plugins/switch_module_dependency)
组件化过程中，module和aar切换

配置`module_dependency.json`：
```
{
    "useModule": true,
    "module_name": "Modularization1",
    "module_dir": "Modularization1",
    "module_group": "me.hacket:Modularization1"
}
```
- useModule true表示module依赖，false表示aar依赖
- module_name module的name
- module_dir module路径
- module_group module对应的远端aar的group和artifactId