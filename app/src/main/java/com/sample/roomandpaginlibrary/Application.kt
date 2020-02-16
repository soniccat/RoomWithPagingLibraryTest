package com.sample.roomandpaginlibrary

import android.app.Application
import com.sample.roomandpaginlibrary.model.DbManager

class Application: Application() {
    companion object {
        lateinit var Instance: com.sample.roomandpaginlibrary.Application
    }

    lateinit var db: DbManager

    init {
        Instance = this
    }

    override fun onCreate() {
        super.onCreate()
        db = DbManager(this)
    }
}