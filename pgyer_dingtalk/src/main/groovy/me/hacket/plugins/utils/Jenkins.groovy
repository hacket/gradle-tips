package me.hacket.plugins.utils


import java.lang.reflect.Field

class Jenkins {

    private static JenkinsEnv jenkinsEnv


    /**
     * 是否处于Jenkins构建环境中
     * @return true是，false不是
     */
    static boolean isInJenkins() {
        Map<String, String> map = System.getenv()
        if (map == null) {
            return false
        }
        return map.containsKey("JENKINS_HOME")
    }

    static String getGitPreviousCommit() {
        if (jenkinsEnv == null) return ""
        return jenkinsEnv.GIT_PREVIOUS_COMMIT
    }

    static JenkinsEnv getJenkinsEnv() {

        if (jenkinsEnv != null) return jenkinsEnv

        jenkinsEnv = new JenkinsEnv()
        Map<String, String> env = System.getenv()
//    Utils.log("getJenkinsEnv(${env.size()}-${jenkinsEnv.getClass().getFields().size()})=$env")
        if (env == null || env.isEmpty()) return jenkinsEnv

//    Utils.log("getJenkinsEnv start.")
        Field[] fields = jenkinsEnv.getClass().getFields()
        env.entrySet().forEach {
            def key = it.key
            def value = it.value
//        Utils.log("key=$key, value=$value")
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i]
                String fieldName = field.getName()
                if (key.equalsIgnoreCase(fieldName)) {
                    field.setAccessible(true)
//                Utils.log("key=$key, fieldName=$fieldName, value=$value")
                    field.set(jenkinsEnv, value)
                    break
                }
            }
        }
//    Utils.log("getJenkinsEnv end. jenkinsEnv=$jenkinsEnv")
        return jenkinsEnv
    }


    static Map<String, String> logAllSysEnv() {
        Map<String, String> env = System.getenv()
        Utils.log("env start.")
        env.entrySet().forEach {
            def key = it.key
            def value = it.value
            Utils.log("$key:$value")
        }
        Utils.log("env end.")
    }

    static class JenkinsEnv {
        public String JENKINS_HOME // /Users/qbserver/.jenkins
        public String JENKINS_URL // http://macpro.xxx.com:8080/
        public String JOB_URL // http://macpro.xxx.com:8080/job/Android-Jenkins-Test/
        public String JOB_DISPLAY_URL
        // http://macpro.xxx.com:8080/job/Android-Jenkins-Test/display/redirect

        public String WORKSPACE //

        public String BUILD_USER_ID // zengfansheng
        public String BUILD_USER // 曾凡胜  需要插件才有效（user build vars plugin）
        public String BUILD_TAG // jenkins-Android-Jenkins-Test-4
        public String BUILD_ID // 4
        public String BUILD_NUMBER // 4
        public String BUILD_URL // http://macpro.xxx.com:8080/job/Android-Jenkins-Test/4/

        public String GIT_URL // git@git.xxx.com:zengfansheng/jenkinstest.git
        public String GIT_BRANCH // origin/master
        public String GIT_PREVIOUS_COMMIT // 0a2670b3d241d8012f62f1d9220c28ec50b92cb1
        public String GIT_COMMIT // 22e34d3018a931b0a5b4c6779617dfeba670a179

        // 自建
        public String REMIX_BRANCH
        public String HTTP_PROXY // true
        public String DingTalk_WebHook // true
        public String BUILD_TYPE // debug

        @Override
        String toString() {
            return Utils.sGson.toJson(this)
        }
    }


    // 获取从上一次成功build到当前提交的更新日志
    static def getChangeLog() {
        if (!isInJenkins()) {
            return ""
        }

        def env = getJenkinsEnv()

        def err = new StringBuffer()
        def lastBuildCommit = env.GIT_PREVIOUS_COMMIT
        def workspace = env.WORKSPACE
        def workspaceFile = new File(workspace)

        Utils.log("[getChangeLog] workspace: " + workspace)

        def gitVersion = new StringBuffer()
        def gitVersionProc = ("git --version").execute(null, workspaceFile)
        gitVersionProc.waitForProcessOutput(gitVersion, err)
        Utils.log("[getChangeLog] git version: " + gitVersion)

        def logTimeProc = ("git log --pretty=format:\"%ct\" " + lastBuildCommit + " -1").execute(null, workspaceFile)
        def lastBuildTime = new StringBuffer()
        logTimeProc.waitForProcessOutput(lastBuildTime, err)
        Utils.log("[getChangeLog] lastBuildTime: " + lastBuildTime)

        def diffLog = new StringBuffer()
        if (!lastBuildTime.toString().isEmpty()) {
            def gitChangeLogScript = new StringBuilder()
            gitChangeLogScript.append("git log --no-merges --pretty=format:\"%s\" --after ")
            gitChangeLogScript.append(lastBuildTime.toString())
            gitChangeLogScript.append(" -10")

            Utils.log("[getChangeLog] gitChangeLogScript=" + gitChangeLogScript)
            def changeLogProc = gitChangeLogScript.toString().execute(null, workspaceFile)
            changeLogProc.waitForProcessOutput(diffLog, err)
        }

        if (diffLog.length() == 0) {
            def gitChangeLogScript = new StringBuilder()
            gitChangeLogScript.append("git log --no-merges --pretty=format:\"%s\" -10")

            Utils.log("[getChangeLog] gitChangeLogScript=" + gitChangeLogScript)
            def changeLogProc = gitChangeLogScript.toString().execute(null, workspaceFile)
            changeLogProc.waitForProcessOutput(diffLog, err)
        }

        if (err != null && !err.toString().isEmpty()) {
            Utils.log("[getChangeLog] cmd err: " + err)
        }

        def diffLogStr = diffLog.toString()
        diffLogStr = diffLogStr.replaceAll("\"", "")
        diffLogStr = diffLogStr.replaceAll("\n", " \n\n")

        if (diffLogStr.length() > 1000) {
            diffLogStr = diffLogStr.substring(0, 999)
        }
        Utils.log("[getChangeLog] diffLog=[${diffLog}], diffLogStr=[${diffLogStr}]")

        Utils.log("======================start===================== st")

        def gitLog = new StringBuffer()
        def gitLogProcess = ("git log ${jenkinsEnv.GIT_PREVIOUS_COMMIT} HEAD -10").execute(null, workspaceFile)
        gitLogProcess.waitForProcessOutput(gitLog, err)
        Utils.log("GIT_PREVIOUS_COMMIT=${jenkinsEnv.GIT_PREVIOUS_COMMIT}, $gitLog=[${gitLog}]")

        Utils.log("======================end===================== st")

        return diffLogStr
    }
}
