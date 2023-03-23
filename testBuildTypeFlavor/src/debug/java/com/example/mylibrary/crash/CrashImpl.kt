package com.example.mylibrary.crash

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import android.util.Log
import com.example.mylibrary.ICrash
import com.tencent.bugly.crashreport.CrashReport
import me.hacket.test.BuildConfig

class CrashImpl : ICrash {

    companion object {
        @JvmStatic
        fun getInstance(): ICrash {
            return CrashImpl()
        }
    }

    override fun init(application: Application): String {
        val isDebug: Boolean = true
        val strategy = CrashReport.UserStrategy(application)
        strategy.appChannel = "default" // 设置渠道，只是测试包用

        strategy.appPackageName = application.packageName // App的包名

        strategy.appVersion = getAppVersionName(application) //App的版本

        val delay = if (isDebug) 1000L else 10000L
        strategy.appReportDelay = delay // 默认10s，改为20s

        // 在开发测试阶段，可以在初始化Bugly之前通过以下接口把调试设备设置成“开发设备”。

        CrashReport.setIsDevelopmentDevice(application, isDebug)
        CrashReport.setBuglyDbName("allo_${BuildConfig.BUILD_TYPE}_bugly.db")

        CrashReport.initCrashReport(application, "7c2dd8bad3", isDebug, strategy)

        Log.d("hacket", "init bugly.")
        return "bugly"
    }

    private fun getAppVersionName(context: Context): String? {
        return getAppVersionName(context, context.packageName)
    }

    private fun getAppVersionName(context: Context, packageName: String?): String? {
        return if (TextUtils.isEmpty(packageName)) null else try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName!!, 0)
            pi?.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }
}