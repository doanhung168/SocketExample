package com.hung_nguyen.socketexample

import android.app.Service
import android.content.Intent
import android.media.MediaCodec
import android.media.MediaFormat
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class RecordService : Service() {

    private lateinit var mMediaProjectionManager: MediaProjectionManager

    private lateinit var mMediaProjection: MediaProjection
    private lateinit var mMediaCodec: MediaCodec
    private var mIsRecording = false

    private val mRecordPermissionObserver = Observer<Intent?> { intent ->
        if(intent != null) {
            startRecording(intent)
        } else {
            Toast.makeText(this, "Deny recording action", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val TAG = "RecordService"
        const val START_RECORD_ACTION = "start_record_action"
        const val STOP_RECORD_ACTION = "stop_record_action"
        val RecordIntentLiveData = MutableLiveData<Intent?>()
    }

    override fun onBind(intent: Intent?) = null

    override fun onCreate() {
        super.onCreate()
        RecordIntentLiveData.observeForever(mRecordPermissionObserver)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when(intent?.action) {
            START_RECORD_ACTION -> {
                startActivity(Intent(this, RecordPermissionActivity::class.java))
            }
            STOP_RECORD_ACTION -> {
                mIsRecording = false
            }
            null -> {
                Log.i(TAG, "onStartCommand: intent is null")
                stopSelfResult(startId)
            }
        }
        return START_NOT_STICKY
    }

    private fun startRecording(data: Intent) {
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        val screenDensity = displayMetrics.densityDpi

        mMediaProjection = mMediaProjectionManager.getMediaProjection(AppCompatActivity.RESULT_OK, data)

        try {
            val format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, screenWidth, screenHeight)
            mMediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
            mMediaCodec.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
            mMediaCodec.start()

            val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "ScreenRecordings")
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val outputFile = File(directory, "screen_record.mp4")
            val outputStream = BufferedOutputStream(FileOutputStream(outputFile))

            val bufferInfo = MediaCodec.BufferInfo()
            while (mIsRecording) {
                val outputBufferIndex = mMediaCodec.dequeueOutputBuffer(bufferInfo, -1)
                if (outputBufferIndex >= 0) {
                    val outputBuffer = mMediaCodec.getOutputBuffer(outputBufferIndex)
                    outputStream.write(outputBuffer?.array())
                    mMediaCodec.releaseOutputBuffer(outputBufferIndex, false)
                }
            }

            outputStream.flush()
            outputStream.close()

            mMediaCodec.stop()
            mMediaCodec.release()
            mMediaProjection.stop()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RecordIntentLiveData.removeObserver(mRecordPermissionObserver)
    }

}