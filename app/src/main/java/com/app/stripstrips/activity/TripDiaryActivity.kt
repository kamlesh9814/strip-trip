package com.app.stripstrips.activity

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.net.toUri
import coil.load
import com.app.stripstrips.R
import com.app.stripstrips.base.MyApplication
import com.app.stripstrips.databinding.ActivityDiaryBinding
import com.app.stripstrips.model.postDetailsApi.PostDetailsRequest
import com.app.stripstrips.network.Repository
import com.app.stripstrips.utils.ConstantsVar
import com.app.stripstrips.utils.Status
import com.app.stripstrips.viewModel.ViewModel
import com.app.stripstrips.viewModel.ViewModelFactory
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import java.text.SimpleDateFormat
import java.util.*

class TripDiaryActivity : AppCompatActivity() {
    lateinit var binding: ActivityDiaryBinding
    var post_id = ""
    var postId = ""
    var postName = ""
    var postDescription = ""
    var location = ""
    var date = ""
    var thumbImage = ""
    var image = ""
    var postImage:Uri? = null
    var video = ""
    var diaryId= ""
    var postPrivacy = ""
    var latitude = 0.0
    var longitude = 0.0
    private var simpleExoPlayer: SimpleExoPlayer? = null


    private val viewModel by viewModels<ViewModel>
    { ViewModelFactory(Application(), Repository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onClick()
        observer()
        intentCall()
        viewModel.userPostDetailsData(PostDetailsRequest(post_id),this)
    }


    private fun intentCall() {
        post_id = intent.getStringExtra("post_ids")!!
        Log.e("TAG", "onBindViewHolder: $post_id")
    }

    private fun onClick() {
        binding.backIV.setOnClickListener {
            finish()
        }
        binding.editLayout.setOnClickListener {
            val intent = Intent(this@TripDiaryActivity, EditPostActivity::class.java)
            intent.putExtra("post_ids", post_id)
            intent.putExtra("diaryId", diaryId)
            intent.putExtra("postName",postName)
            intent.putExtra("postDescription",postDescription)
            intent.putExtra("location",location)
            intent.putExtra("latitude",latitude.toString())
            intent.putExtra("longitude",longitude.toString())
            intent.putExtra("postPrivacy",postPrivacy)
            intent.putExtra("date",date)
            intent.putExtra("image",image)
            intent.putExtra("video",video)
            intent.putExtra("thumbImage",thumbImage)
            intent.putExtra("postImage",postImage.toString())
            startActivity(intent)
        }
    }

    private fun setupExoplayer(urlVideo: String) {
        releasePlayer()
        binding.exoPlayersViews.visibility = View.VISIBLE
        binding.ivItem.visibility = View.GONE
        simpleExoPlayer = SimpleExoPlayer.Builder(this).build()
        binding.exoPlayersViews.player = simpleExoPlayer
        val mediaUri = Uri.parse(urlVideo)
        val mediaItem: MediaItem = MediaItem.fromUri(mediaUri)
        simpleExoPlayer!!.addMediaItem(mediaItem)
        simpleExoPlayer!!.prepare()
        simpleExoPlayer!!.pause()
        binding.exoPlayersViews.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
        simpleExoPlayer!!.playWhenReady = true
        simpleExoPlayer!!.play()
        simpleExoPlayer!!.seekBack()
        simpleExoPlayer!!.repeatMode = Player.REPEAT_MODE_ALL
        simpleExoPlayer!!.videoScalingMode
        simpleExoPlayer!!.pauseAtEndOfMediaItems
        simpleExoPlayer!!.isLoading

        simpleExoPlayer!!.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_BUFFERING) {

                    // hide controls
                    // show progress
                } else {
                    // hide progress and controls

                }
            }
        })

    }

    private fun releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer!!.release()
            simpleExoPlayer!!.stop()
            simpleExoPlayer = null
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    override fun onPause() {
        super.onPause()
        if (simpleExoPlayer != null) {
            simpleExoPlayer!!.playWhenReady = false

        }
    }

    override fun onResume() {
        super.onResume()
        if (ConstantsVar.ISBACK == "1"){
            ConstantsVar.ISBACK = "0"
            finish()
        }else{
            if (simpleExoPlayer != null) {
                simpleExoPlayer!!.playWhenReady = true

            }
        }

    }

    private fun observer() {
        viewModel.postDetailApi.observe(this@TripDiaryActivity) {
            when (it.status) {
                Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    it.data!!.data
                    postName = it.data.data!!.postName.toString()
                    postDescription = it.data.data.postDescription.toString()
                    location = it.data.data.location.toString()
                    latitude = it.data.data.latitude!!.toDouble()
                    longitude = it.data.data.longitude!!.toDouble()
                    location = it.data.data.location.toString()
                    date = it.data.data.postDate.toString()
                    postName = it.data.data.postName.toString()
                    diaryId = it.data.data.diaryId.toString()
                    thumbImage = it.data.data.thumbImage.toString()
                    postPrivacy = it.data.data.postPrivacy.toString()
                    binding.postNameTv.text = it.data.data.postName
                    binding.PostDescriptionTv.text = it.data.data.postDescription
                    binding.tvLocation.text = it.data.data.location
                    binding.dateTv.text = it.data.data.postDate
                    video = it.data.data.postImage.toString()
                    image = it.data.data.postImage.toString()
                    postImage = it.data.data.postImage!!.toUri()
                    if (video != "" && video.contains(".mp4") || video.contains(".3gp") || video.contains(
                            ".mov"
                        )
                    ) {
                        binding.ivItem.load(it.data.data.thumbImage) {
                            error(R.drawable.human)
                        }
                        setupExoplayer(video)
                    } else {
                        binding.exoPlayersViews.visibility = View.GONE
                        binding.ivItem.visibility = View.VISIBLE
                        binding.ivItem.load(image)
                    }
                    Log.e("TAG", "onBindViewHolder: ${it.data.data.postImage}")
                    binding.dateTv.text = convertDateFormat(it.data.data.postDate.toString())

                }
                Status.LOADING -> {
                    MyApplication.showLoader(this)

                }
                Status.ERROR -> {
                    MyApplication.hideLoader()
                    //Log.e("tag", it.message!!)
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertDateFormat(date: String): String {
        val dates: Date? = SimpleDateFormat("yyyy-MM-dd").parse(date)
        return SimpleDateFormat("MMM dd, yyyy").format(dates!!)
    }

/*    private fun getLoadControl(): LoadControl {
        val builder: DefaultLoadControl.Builder =
            DefaultLoadControl.Builder()
        builder.setBufferDurationsMs(25000, 50000, 100, 300)
        return builder.createDefaultLoadControl()
    }*/
}