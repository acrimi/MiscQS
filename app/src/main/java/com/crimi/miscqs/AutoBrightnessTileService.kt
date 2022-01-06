package com.crimi.miscqs

import android.provider.Settings

class AutoBrightnessTileService : MiscQSTileService() {
    override fun onClick() {
        if (!checkSettingsPermission()) {
            return
        }
        val currentSetting = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE)
        val newSetting = if (currentSetting == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
        } else {
            Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
        }
        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, newSetting)
    }
}