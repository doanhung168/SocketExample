package com.hung_nguyen.socketexample

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSION_CODE = 123


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissionsIfNecessary()

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            val startRecordIntent = Intent(this, RecordService::class.java)
            startRecordIntent.action = RecordService.START_RECORD_ACTION
            startService(startRecordIntent)
        }

        findViewById<Button>(R.id.btnStop).setOnClickListener {
            val stopRecordIntent = Intent(this, RecordService::class.java)
            stopRecordIntent.action = RecordService.STOP_RECORD_ACTION
            startService(stopRecordIntent)
        }

    }


    private fun requestPermissionsIfNecessary() {
        val permissionsToRequest = mutableListOf<String>()

        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.add(android.Manifest.permission.RECORD_AUDIO)
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissions(permissionsToRequest.toTypedArray(), REQUEST_PERMISSION_CODE)
        }
    }

}