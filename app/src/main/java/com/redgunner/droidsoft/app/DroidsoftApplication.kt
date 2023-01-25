package com.redgunner.droidsoft.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import androidx.fragment.app.activityViewModels

@HiltAndroidApp
class DroidsoftApplication : Application() {

    override fun onCreate() {
        super.onCreate()

   }
}