package com.skm.imputetasks

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.skm.imputetasks.databinding.ActivityTask2Binding

class Task2Activity : AppCompatActivity() {
    lateinit var binding: ActivityTask2Binding
    val schemas = arrayListOf("http", "https", "ws", "wss")
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTask2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playButton.apply {
            isEnabled = false
            alpha = 0.5f
        }

        binding.clearButton.apply {
            isEnabled = false
            alpha = 0.5f
        }

        binding.etUrl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                editable?.let {
                    if (it.trim().isNotEmpty()) {
                        binding.playButton.apply {
                            isEnabled = schemas.contains(it.trim().split("://")[0])
                            alpha = if (schemas.contains(it.trim().split("://")[0])) {
                                1f
                            } else {
                                0.5f
                            }
                        }
                        binding.clearButton.apply {
                            isEnabled = true
                            alpha = 1f
                        }
                    } else {
                        binding.clearButton.apply {
                            isEnabled = false
                            alpha = 0.5f
                        }
                    }
                }
            }
        })

        binding.clearButton.setOnClickListener {
            binding.etUrl.text.clear()
            binding.playButton.apply {
                isEnabled = false
                alpha = 0.5f

            }
        }

        binding.playButton.setOnClickListener {
            val url = binding.etUrl.text.toString().trim()
            if (url.isNotEmpty() && schemas.contains(url.split("://")[0])) {
                this.currentFocus?.let { view ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                }
                val videoView = binding.videoView
                videoView.visibility = View.VISIBLE

                val uri: Uri =
                    Uri.parse(
                        url
                    )

                videoView.setVideoURI(uri)

                MediaController(this).apply {
                    setAnchorView(videoView)
                    setMediaPlayer(videoView)
                    videoView.setMediaController(this)
                }
                videoView.start()
            } else {
                Toast.makeText(applicationContext, "Video Url is not supported", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}