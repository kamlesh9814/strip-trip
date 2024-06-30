package com.app.stripstrips.activity
import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.os.AsyncTask
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.app.stripstrips.R
import com.app.stripstrips.adapters.TripDetailsAdapter
import com.app.stripstrips.base.MyApplication
import com.app.stripstrips.databinding.ActivityTripsDetailBinding
import com.app.stripstrips.model.tripDetails.DataItem
import com.app.stripstrips.model.tripDetails.TripDetailsRequest
import com.app.stripstrips.network.Repository
import com.app.stripstrips.viewModel.ViewModel
import com.app.stripstrips.viewModel.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.maps.android.ui.IconGenerator
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class TripsDetailsLocationActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {
    lateinit var binding: ActivityTripsDetailBinding
    var TAG = "TripsDetailsLocationActivity"
    var trip_id = ""
    var tripImage = ""
    var thumbImage = ""
    var latitude = 0.0
    var longitude = 0.0
    var from = ""
    private var trisDetailsList: ArrayList<DataItem> = ArrayList()
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var latLng: LatLng? = null
    private var locationArrayList: ArrayList<LatLng>? = null
    private var mBottomSheetBehavior: BottomSheetBehavior<*>? = null

    private val viewModel by viewModels<ViewModel>
    { ViewModelFactory(Application(), Repository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        if (intent != null) {
            trip_id = intent.getStringExtra("Trip_Id").toString()
            Log.e(TAG, "onCreate: $trip_id")
            from = intent.getStringExtra("from")!!
            binding.shareIV.isVisible = from == "trip"
            viewModel.dataTripDetails(TripDetailsRequest(trip_id, "1", "1000"),this)
        }
        onClick()
        locationDropDownMethod()
        showDialogOne()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun locationDropDownMethod() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        locationArrayList = ArrayList()

    }

    private fun onClick() {
        binding.backImageView.setOnClickListener {
            finish()
        }
        binding.shareIV.setOnClickListener {
            val intent =
                Intent(this@TripsDetailsLocationActivity, UserShareListingActivity::class.java)
            intent.putExtra("Trip_Id", trip_id)
            startActivity(intent)
        }
    }

    /** Bottom Sheet Code **/
    private fun showDialogOne() {

        val dialog = findViewById<View>(R.id.bottom_sheet)
        mBottomSheetBehavior = BottomSheetBehavior.from(dialog)

            val noDataTv = dialog.findViewById<TextView>(R.id.tvNoDataFound)
            val recyclerViewList = dialog.findViewById<RecyclerView>(R.id.BottomSheetRecyclerView)

            mBottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED

            mBottomSheetBehavior!!.setPeekHeight(200, true)
            mBottomSheetBehavior!!.setBottomSheetCallback(object :
                BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {

                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }
            })


            fun adapterCall(trisDetailList: ArrayList<DataItem>) {
                recyclerViewList!!.adapter =
                    TripDetailsAdapter(this, trisDetailList, tripImage, thumbImage)
            }
        /** observer call under BottomSheet **/
        viewModel.tripDetailsData.observe(this) {
            when (it.status) {
                com.app.stripstrips.utils.Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    Log.e(TAG, "showDialogOne: ${it.data?.data!!}")
                    trisDetailsList = it.data.data

                    if (trisDetailsList.size == 0) {
                        noDataTv!!.visibility = View.VISIBLE

                    } else {
                        adapterCall(trisDetailsList)

                        for (i in trisDetailsList.indices) {
                            latLng = LatLng(
                                trisDetailsList[i].latitude!!.toDouble(),
                                trisDetailsList[i].longitude!!.toDouble()
                            )

                            var markerImage = ""
                            markerImage =
                                if (trisDetailsList[i].thumbImage != "") {  //image
                                    trisDetailsList[i].thumbImage.toString()

                                } else {
                                    trisDetailsList[i].postImage.toString()
                                }
                            var zoomNow = false
                            if (trisDetailsList.size - 1 == i) {
                                zoomNow = true
                            }
                            val myTask = FetchBitmap(
                                markerImage,
                                mMap,
                                latLng!!,
                                this,
                                trisDetailsList[i].postName!!,
                                zoomNow
                            )
                            myTask.execute()
                        }
                        binding.TVTitle.text = it.data.tripDetails?.tripName
                        tripImage = it.data.tripDetails?.tripImage.toString()
                        thumbImage = it.data.tripDetails?.thumbImage.toString()
                        onMapReady(mMap)
                    }
                }
                com.app.stripstrips.utils.Status.LOADING -> {
                    MyApplication.showLoader(this)
                }

                com.app.stripstrips.utils.Status.ERROR -> {
                    MyApplication.hideLoader()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        try {
            // Customise map styling via JSON file
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.light_map_style))

            if (!success) {
                Log.e("TAG", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("TAG", "Can't find style. Error: ", e)
        }

        setUpMap()

        mMap.setOnInfoWindowClickListener { marker ->
            val title = marker.title

            var user: DataItem? = null
            for (i in trisDetailsList.indices) {

                if (title == trisDetailsList[i].postName) {
                    user = trisDetailsList[i]
                }
                Log.e(TAG, "onMapReadyss: $user")

            }

            Log.e("MAPCLICKED", "onMapReady$user")
            if (user != null) {

                val convertDate = convertDateFormat(user.postDate.toString())
                val intent = Intent(this, TripDetailActivity::class.java)
                intent.putExtra("post_id", user.postId)
                intent.putExtra("postName", user.postName)
                intent.putExtra("postDescription", user.postDescription)
                intent.putExtra("location", user.location)
                intent.putExtra("updatedAt", convertDate)
                intent.putExtra("postImage", user.postImage)
                intent.putExtra("thumbImage", user.thumbImage)
                intent.putExtra("tripImage", tripImage)
                intent.putExtra("thumbImage", thumbImage)
                startActivity(intent)
            }

            Log.e("MAPCLICKED", "onMapReady$title: ${marker.title}")
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertDateFormat(date: String): String {
        val dates: Date? = SimpleDateFormat("yyyy-MM-dd").parse(date)
        return SimpleDateFormat("MMM dd, yyyy").format(dates!!)
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        Log.e(TAG, "onMarkerClick: ${marker.title}")
        if (marker.isInfoWindowShown) {
            marker.hideInfoWindow()
        } else {
            marker.showInfoWindow()
        }
        return true
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 2
    }


    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class FetchBitmap(
        var imageURL: String,
        var myMap: GoogleMap,
        var loc: LatLng,
        var context: TripsDetailsLocationActivity,
        var postName: String,
        var zoomNow: Boolean

    ) :
        AsyncTask<Void?, Void?, Bitmap?>() {
        @SuppressLint("UseCompatLoadingForDrawables")
        override fun onPostExecute(result: Bitmap?) {
            val mImageView = CircleImageView(applicationContext)
            val layoutParams = LinearLayout.LayoutParams(80, 80)
            mImageView.layoutParams = layoutParams
            val mIconGenerator = IconGenerator(applicationContext)
            mIconGenerator.setBackground(resources.getDrawable(R.drawable.circle_drawable))
            mIconGenerator.setContentView(mImageView)
            mImageView.setImageBitmap(result)
            val iconBitmap: Bitmap = mIconGenerator.makeIcon()
            val markerOptions =
                MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromBitmap(iconBitmap))
                    .title(postName)
            myMap.addMarker(markerOptions)

            if (zoomNow) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng!!, 8f))
            }
        }


        override fun onProgressUpdate(vararg values: Void?) {
            super.onProgressUpdate(*values)
        }

        @Deprecated(
            "Deprecated in Java",
            ReplaceWith("super.onPreExecute()", "android.os.AsyncTask")
        )
        override fun onPreExecute() {
            super.onPreExecute()
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void?): Bitmap? {
            Thread.currentThread().priority = Thread.MAX_PRIORITY
            return getBitmapFromURL(imageURL)
        }
    }
}
