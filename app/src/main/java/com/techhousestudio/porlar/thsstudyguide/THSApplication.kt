package com.techhousestudio.porlar.thsstudyguide

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber
import timber.log.Timber.DebugTree


class THSApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
        AndroidThreeTen.init(this)
    }
}