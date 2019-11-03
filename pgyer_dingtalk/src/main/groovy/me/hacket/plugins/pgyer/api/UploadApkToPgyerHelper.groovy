package me.hacket.plugins.pgyer.api

import me.hacket.plugins.utils.Utils
import okhttp3.*

import java.util.concurrent.TimeUnit

/**
 * https://www.pgyer.com/doc/view/api#uploadApp
 *
 * Created by zengfansheng at 2021/8/4
 */
class UploadApkToPgyerHelper {

    private static final String BASE_URL = "https://www.pgyer.com/apiv2/app/upload"

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()

    static PgyerResponse upload(PgyerRequest req, File file) throws IOException {

        HashMap<String, String> params = Utils.toMap(req)
        Utils.log("upload file=${file.getName()}, params= " + params)

        RequestBody fileBody = RequestBody.create(MediaType.parse("application/vnd.android.package-archive"), file)

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
///                .addPart(
//                        Headers.of("Content-Dispcosition", "form-data name=\"file\" filename=\"" + fileName + "\""),
//                        RequestBody.create(MEDIA_TYPE_PNG, file))
//                .addPart(
//                        Headers.of("Content-Disposition", "form-data name=\"imagetype\""),
//                        RequestBody.create(null, imageType))
//                .addPart(
//                        Headers.of("Content-Disposition", "form-data name=\"userphone\""),
//                        RequestBody.create(null, userPhone))
                .addFormDataPart("file", file.getName(), fileBody)

        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey()
                String value = entry.getValue()
                if (value != null && !value.isEmpty()) {
                    Utils.log("upload addFormDataPart key=$key, value=$value")
                    builder.addFormDataPart(key, value)
                }
            }
        }

        RequestBody requestBody = builder.build()

        Request request = new Request.Builder()
                .url(BASE_URL)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(requestBody)
                .build()

        Response response = client.newCall(request).execute()
        if (!response.isSuccessful()) {
            throw new RuntimeException("upload error code: " + response.code())
        } else {
            ResponseBody body = response.body()
            if (body == null) {
                throw new RuntimeException("upload error body null. ")
            }
            String jsonString = body.string()
            Utils.log(" upload jsonString =" + jsonString)

            PgyerResponse pgyerResponse = Utils.sGson.fromJson(jsonString, PgyerResponse.class)
            Utils.log(" upload pgyerResponse =" + pgyerResponse)
            return pgyerResponse
        }
    }

}