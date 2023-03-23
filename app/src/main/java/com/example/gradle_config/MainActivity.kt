package com.example.gradle_config

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.multidex.BuildConfig
import com.example.mylibrary.LoginImpl
import com.example.mylibrary.crash.CrashImpl

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val init = CrashImpl().init(application)
        val tvInfo = findViewById<TextView>(R.id.tv_info)
        tvInfo.text = "${BuildConfig.BUILD_TYPE}-${BuildConfig.FLAVOR}- $init"

        findViewById<Button>(R.id.btn_login).setOnClickListener {
            val name = findViewById<EditText>(R.id.et_name).text.toString()
            val pwd = findViewById<EditText>(R.id.et_pwd).text.toString()

            LoginImpl().login(name, pwd)
        }
    }
}