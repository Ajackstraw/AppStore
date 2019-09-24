package com.jackstraw.market

import android.app.Application
import android.content.Context

/**
 * @date 2018/10/26
 * @author pink-jackstraw
 * @describe
 */
class JApplication : Application() {

    private val tag = "JApplication"

    companion object {
        var appContext : Context? = null
    }

    init {
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}
