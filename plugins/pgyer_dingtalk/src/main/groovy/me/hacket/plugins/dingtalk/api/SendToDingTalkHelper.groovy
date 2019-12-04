package me.hacket.plugins.dingtalk.api

import me.hacket.plugins.utils.Utils
import okhttp3.*

import java.util.concurrent.TimeUnit

/**
 * 自定义机器人接入
 *
 * https://developers.dingtalk.com/document/robots/custom-robot-access/title-nfv-794-g71#section-e4x-4y8-9k0
 */
class SendToDingTalkHelper {

    private static final String URL_DING_TALK_ROBOT = "https://oapi.dingtalk.com/robot/send?access_token="

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()

    static def sendToDingTalk(String accessToken, DingRequest req) {

        def url = URL_DING_TALK_ROBOT + accessToken

        String json = Utils.sGson.toJson(req)

        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"), json)

        Request request = new Request.Builder()
                .url(url)
//                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build()
        Response response
        try {
            response = client.newCall(request).execute()
            if (!response.isSuccessful()) {
                throw new RuntimeException("sendToDingTalk error code: " + response.code())
            } else {
                ResponseBody body = response.body()
                if (body == null) {
                    throw new RuntimeException("sendToDingTalk error body null. ")
                }
                String jsonString = body.string()
                Utils.log(" sendToDingTalk jsonString =" + jsonString)

                DingResponse dingResponse = Utils.sGson.fromJson(jsonString, DingResponse.class)
                Utils.log(" sendToDingTalk dingResponse =" + dingResponse)
                return dingResponse
            }
        } catch (IOException e) {
            e.printStackTrace()
            Utils.log("sendToDingTalk IOException: " + e.getMessage())
        }
        return null
    }

}