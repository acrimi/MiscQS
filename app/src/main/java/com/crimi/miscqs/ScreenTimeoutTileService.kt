package com.crimi.miscqs

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.provider.Settings
import android.service.quicksettings.Tile
import kotlin.math.min

class ScreenTimeoutTileService : MiscQSTileService(Settings.System.SCREEN_OFF_TIMEOUT) {
    private val defaultMaxTimeout = 24 * 60 * 60 * 1000L // 1d
    private val timeouts = arrayOf(
        15 * 1000L, // 15s
        30 * 1000L, // 30s
        60 * 1000L, // 1m
        2 * 60 * 1000L, // 2m
        5 * 60 * 1000L, // 5m
        10 * 60 * 1000L, // 10m
        20 * 60 * 1000L, // 20m
        30 * 60 * 1000L, // 30m
        60 * 60 * 1000L, // 1h
        5 * 60 * 60 * 1000L, // 5h
    )
    private val policyManager: DevicePolicyManager by lazy {
        getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }
    private val maxTimeout: Long
        get() = policyManager.getMaximumTimeToLock(null).takeUnless { it == 0L } ?: defaultMaxTimeout

    override fun getNewValue() {
        when (val current = longValue) {
            timeouts.last() -> {
                longValue = maxTimeout
            }
            maxTimeout -> {
                longValue = timeouts.first()
            }
            else -> {
                for (timeout in timeouts) {
                    if (timeout > current) {
                        longValue = min(timeout, maxTimeout)
                        break
                    }
                }
            }
        }
    }

    override fun getState(): Int {
        return if (longValue < maxTimeout) {
            Tile.STATE_ACTIVE
        } else {
            Tile.STATE_INACTIVE
        }
    }

    override fun getSubtitle(): CharSequence {
        return longValue.div(24 * 60 * 60 * 1000).takeUnless { it == 0L }?.toString()?.plus("d") ?:
            longValue.div(60 * 60 * 1000).takeUnless { it == 0L }?.toString()?.plus("h") ?:
            longValue.div(60 * 1000).takeUnless { it == 0L }?.toString()?.plus("m") ?:
            longValue.div(1000).toString() + "s"
    }
}