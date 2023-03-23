package com.example.mylibrary

import android.app.Application
import android.content.Context

/**
 *
 * Created by Admin at 2023/3/23
 */
interface ICrash {

    fun init(context: Application): String
}