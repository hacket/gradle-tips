package me.hacket.plugins.dingtalk.task

import me.hacket.plugins.DingPgyerExtension
import me.hacket.plugins.dingtalk.api.DingRequest
import me.hacket.plugins.pgyer.api.PgyerResponse
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

/**
 * 发送消息到钉钉
 *
 * https://developers.dingtalk.com/document/robots/custom-robot-access
 */
class SendBuildResultToDingTalkTask extends DefaultTask {

    public Project targetProject

    SendBuildResultToDingTalkTask() {
        setGroup(RemixConstants.GROUP)
        setDescription("Send message to DingTalk!")
    }

    def init(Project targetProject) {
        this.targetProject = targetProject
    }

    @TaskAction
    def sendMessageToDingTalk() {

        def isInJenkins = Jenkins.isInJenkins()
        Utils.log(" ${this.getName()} task start, isInJenkins=$isInJenkins")

        DingPgyerExtension extension = DingPgyerExtension.getConfig(targetProject)

        Utils.log("apiToken = " + extension.apiToken)

        PgyerResponse pgyerResponse = project.ext._pgyer_response_
        if (pgyerResponse == null) {
            def msg = "未生成APK或上传蒲公英失败了！"
            buildFailedSendDingTalk(extension, msg, isInJenkins)
            throw new RuntimeException(msg)
        }

        if (pgyerResponse.isSuccessFul()) {
            buildSuccessSendDingTalk(extension, pgyerResponse, isInJenkins)
        } else {
            def msg = "${pgyerResponse.code}(${pgyerResponse.message})"
            buildFailedSendDingTalk(extension, msg, isInJenkins)
        }

        Utils.log(" ${this.getName()} task end. ")
    }

    protected def buildFailedSendDingTalk(DingPgyerExtension extension, String errMsg, boolean isInJenkins) {

        def appName = Utils.getAppName(project)
        String title = "[Android ${appName}] 构建失败"

        def at = extension.atMobiles
        def atSb = new StringBuilder()
        at.forEach {
            atSb.append("@" + it + " ")
        }

        def contentSb = new StringBuilder()
        contentSb.append("## Android ${appName} 构建失败${Utils.br}")
        contentSb.append("构建分支/Revision: ${Utils.getGitBranch()}(${Utils.getGitRevision()})${Utils.br}")
        contentSb.append("构建类型: ${getAppType(this.getName())}${Utils.br}")
        if (isInJenkins) {
            def jenkinsEnv = Jenkins.getJenkinsEnv()
            def buildUserId = jenkinsEnv.BUILD_USER_ID
//            def buildUser = jenkinsEnv.BUILD_USER
            contentSb.append("构建用户: ${buildUserId}${Utils.br}")
        }
        contentSb.append("构建失败原因: $errMsg ([蒲公英接口错误码](https://www.pgyer.com/doc/view/api#errorCode))${Utils.br}")
        contentSb.append("${atSb.toString()}${Utils.br}")

        def req = DingRequest.markdown(title, contentSb.toString(), at)

        Utils.log("buildFailedSendDingTalk isInJenkins=$isInJenkins, title${title}, content=${contentSb.toString()}")
        sendDingTalk(extension.apiToken, req)
    }

