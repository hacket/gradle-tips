package me.hacket.plugins.pgyer.api

/**
 * 蒲公英POST参数
 *
 * https://www.pgyer.com/doc/view/api#uploadApp
 *
 * Created by zengfansheng at 2021/8/2
 */
class PgyerRequest {

    public String _api_key // (必填) API Key
    public int buildInstallType = 1 // (选填)应用安装方式，值为(1,2,3，默认为1 公开安装)。1：公开安装，2：密码安装，3：邀请安装
    public String buildPassword // (选填) 设置App安装密码，密码为空时默认公开安装
    public String buildUpdateDescription // (选填) 版本更新描述，请传空字符串，或不传。
    public int buildInstallDate = 2 // (选填)是否设置安装有效期，值为：1 设置有效时间， 2 长期有效，如果不填写不修改上一次的设置

    public String buildInstallStartDate // (选填)安装有效期开始时间，字符串型，如：2018-01-01
    public String buildInstallEndDate // (选填)安装有效期结束时间，字符串型，如：2018-12-31
    public String buildChannelShortcut // (选填)所需更新的指定渠道的下载短链接，只可指定一个渠道，字符串型，如：abcd

    PgyerRequest(String _api_key) {
        this._api_key = _api_key
    }

    PgyerRequest(String _api_key, String buildUpdateDescription) {
        this._api_key = _api_key
        this.buildUpdateDescription = buildUpdateDescription
    }

    @Override
    String toString() {
        return Utils.sGson.toJson(this)
    }
}
