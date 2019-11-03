package me.hacket.plugins.pgyer.api

/**
 * 蒲公英POST返回数据
 *
 * https://www.pgyer.com/doc/view/api#uploadApp
 *
 * Created by zengfansheng at 2021/8/2
 */
class PgyerResponse {

    public int code
    public String message
    public PgyerData data

    PgyerResponse(int code, String message) {
        this.code = code
        this.message = message
    }

    boolean isSuccessFul() {
        return code == 0 && data != null
    }

    static class PgyerData {
        public String buildKey // Build Key是唯一标识应用的索引ID
        public int buildType // 应用类型（1:iOS 2:Android）
        public int buildIsFirst // 是否是第一个App（1:是 2:否）
        public int buildIsLastest // 是否是最新版（1:是 2:否）
        public long buildFileSize // App 文件大小
        public String buildName // 应用名称
        public String buildVersion // 版本号, 默认为1.0 (是应用向用户宣传时候用到的标识，例如：1.1、8.2.1等。)
        public String buildVersionNo
        // 上传包的版本编号，默认为1 (即编译的版本号，一般来说，编译一次会变动一次这个版本号, 在 Android 上叫 Version Code。对于 iOS 来说，是字符串类型；对于 Android 来说是一个整数。例如：1001，28等。)
        public int buildBuildVersion // 蒲公英生成的用于区分历史版本的build号
        public String buildIdentifier // 应用程序包名，iOS为BundleId，Android为包名
        public String buildIcon
        // 应用的Icon图标key，访问地址为 https://www.pgyer.com/image/view/app_icons/[应用的Icon图标key]
        public String buildDescription // 应用介绍
        public String buildUpdateDescription // 应用更新说明
        public String buildScreenShots
        // 应用截图的key，获取地址为 https://www.pgyer.com/image/view/app_screenshots/[应用截图的key]
        public String buildShortcutUrl // 应用短链接
        public String buildQRCodeURL // 应用二维码地址
        public String buildCreated // 应用上传时间
        public String buildUpdated // 应用更新时间

        @Override
        String toString() {
            return Utils.sGson.toJson(this)
        }
    }

    @Override
    String toString() {
        return Utils.sGson.toJson(this)
    }
}
