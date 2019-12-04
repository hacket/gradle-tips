# buildSrc 和 Composing Builds 

## 什么是 buildSrc？
当运行 Gradle 时会检查项目中是否存在一个名为 buildSrc 的目录。然后 Gradle 会自动编译并测试这段代码，并将其放入构建脚本的类路径中, 对于多项目构建，只能有一个 buildSrc 目录，该目录必须位于根项目目录中。


## 什么是Composing Builds？
复合构建只是包含其他构建的构建. 在许多方面，复合构建类似于 Gradle 多项目构建，不同之处在于，它包括完整的 builds ，而不是包含单个 projects
- 组合通常独立开发的构建，例如，在应用程序使用的库中尝试错误修复时
- 将大型的多项目构建分解为更小，更孤立的块，可以根据需要独立或一起工作


## buildSrc vs Composing Builds
- 都支持自动补全和单击跳转
- buildSrc可用来实现自定义插件、gradle task和一些公共的配置(如dependencies、版本号等配置)；buildSrc种有依赖有更新时，会让build cache失效，将重新构建整个项目
- Composing Builds中有依赖更新时 不会重新构建整个项目；更推荐用这种方式来统一管理依赖