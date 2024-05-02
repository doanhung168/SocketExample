package com.hung_nguyen.socketexample

import android.app.Activity
import android.content.Intent
import android.media.projection.MediaProjectionManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts

class RecordPermissionActivity : AppCompatActivity() {

    private val mRecordPermissionRequest
        = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            RecordService.RecordIntentLiveData.value = result.data
        } else {
            RecordService.RecordIntentLiveData.value = null
        }
        finish()
    }

    private fun requestRecordPermission() {
        val mediaProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        mRecordPermissionRequest.launch(mediaProjectionManager.createScreenCaptureIntent())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_NoDisplay)
        requestRecordPermission()
    }
}