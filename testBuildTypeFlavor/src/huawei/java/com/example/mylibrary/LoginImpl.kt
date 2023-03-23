package com.example.mylibrary

import android.util.Log

/**
 *
 * Created by Admin at 2023/3/23
 */
class LoginImpl : ILogin {
    override fun login(name: String, pwd: String) {
        Log.d("hacket", "hw login $name $pwd.")
    }
}