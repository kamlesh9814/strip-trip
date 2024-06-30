package com.app.stripstrips.activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.app.stripstrips.R
import com.app.stripstrips.databinding.ActivityDetailsBinding
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout

@Suppress("DEPRECATION")
class TripDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailsBinding
    var post_id = ""
    var postName = ""
    var postDescription = ""
    var location = ""
    var date = ""
    var post_Image = ""
    var thumbVideo = ""
    var tripImage = ""
    var thumbImage = ""
    var simpleExoPlayer: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        getIntentData()
        onClick()
        setContentView(binding.root)

    }

    private fun getIntentData() {
        post_id = intent.getStringExtra("post_id")!!
        Log.e("TAG", "postId: $post_id", )
        postName = intent.getStringExtra("postName")!!
        postDescription = intent.getStringExtra("postDescription")!!
        location = intent.getStringExtra("location")!!
        date = intent.getStringExtra("updatedAt")!!
        post_Image = intent.getStringExtra("postImage")!!
        thumbVideo = intent.getStringExtra("thumbImage")!!//thumbnail
        Log.e("TripDetailsActivity", "getIntentData: $tripImage &&& $thumbImage")

        binding.dateTv.text = date
        binding.postNameTv.text = postName
        binding.PostDescriptionTv.text = postDescription
        binding.tvLocation.text = location

        if (tripImage != "" && tripImage.contains(".mp4") || tripImage.contains(".3gp") ||  tripImage.contains(".mov")) {
//video
            Log.e("ImageLoad", "getIntentData: if caseee ")
            Log.e("ImageLoad", "emptyImage: $tripImage")
            binding.ivItem.load(thumbImage) {
                error(R.drawable.human)
            }
            setupExoplayer(tripImage)

        } else if (post_Image.isNotEmpty()) {
            //image

            if (post_Image != "" && post_Image.contains(".mp4") || post_Image.contains(".3gp") ||  post_Image.contains(".mov")) {
                binding.ivItem.load(thumbImage) {
                    error(R.drawable.human)
                }
                setupExoplayer(post_Image)
            } else {

                binding.exoPlayersViews.visibility = View.GONE
                binding.ivItem.visibility = View.VISIBLE
                Log.e("ImageLoad", "getIntentData: else if caseee ")

                Log.e("ImageLoad", "getIntentData: $post_Image")
                binding.ivItem.load(post_Image) {
                    error(R.drawable.human)
                }
            }
        }
    }

    private fun onClick() {
        binding.backIV.setOnClickListener {
            finish()
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
        if (simpleExoPlayer != null) {
            simpleExoPlayer!!.playWhenReady = true

        }
    }

/*    private fun getLoadControl(): LoadControl {
        val builder: DefaultLoadControl.Builder =
            DefaultLoadControl.Builder()
        builder.setBufferDurationsMs(25000, 50000, 100, 300)
        return builder.createDefaultLoadControl()
    }*/
}