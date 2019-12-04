plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}
repositories {
    mavenCentral()
    google()
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}
gradlePlugin {
    plugins.register("dependencies") {
        id = "dependencies"
        implementationClass = "me.hacket.deps.DependenciesPlugin"
    }
}
