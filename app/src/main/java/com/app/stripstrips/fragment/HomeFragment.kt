@file:Suppress("DEPRECATION")

package com.app.stripstrips.fragment
import android.R
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.app.stripstrips.activity.TripsDetailsLocationActivity
import com.app.stripstrips.adapters.HomeFragmentAdapter
import com.app.stripstrips.base.MyApplication
import com.app.stripstrips.commonInterface.OnClickLishner
import com.app.stripstrips.databinding.FragmentHomeBinding
import com.app.stripstrips.model.homeApi.HomeDataItem
import com.app.stripstrips.model.homeApi.HomeRequest
import com.app.stripstrips.model.likeDislikeApi.LikeDislikeRequest
import com.app.stripstrips.network.Repository
import com.app.stripstrips.sharedPreferences.AppPrefs
import com.app.stripstrips.utils.ConstantsVar
import com.app.stripstrips.utils.smoothSnapToPosition
import com.app.stripstrips.viewModel.ViewModel
import com.app.stripstrips.viewModel.ViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
class HomeFragment : Fragment() {
    var TAG = HomeFragment::class.java.toString()
    lateinit var binding: FragmentHomeBinding
    var pos: Int = -1
    var imageCollection: ArrayList<HomeDataItem>? = ArrayList()
    private var homeFragmentAdapter: HomeFragmentAdapter? = null
    private var layoutManager: LinearLayoutManager? = null
    private var simpleExoPlayer: SimpleExoPlayer? = null
    var videoView: PlayerView? = null
    var stripImage: ImageView? = null
    var snapHelper: SnapHelper? = null
    var oldListPos = -1
    var currentPlayerPos = -1
    private val viewModel by viewModels<ViewModel>
    { ViewModelFactory(Application(), Repository()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        observerLikeDislike()
        listeners()
        internetCheck()
        Log.e("token", AppPrefs(requireActivity()).getToken("token")!!)
        return binding.root

    }

    private fun internetCheck() {
        if (ConstantsVar.isNetworkAvailable(requireContext())) {
            viewModel.dataHomePage(HomeRequest("1", "1000"),requireContext())
            observer()

        } else {
            Toast.makeText(requireActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    private val isLikeOrDisLike: OnClickLishner = object : OnClickLishner {
        override fun onClick(position: Int, s: String) {
            pos = position
            viewModel.likeDislikeDetailsApiData(
                LikeDislikeRequest(imageCollection?.get(position)?.postId, s)
            )
        }
    }

    private fun listeners() {
        val mLayoutManager = LinearLayoutManager(requireActivity())
        binding.TripsTabSharedRecyclerView.layoutManager = mLayoutManager

        binding.TripsTabSharedRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {


            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (imageCollection?.isNotEmpty()!!) {
                    val adapterPosition = recyclerView.layoutManager!!.getPosition(
                        snapHelper?.findSnapView(recyclerView.layoutManager)!!
                    )

                    Log.e("HOMEFRAGMENT", "onScrolled: $adapterPosition")

                    if (adapterPosition != oldListPos) {
                        oldListPos = adapterPosition
                        currentPlayerPos = adapterPosition
                        releasePlayer()
                        setupPlayer(currentPlayerPos)
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val scrollOffset = recyclerView.computeVerticalScrollOffset()
                val height = recyclerView.height
                val adapterPosition = scrollOffset / height
                Log.e("ADAPTERPOS", "onScrollStateChanged: $adapterPosition")
                binding.TripsTabSharedRecyclerView.suppressLayout(true)
            }
        })
    }

    private fun adapterCall() {
        homeFragmentAdapter = HomeFragmentAdapter(
            requireActivity(), imageCollection, allDataCallBack, isLikeOrDisLike
        )
        binding.TripsTabSharedRecyclerView.adapter = homeFragmentAdapter
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.TripsTabSharedRecyclerView.layoutManager = NoScrollLinearLayoutManager(context)
        (binding.TripsTabSharedRecyclerView.layoutManager as NoScrollLinearLayoutManager).disableScrolling()

        binding.TripsTabSharedRecyclerView.layoutManager = layoutManager

        snapHelper = PagerSnapHelper()
        binding.TripsTabSharedRecyclerView.layoutManager = layoutManager
        snapHelper?.attachToRecyclerView(binding.TripsTabSharedRecyclerView)
        binding.TripsTabSharedRecyclerView.suppressLayout(true)

    }

    private val allDataCallBack = object : HomeFragmentAdapter.AdapterOnClick {
        override fun onClick(position: Int, exoPlayersViews: StyledPlayerView) {

        }
    }

    fun setupPlayer(position: Int) {
        val videoUrl = imageCollection!![position].postImage
        val layout: View? = layoutManager!!.findViewByPosition(position)
        videoView = layout?.findViewById(com.app.stripstrips.R.id.exoPlayersViews)
        stripImage = layout?.findViewById(com.app.stripstrips.R.id.imageView)

        if (videoUrl!!.contains(".mp4") || videoUrl.contains(".3gp") || videoUrl.contains(".mov")) {
            /** play video code **/

            releasePlayer()
            Glide.with(requireContext())
                .load(imageCollection!![position].thumbImage)
                .into(stripImage!!)

            videoView?.visibility = View.GONE
            stripImage?.visibility = View.VISIBLE
            Log.e(TAG, "setupPlayer:VIDEO $videoUrl")
            simpleExoPlayer = SimpleExoPlayer.Builder(requireActivity()).build()
            videoView!!.player = simpleExoPlayer

            val mediaUri = Uri.parse(imageCollection!![position].postImage)
            val mediaItem: MediaItem = MediaItem.fromUri(mediaUri)
            simpleExoPlayer!!.addMediaItem(mediaItem)
            simpleExoPlayer!!.prepare()
            simpleExoPlayer!!.pause()
            //videoView!!.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH
            simpleExoPlayer!!.playWhenReady = true
            simpleExoPlayer!!.repeatMode = SimpleExoPlayer.REPEAT_MODE_ALL
            simpleExoPlayer!!.play()
            simpleExoPlayer!!.repeatMode = Player.REPEAT_MODE_ALL
            simpleExoPlayer!!.seekBack()
            simpleExoPlayer!!.audioComponent?.volume = 0f
            simpleExoPlayer!!.videoScalingMode
            simpleExoPlayer!!.pauseAtEndOfMediaItems
            simpleExoPlayer!!.isLoading


            simpleExoPlayer!!.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    when (state) {
                        Player.STATE_BUFFERING -> {
                            // hide controls
                            // show progress
                            videoView?.visibility = View.GONE
                            stripImage?.visibility = View.VISIBLE


                        }
                        Player.STATE_READY -> {

                            videoView?.visibility = View.VISIBLE
                            stripImage?.visibility = View.GONE
                            // hide progress and controls
                        }
                        else -> {
                            videoView?.visibility = View.GONE
                            stripImage?.visibility = View.VISIBLE
                        }
                    }
                }
            })

        } else {
            /** image display **/
            releasePlayer()
            Log.e(TAG, "setupPlayer IMAGE: $videoUrl")
            videoView?.visibility = View.GONE
            stripImage?.visibility = View.VISIBLE
            Glide.with(requireActivity())
                .load(videoUrl)
                .into(stripImage!!)
        }
    }

    private fun observer() {
        viewModel.homePageData.observe(viewLifecycleOwner) {
            when (it.status) {
                com.app.stripstrips.utils.Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    imageCollection = it!!.data!!.data
                    Log.e(TAG, "observer: ${imageCollection!!.size}")
                    adapterCall()

                }

                com.app.stripstrips.utils.Status.LOADING -> {
                    MyApplication.showLoader(requireContext())

                }
                com.app.stripstrips.utils.Status.ERROR -> {
                    MyApplication.hideLoader()
                    if (imageCollection?.size==0) {
                        Log.e(TAG, "observer: ifcase" )
                        binding.TripsTabSharedRecyclerView.visibility = View.GONE
                        binding.tvNoDataFound.visibility = View.VISIBLE

                    } else {
                        Log.e(TAG, "observer: elsecase" )
                        binding.TripsTabSharedRecyclerView.visibility = View.VISIBLE
                        binding.tvNoDataFound.visibility = View.INVISIBLE
                    }
/*
                    if ( it.data!!.code == 401) {
                        ConstantsVar.ClearDataDialogAlart(requireActivity())

                    } else {
                        ConstantsVar.AppDialogAlart(requireActivity(),it.data.message)
                    }*/
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observerLikeDislike() {
        viewModel.likeDislikeApi.observe(viewLifecycleOwner) {
            when (it.status) {
                com.app.stripstrips.utils.Status.SUCCESS -> {
                    MyApplication.hideLoader()

                    if (it.data!!.status == 2) {
                        val tripId = it.data.androidTripId
                        Log.e(TAG, "observerLikeDislikeTRIPID: ${tripId}")
                        Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(activity, TripsDetailsLocationActivity::class.java)
                        intent.putExtra("Trip_Id", tripId)
                        intent.putExtra("thumbImage", "")
                        intent.putExtra("from", "trip")//from home trip created
                        startActivity(intent)

                        binding.TripsTabSharedRecyclerView.suppressLayout(false)
                        binding.TripsTabSharedRecyclerView.smoothSnapToPosition(currentPlayerPos + 1)
                        Log.e(TAG, "observerLike IFFFF:${currentPlayerPos}")

                    } else {
                        Log.e(TAG, "observerLike ELSEEEE:${currentPlayerPos}")
                        binding.TripsTabSharedRecyclerView.visibility = View.VISIBLE
                        binding.tvNoDataFound.visibility = View.GONE
                        binding.TripsTabSharedRecyclerView.suppressLayout(false)

                        binding.TripsTabSharedRecyclerView.smoothSnapToPosition(currentPlayerPos + 1)

                        Log.e(TAG, "TRIPSSS: ${imageCollection!!.size}", )
                        Log.e(TAG, "TRIPSSS: $currentPlayerPos", )

                        if(imageCollection!!.size-1 == currentPlayerPos)
                        {
                            Log.e(TAG, "lastPosition: ${imageCollection!!.size-1}")
                            binding.TripsTabSharedRecyclerView.visibility = View.GONE
                            binding.tvNoDataFound.visibility = View.VISIBLE

                        }

                    }

                }
                com.app.stripstrips.utils.Status.LOADING -> {
                    MyApplication.showLoader(requireContext())

                }

                com.app.stripstrips.utils.Status.ERROR -> {
                    MyApplication.hideLoader()

                }
            }
        }
    }

    fun releasePlayer() {
        if (simpleExoPlayer != null) {
            Log.e("TAG", "releasePlayer: ")
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


    fun showSnack(msg: String, data: String?) {

        Log.e(TAG, "showSnack: ${data}")
        val rootView: View = requireActivity().window.decorView.findViewById(R.id.content)


        val snackbar = Snackbar
            .make(rootView, msg, Snackbar.LENGTH_LONG)
            .setAction("Open") {
                val mSnackbar =
                    Snackbar.make(
                        rootView,
                        "GoTo trip",
                        Snackbar.LENGTH_LONG
                    )
                //open tripDetails activity
                Log.e(TAG, "showSnack: ${data}")

                val intent = Intent(activity, TripsDetailsLocationActivity::class.java)
                intent.putExtra("Trip_Id", data!!.toString())
                intent.putExtra("fromHome", "1")//from home trip created
                startActivity(intent)
                mSnackbar.show()
            }

        snackbar.show()
    }


    class NoScrollLinearLayoutManager(context: Context?) : LinearLayoutManager(context) {
        private var scrollable = true

        fun enableScrolling() {
            scrollable = false
        }

        fun disableScrolling() {
            scrollable = true
        }

        override fun canScrollVertically() =
            super.canScrollVertically() && scrollable


        override fun canScrollHorizontally() =
            super.canScrollVertically()
                    && scrollable
    }
}

