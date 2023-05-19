package ati.sn0w_w01f.hms3

import android.app.Application
import com.google.android.material.color.DynamicColors

class DynamicTheme : Application(){
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}