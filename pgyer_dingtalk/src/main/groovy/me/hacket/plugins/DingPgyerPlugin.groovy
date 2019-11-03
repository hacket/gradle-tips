package me.hacket.plugins

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import me.hacket.plugins.pgyer.task.PgyUploadApkTask
import org.gradle.api.*

class DingPgyerPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {

        // 是否有com.android.application插件
        if (!target.plugins.hasPlugin("com.android.application")) {
            throw new ProjectConfigurationException("Plugin requires the 'com.android.application' plugin to be configured.", null)
        }

        if (Jenkins.isInJenkins()) {
            Jenkins.logAllSysEnv()
        }
        applyExtension(target)
        applyTask(target)
    }

    void applyTask(Project target) {
        target.afterEvaluate {
            DingPgyerExtension remixExtension = target.extensions.findByName(Constants.EXTENSION_NAME)
            Utils.log("applyTask--->>> RemixDingPgyerExtension：$remixExtension , jenkinsEnv=${Jenkins.getJenkinsEnv()}")
            AppExtension androidExtension = target.getExtensions().findByName(Constants.ANDROID_EXTENSION_NAME)
            DomainObjectSet<ApplicationVariant> variants = androidExtension.applicationVariants
            variants.all { ApplicationVariant variant ->
                String variantName = variant.baseName.capitalize()

                boolean isEnable = remixExtension.isEnable(variantName)
                if (!isEnable) {
                    Utils.log("================= 不可用，不创建task了$variantName baseName=${variant.baseName}")
                    return
                }
                // 创建蒲公英上传task
                PgyUploadApkTask pgyerTask = target.tasks.create("${Constants.TASK_UPLOAD_PGYER}$variantName", PgyUploadApkTask.class)
                Utils.log("applyTask--->>> 创建蒲公英上传task：${Constants.TASK_UPLOAD_PGYER}$variantName baseName=${variant.baseName}")
                pgyerTask.init(variant, target)

                // 创建发送钉钉消息task
                Task dingTalkTask = target.tasks.create("${Constants.TASK_SEND_DINGTALK}$variantName", SendBuildResultToDingTalkTask.class)
                Utils.log("applyTask--->>> 创建发送钉钉消息task：${Constants.TASK_SEND_DINGTALK}$variantName")
                dingTalkTask.init(target)

                Task assemble
                if (variant.hasProperty('assembleProvider')) {
                    // variant.getAssemble()' is obsolete and has been replaced with 'variant.getAssembleProvider()'
                    assemble = variant.assembleProvider.get()
                } else {
                    assemble = variant.assemble
                }
                assemble.dependsOn(target.getTasks().findByName("clean"))
                pgyerTask.dependsOn(assemble)
                dingTalkTask.dependsOn(pgyerTask)
            }
        }
    }

    private void applyExtension(Project target) {
        target.extensions.create(Constants.EXTENSION_NAME, DingPgyerExtension)
    }

}
