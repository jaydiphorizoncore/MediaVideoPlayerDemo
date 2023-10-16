package com.example.mediavideoplayerdemo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.mediavideoplayerdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val GALLERY_REQUEST_CODE = 103
    private val STORAGE_PERMISSION = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding.videoView)
        binding.videoView.setMediaController(mediaController)

        videoPlayInRaw_Uri_Path_Link()
        clickListener()
    }

    private fun videoPlayInRaw_Uri_Path_Link() {
        // binding.videoView.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.video))
        val videoPath = "android.resource://" + packageName + "/" + R.raw.video
        binding.videoView.setVideoPath(videoPath)
        // binding.videoView.setVideoURI(Uri.parse("https://file-examples.com/storage/fec36b918d65009119ed030/2017/04/file_example_MP4_640_3MG.mp4"))
        binding.videoView.setOnPreparedListener {
            Log.i(
                "TAG", "Duration = " +
                        binding.videoView.duration
            )
        }
        binding.videoView.start()
    }

    private fun clickListener() {
        binding.ChoseVideo.setOnClickListener {
            requestStoragePermission()
        }
        binding.btnAudioPlayer.setOnClickListener {
            val i = Intent(this, AudioActivity::class.java)
            startActivity(i)
        }
    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), STORAGE_PERMISSION
            )
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent()
        galleryIntent.type = "video/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                } else {
                    Toast.makeText(this, "Storage permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY_REQUEST_CODE && data != null) {
            val selectedVideoUri: Uri? = data.data!!
            binding.videoView.setVideoURI(selectedVideoUri)
            binding.videoView.start()

        }
    }
}