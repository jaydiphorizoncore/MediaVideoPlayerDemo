package com.example.mediavideoplayerdemo

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.example.mediavideoplayerdemo.databinding.ActivityAudioBinding
import java.io.IOException

class AudioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioBinding
    private var mediaPlayer = MediaPlayer()
    private lateinit var seekbar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_audio)

        //val audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
        val audio = Uri.parse("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

        try {
            mediaPlayer.setDataSource(this, audio)
            mediaPlayer.prepare() // Prepare the MediaPlayer after setting the data source
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //mediaPlayer = MediaPlayer.create(this, R.raw.school_bell)

        seekbar = binding.seekBar
        setupSeekBar()
        binding.btnPlay.setOnClickListener {
            playAudio()
        }

        binding.btnStop.setOnClickListener {
            stopAudio()
        }

        binding.btnPause.setOnClickListener {
            pauseAudio()
        }
    }

    private fun pauseAudio() {
        mediaPlayer.pause()
    }

    private fun playAudio() {
        mediaPlayer.start()
    }

    private fun stopAudio() {
        mediaPlayer.stop()
        // it is again need to be prepared
        // for the next instance of playback
        mediaPlayer.prepare()
    }

    // This method initializes the SeekBar by setting its maximum value to the duration of the audio.
    //It also sets up a Runnable named updateSeekBar to continuously update the progress of the SeekBar
    // based on the current position of the MediaPlayer.
    // It sets an OnSeekBarChangeListener to handle user interactions with the SeekBar.
    // When the user drags the SeekBar, it seeks to the selected position in the audio.

    private fun setupSeekBar() {
        seekbar.max = mediaPlayer.duration

        val updateSeekBar = Runnable {
            seekbar.postDelayed({ updateSeekBar() }, 1000)
        }

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        updateSeekBar.run()
    }


    /*This method is responsible for updating the progress of the SeekBar based on the current position of
      the MediaPlayer. It does this by setting the SeekBar progress and posting a delayed callback to itself
      every 1000 milliseconds (1 second).*/
    private fun updateSeekBar() {
        seekbar.progress = mediaPlayer.currentPosition
        seekbar.postDelayed({ updateSeekBar() }, 1000)
        binding.tvPass.text = "${mediaPlayer.currentSecond} sec"
        val diff = mediaPlayer.seconds - mediaPlayer.currentSecond
        binding.tvDue.text = "$diff sec"

    }

    /*   override fun onDestroy() {
           super.onDestroy()
           mediaPlayer.release()
       }*/

    // Creating an extension property to get media player current position in seconds
    private val MediaPlayer.currentSecond: Int
        get() {
            return this.currentPosition / 1000
        }

    // Creating an extension property to get the media player time duration in seconds
    private val MediaPlayer.seconds: Int
        get() {
            return this.duration / 1000
        }
}

