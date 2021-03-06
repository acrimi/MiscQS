package com.crimi.miscqs

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.service.quicksettings.TileService
import android.widget.Toast

abstract class MiscQSTileService(
    private val settingName: String
) : TileService() {
    private val pollInterval = 1000L
    private val handler = Handler(Looper.getMainLooper())
    private val pollingListener = object : Runnable {
        override fun run() {
            val state = getState()
            if (state != qsTile.state) {
                qsTile.state = state
                qsTile.updateTile()
            }
            handler.postDelayed(this, pollInterval)
        }
    }

    protected var intValue: Int
        get() = Settings.System.getInt(contentResolver, settingName)
        set(value) {
            Settings.System.putInt(contentResolver, settingName, value)
        }
    protected var longValue: Long
        get() = Settings.System.getLong(contentResolver, settingName)
        set(value) {
            Settings.System.putLong(contentResolver, settingName, value)
        }

    private fun checkSettingsPermission(): Boolean {
        return if (Settings.System.canWrite(this)) {
            true
        } else {
            startActivityAndCollapse(
                Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    data = (Uri.parse("package:${packageName}"))
                }
            )
            Toast.makeText(this, "Please give MiscQS permission to modify system settings", Toast.LENGTH_SHORT).show()
            false
        }
    }

    protected abstract fun getNewValue()

    protected abstract fun getState(): Int

    override fun onClick() {
        if (checkSettingsPermission()) {
            getNewValue()
            qsTile.state = getState()
            qsTile.updateTile()
        }
    }

    override fun onTileAdded() {
        checkSettingsPermission()
    }

    override fun onStartListening() {
        handler.postDelayed(pollingListener, pollInterval)
    }

    override fun onStopListening() {
        handler.removeCallbacksAndMessages(null)
    }
}