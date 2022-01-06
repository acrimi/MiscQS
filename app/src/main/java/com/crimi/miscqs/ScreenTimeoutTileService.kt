package com.crimi.miscqs

import android.content.SharedPreferences
import android.provider.Settings
import android.service.quicksettings.Tile
import androidx.preference.PreferenceManager

class ScreenTimeoutTileService : MiscQSTileService(Settings.System.SCREEN_OFF_TIMEOUT) {
    private val preferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }
    private var previousTimeout: Long
        get() = preferences.getLong("previousTimeout", -1L)
        set(value) = preferences.edit().putLong("previousTimeout", value).apply()
    private val oneWeek = 7 * 24 * 60 * 60 * 1000L

    override fun getNewValue() {
        if (previousTimeout == -1L) {
            previousTimeout = longValue
        }
        if (longValue == previousTimeout) {
            longValue = oneWeek
        } else {
            longValue = previousTimeout
        }
    }

    override fun getState(): Int {
        return if (longValue == oneWeek) {
            Tile.STATE_INACTIVE
        } else {
            Tile.STATE_ACTIVE
        }
    }
}