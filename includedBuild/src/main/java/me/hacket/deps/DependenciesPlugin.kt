package me.hacket.deps

import org.gradle.api.Plugin
import org.gradle.api.Project

class DependenciesPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("------------>>> apply VersionPlugin")
    }
}
