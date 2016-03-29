
####gradle代理

> 我们经常会遇到，出现gradle编译时出现Connection to https://jcenter.bintray.com refused错误，无法连接到jcenter服务器，这时需要挂代理

**为gradle配置代理，gradle.properties文件加上下面几句：**

```java
systemProp.https.proxyPort=1080
systemProp.http.proxyHost=172.18.188.42
systemProp.https.proxyHost=172.18.188.42
systemProp.http.proxyPort=1080
```

####Reference
[Accessing the web via a proxy](https://docs.gradle.org/current/userguide/userguide_single.html#sec%3aaccessing_the_web_via_a_proxy)