    protected def buildSuccessSendDingTalk(DingPgyerExtension extension, PgyerResponse response, boolean isInJenkins) {
        def pgyResponseData = response.data
        def buildFileSize = pgyResponseData.buildFileSize
        def appName = pgyResponseData.buildName
        def buildVn = pgyResponseData.buildVersion
        def buildVc = pgyResponseData.buildVersionNo
        def buildBuildVersion = pgyResponseData.buildBuildVersion
        def buildQRCodeURL = pgyResponseData.buildQRCodeURL
        def buildShortcutUrl = pgyResponseData.buildShortcutUrl
        def buildFileSizeFormat = Utils.formatFileSize(buildFileSize)

        def apkUrl = "https://www.pgyer.com/${pgyResponseData.buildKey}"

//        Utils.log("buildShortcutUrl = " + buildShortcutUrl)
        Utils.log(pgyResponseData.toString())

        String title = "[Android ${appName}] 构建成功"

        def at = extension.atMobiles
        def asSb = new StringBuilder()
        at.forEach {
            asSb.append("@" + it + " ")
        }

        def contentSb = new StringBuilder()
        contentSb.append("### Android ${appName} 构建成功${Utils.br}")
        contentSb.append("构建分支/Revision: ${Utils.getGitBranch()}(${Utils.getGitRevision()})${Utils.br}")
        contentSb.append("构建类型: ${getAppType(this.getName())}${Utils.br}")
        contentSb.append("App版本(vn/vc): ${buildVn}(${buildVc})${Utils.br}")
        contentSb.append("App大小: ${buildFileSizeFormat}${Utils.br}")
        contentSb.append("[蒲公英下载#${buildBuildVersion}](${apkUrl})${Utils.br}")
        contentSb.append("![](${buildQRCodeURL})${Utils.br}")
        if (isInJenkins) {
            def jenkinsEnv = Jenkins.getJenkinsEnv()
            def buildType = jenkinsEnv.BUILD_TYPE
            def jenkinsDownloadUrl = "${jenkinsEnv.JOB_URL}${jenkinsEnv.BUILD_NUMBER}/artifact/app/build/outputs/apk/${buildType}/${appName}-${pgyResponseData.buildVersion}-${pgyResponseData.buildVersionNo}-${buildType}.apk"
            def jenkinsQrcodeUrl = "${jenkinsEnv.JOB_URL}${jenkinsEnv.BUILD_NUMBER}/artifact/app/build/outputs/apk/${buildType}/QRcode.png"
            def buildUserId = jenkinsEnv.BUILD_USER_ID
//            def buildUser = jenkinsEnv.BUILD_USER
            contentSb.append("[备用Jenkins下载#(${jenkinsEnv.BUILD_NUMBER})](${jenkinsDownloadUrl})  ")
            contentSb.append("[二维码](${jenkinsQrcodeUrl})${Utils.br}")
            contentSb.append("构建用户: ${buildUserId}${Utils.br}")
        }
        contentSb.append("构建时间: ${Utils.getNowTime()}${Utils.br}")
        contentSb.append("更新日志: ${Utils.br} > ${extension.changeLog}${Utils.br}")
        if (isInJenkins) {
            contentSb.append("更新日志: ${Jenkins.getChangeLog()}${Utils.br}")
        }
        contentSb.append("${asSb.toString()}${Utils.br}")

        def req = DingRequest.markdown(title, contentSb.toString(), at)

        Utils.log("buildSuccessSendDingTalk isInJenkins=${isInJenkins}, title${title}, content=${contentSb.toString()}")

        sendDingTalk(extension.apiToken, req)
    }

    protected def sendDingTalk(String token, DingRequest req) {
        Utils.log("sendDingTalk prepare, token=${token}, req=$req")
        def response = SendToDingTalkHelper.sendToDingTalk(token, req)
        if (response != null && response.isSuccessFul()) {
            Utils.log("sendDingTalk succeed, response= " + response)
        } else {
            if (response == null) {
                Utils.log("sendDingTalk Response empty.")
                throw new RuntimeException("sendDingTalk Response empty.")
            } else {
                Utils.log("sendDingTalk failed code=${response.errcode}, err_msg=${response.errmsg}")
                throw new RuntimeException("sendDingTalk failed code=${response.errcode}, err_msg=${response.errmsg}")
            }
        }
    }

    protected String getAppType(String taskName) {
        String t
        if (taskName.contains("Debug")) {
            t = "debug(开发包)"
        } else if (taskName.contains("Preview")) {
            t = "preview(测试包)"
        } else if (taskName.contains("Cloundtest")) {
            t = "cloundtest(云测包)"
        } else if (taskName.contains("Release")) {
            t = "release(发版包)"
        } else {
            t = "(未知类型包)"
        }
        return t
    }
}