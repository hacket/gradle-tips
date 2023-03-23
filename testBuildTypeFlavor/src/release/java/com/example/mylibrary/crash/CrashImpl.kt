package com.example.mylibrary.crash

import android.content.Context
import android.util.Log
import com.example.mylibrary.ICrash

/**
 *
 * Created by Admin at 2023/3/23
 */
class CrashImpl : ICrash {
    override fun init(context: Context): String {
        Log.i("hacket", "Firebase crash")
        return "Firebase"
    }
}