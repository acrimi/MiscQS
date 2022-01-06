package com.crimi.miscqs

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.service.quicksettings.TileService
import android.widget.Toast

open class MiscQSTileService : TileService() {

    protected fun checkSettingsPermission(): Boolean {
        return if (Settings.System.canWrite(this)) {
            true
        } else {
            startActivity(
                Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    data = (Uri.parse("package:${packageName}"))
                }
            )
            Toast.makeText(this, "Please give MiscQS permission to modify system settings", Toast.LENGTH_SHORT).show()
            false
        }
    }

    override fun onTileAdded() {
        checkSettingsPermission()
    }
}