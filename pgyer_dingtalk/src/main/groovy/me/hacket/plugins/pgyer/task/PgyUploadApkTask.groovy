package me.hacket.plugins.pgyer.task

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.api.BaseVariantOutput
import me.hacket.plugins.DingPgyerExtension
import me.hacket.plugins.pgyer.api.PgyerRequest
import me.hacket.plugins.pgyer.api.PgyerResponse
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction
class PgyUploadApkTask extends DefaultTask {

    public BaseVariant variant
    public Project targetProject

    PgyUploadApkTask() {
        setGroup(RemixConstants.GROUP)
        setDescription("upload apk to pgyer")
    }

    void init(BaseVariant variant, Project targetProject) {
        this.variant = variant
        this.targetProject = targetProject
    }

    @TaskAction
    def uploadToPgy() {

        Utils.log(" ${this.getName()} task start. ")

        AppExtension appExtension = (AppExtension) targetProject.getExtensions()
                .findByName(RemixConstants.ANDROID_EXTENSION_NAME)
        for (BaseVariantOutput it : variant.getOutputs()) {

            it.outputs[0].filters

            File apkFile = it.getOutputFile()
            if (apkFile == null || !apkFile.exists()) {
                PgyerResponse response = new PgyerResponse(RemixConstants.ERR_CODE_APK_NOT_EXISTS,
                        "uploadToPgy apkFile failed. Apk is not existed ${apkFile.getAbsolutePath()}.")
                Utils.log("uploadToPgy apkFile failed. apk is not existed!")
                project.ext._pgyer_response_ = response
                break
//                throw new RuntimeException(apkFile + " is not existed!")
            } else {
                Utils.log("uploadToPgy apkFile=${apkFile.getAbsoluteFile()}")
            }

            DingPgyerExtension extension = DingPgyerExtension.getConfig(targetProject)

            Utils.log("applicationId :" + variant.getMergedFlavor().getApplicationId())
            Utils.log("uploadFileName:" + apkFile.getAbsoluteFile())
            Utils.log("versionName   :" + appExtension.getDefaultConfig().getVersionName())
            Utils.log("versionCode   :" + appExtension.getDefaultConfig().getVersionCode())

            PgyerResponse response
            try {
                def request = new PgyerRequest(extension.pgyerApiKey, extension.changeLog)
                response = UploadApkToPgyerHelper.upload(request, apkFile)
                if (response != null && response.isSuccessFul()) {
                    def data = response.data
                    Utils.log("uploadToPgy succeed, data= $data")
                } else {
                    if (response == null) {
                        response = new PgyerResponse(RemixConstants.ERR_CODE_UPLOAD_PGYER, "uploadToPgy failed.")
                        Utils.log("uploadToPgy Response empty.")
                    } else {
                        Utils.log("uploadToPgy failed code=${response.code}, err_msg=${response.message}")
                    }
                }
            } catch (Exception e) {
                e.printStackTrace()
                Utils.log("uploadToPgy failed ${e.getMessage()}!!! ")
                if (response == null) {
                    response = new PgyerResponse(RemixConstants.ERR_CODE_UPLOAD_PGYER, "uploadToPgy failed. ${e.getMessage()}")
                }
            } finally {
                project.ext._pgyer_response_ = response
            }
            Utils.log(" ${this.getName()} task end. ")
        }
    }

}