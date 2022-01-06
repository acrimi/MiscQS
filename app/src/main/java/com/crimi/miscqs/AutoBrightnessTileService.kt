package com.crimi.miscqs

import android.provider.Settings
import android.service.quicksettings.Tile

class AutoBrightnessTileService : MiscQSTileService(Settings.System.SCREEN_BRIGHTNESS_MODE) {

    override fun getNewValue() {
        intValue = if (intValue == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
        } else {
            Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
        }
    }

    override fun getState(): Int {
        return if (intValue == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
            Tile.STATE_ACTIVE
        } else {
            Tile.STATE_INACTIVE
        }
    }
}