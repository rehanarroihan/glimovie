package com.multazamgsd.glimovie

import android.app.Application
import android.content.Context
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    companion object {
        val networkFlipperPlugin = NetworkFlipperPlugin()
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            SoLoader.init(this, false)
            val flipperClient = AndroidFlipperClient.getInstance(this)
            flipperClient.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            flipperClient.addPlugin(DatabasesFlipperPlugin(this))
            flipperClient.addPlugin(networkFlipperPlugin)
            flipperClient.start()
        }
    }
}