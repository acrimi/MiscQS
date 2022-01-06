package com.crimi.miscqs

import android.provider.Settings
import android.service.quicksettings.Tile

class HapticTileService : MiscQSTileService(Settings.System.HAPTIC_FEEDBACK_ENABLED) {
    override fun getNewValue() {
        intValue = intValue xor 1
    }

    override fun getState(): Int {
        return if (intValue == 1) {
            Tile.STATE_ACTIVE
        } else {
            Tile.STATE_INACTIVE
        }
    }

}