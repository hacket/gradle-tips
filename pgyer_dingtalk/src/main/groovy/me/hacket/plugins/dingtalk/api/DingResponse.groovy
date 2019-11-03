package me.hacket.plugins.dingtalk.api

class DingResponse {

    public int errcode
    public String errmsg

    boolean isSuccessFul() {
        return errcode == 0
    }

    @Override
    String toString() {
        return Utils.sGson.toJson(this)
    }
}