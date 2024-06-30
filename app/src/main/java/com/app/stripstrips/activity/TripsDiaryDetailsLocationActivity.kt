package com.app.stripstrips.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.stripstrips.R
import com.app.stripstrips.adapters.TripsDiaryDetailsAdapter
import com.app.stripstrips.base.MyApplication
import com.app.stripstrips.databinding.ActivityTripsDiaryDetailsBinding
import com.app.stripstrips.model.MapData
import com.app.stripstrips.model.postDeleteApi.PostDeleteRequest
import com.app.stripstrips.model.tripsDiaryDetailsApi.TripsData
import com.app.stripstrips.model.tripsDiaryDetailsApi.TripsDiaryDetailsRequest
import com.app.stripstrips.model.tripsDiaryDetailsApi.TripsDiaryListingDataItem
import com.app.stripstrips.network.PostDeleteInterface
import com.app.stripstrips.network.Repository
import com.app.stripstrips.utils.EasyLocationProvider
import com.app.stripstrips.utils.Status
import com.app.stripstrips.viewModel.ViewModel
import com.app.stripstrips.viewModel.ViewModelFactory
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.maps.android.ui.IconGenerator
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class TripsDiaryDetailsLocationActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {
    lateinit var binding: ActivityTripsDiaryDetailsBinding

    private var tripDiaryDetailsList: ArrayList<TripsDiaryListingDataItem> = ArrayList()
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationDiaryArrayList: ArrayList<LatLng>? = null
    private var mBottomSheetBehaviorDiary2: BottomSheetBehavior<*>? = null
    var latLng: LatLng? = null
    var TAG = "TripsDiaryDetailsLocationActivity"
    var tripDiary_id = ""
    var tripImage = ""
    var thumbImage = ""
    var trip_date = ""
    var pos = -1
    var firstPosition = -1
    var secondPositionGlobal = -1
    var latitude = 0.0
    var longitude = 0.0
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    var tripsDiaryNestedDetailsAdapter: TripsDiaryDetailsAdapter? = null
    var markerImage = ""

    private val viewModel by viewModels<ViewModel>
    { ViewModelFactory(Application(), Repository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripsDiaryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        tripDiary_id = intent.getStringExtra("Trip-Diary-Id")!!
        onClick()
        locationDropDownMethod()
        showDialogOne()
    }

    override fun onResume() {
        super.onResume()
        viewModel.tripDiaryDetailsApiData(TripsDiaryDetailsRequest(tripDiary_id, "1", "1000"),this)

    }

    private fun locationDropDownMethod() {
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this@TripsDiaryDetailsLocationActivity)
        val mapFragments = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragments.getMapAsync(this)
        locationDiaryArrayList = ArrayList()

    }

    private fun onClick() {
        binding.backImageView.setOnClickListener {
            finish()
        }
    }

    private fun showDialogOne() {
        val dialog = findViewById<View>(R.id.bottomSheet2)
        mBottomSheetBehaviorDiary2 = BottomSheetBehavior.from(dialog)

        val noDataTv = dialog.findViewById<TextView>(R.id.NoDataFoundTV)
        val recyclerViewDiaryList =
            dialog.findViewById<RecyclerView>(R.id.BottomSheetRecyclerViewDiary)

        mBottomSheetBehaviorDiary2!!.state = BottomSheetBehavior.STATE_EXPANDED

        mBottomSheetBehaviorDiary2!!.setPeekHeight(200, true)
        mBottomSheetBehaviorDiary2!!.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        fun adapterCall(tripDiaryDetailsList: ArrayList<TripsDiaryListingDataItem>) {
            tripsDiaryNestedDetailsAdapter =
                TripsDiaryDetailsAdapter(
                    this,
                    tripDiaryDetailsList,
                    tripImage,
                    thumbImage,
                    onDeletePost
                )
            recyclerViewDiaryList.adapter = tripsDiaryNestedDetailsAdapter
        }

        viewModel.tripDiaryDetailsApi.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    tripDiaryDetailsList.clear()
                    Log.e(TAG, "showDialogOne: ${it?.data?.data!!}")

                    tripDiaryDetailsList = it.data.data
                    adapterCall(tripDiaryDetailsList)

                    for (j in tripDiaryDetailsList.indices) {

                        for (i in tripDiaryDetailsList[j].data.indices) {

                            latLng = LatLng(
                                tripDiaryDetailsList[j].data[i].latitude.toDouble(),
                                tripDiaryDetailsList[j].data[i].longitude.toDouble()
                            )

                            markerImage =
                                if (tripDiaryDetailsList[j].data[i].postImage.contains(".mp4")
                                    || tripDiaryDetailsList[j].data[i].postImage.contains(
                                        ".3gp"
                                    ) && tripDiaryDetailsList[j].data[i].postImage.contains(".mov")
                                ) {  //image
                                    tripDiaryDetailsList[j].data[i].thumbImage.toString()

                                } else {
                                    tripDiaryDetailsList[j].data[i].postImage.toString()
                                }

                            var zoomNow = false
                            if (tripDiaryDetailsList.size - 1 == i) {
                                zoomNow = true
                            }
                            val myTask = FetchBitmap(
                                markerImage,
                                mMap,
                                latLng!!,
                                this,
                                tripDiaryDetailsList[j].data[i].postName,
                                zoomNow
                            )
                            myTask.execute()
                        }
                    }
                    binding.textView.text = it.data.diaryDetails.tripName
                    tripImage = it.data.diaryDetails.tripImage
                    onMapReady(mMap)

                }

                Status.LOADING -> {
                    MyApplication.showLoader(this)

                }

                Status.ERROR -> {
                    MyApplication.hideLoader()
                    Log.e("tag", it.message!!)
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Delete observer
        viewModel.postDeleteApi.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    tripDiaryDetailsList[firstPosition].data.removeAt(secondPositionGlobal)
                    if (tripDiaryDetailsList[firstPosition].data.isEmpty()) {
                        tripDiaryDetailsList.removeAt(firstPosition)
                    }
                    tripsDiaryNestedDetailsAdapter!!.notifyItemRemoved(pos)
                    tripsDiaryNestedDetailsAdapter!!.notifyItemChanged(pos)

                    mMap.clear()

                    for (j in tripDiaryDetailsList.size-1..0) {

                        for (i in tripDiaryDetailsList[j].data.size-1..0) {

                            latLng = LatLng(
                                tripDiaryDetailsList[j].data[i].latitude.toDouble(),
                                tripDiaryDetailsList[j].data[i].longitude.toDouble()
                            )

                            markerImage =
                                if (tripDiaryDetailsList[j].data[i].postImage.contains(".mp4")
                                    || tripDiaryDetailsList[j].data[i].postImage.contains(
                                        ".3gp"
                                    ) && tripDiaryDetailsList[j].data[i].postImage.contains(".mov")
                                ) {  //image
                                    tripDiaryDetailsList[j].data[i].thumbImage.toString()

                                } else {
                                    tripDiaryDetailsList[j].data[i].postImage.toString()
                                }

                            var zoomNow = false
                            if (tripDiaryDetailsList.size - 1 == i) {
                                zoomNow = true
                            }
                            val myTask = FetchBitmap(
                                markerImage,
                                mMap,
                                latLng!!,
                                this,
                                tripDiaryDetailsList[j].data[i].postName,
                                zoomNow
                            )
                            myTask.execute()
                        }
                    }
                     onMapReady(mMap)

                    if (tripDiaryDetailsList.size == 0) {
                        noDataTv.visibility = View.VISIBLE

                    } else {
                        noDataTv.visibility = View.INVISIBLE
                        adapterCall(tripDiaryDetailsList)
                    }
                }

                Status.LOADING -> {
                    MyApplication.showLoader(this)
                }

                Status.ERROR -> {
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

            var user: TripsData? = null
            for (i in tripDiaryDetailsList.indices) {
                for (j in tripDiaryDetailsList[i].data.indices) {

                    if (title == tripDiaryDetailsList[i].data[j].postName) {
                        user = tripDiaryDetailsList[i].data[j]
                    }
                    trip_date = convertDateFormat(tripDiaryDetailsList[i].date)
                }
            }
            if (user != null) {
                val intent = Intent(this, TripDiaryActivity::class.java)
                intent.putExtra("post_ids", user.postId)
                startActivity(intent)
            }
            Log.e(TAG, "onMapReady:$title: ${marker.title}")
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
                TripsDetailsLocationActivity.LOCATION_PERMISSION_REQUEST_CODE
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


    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection = url
                .openConnection() as HttpURLConnection
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
        var context: TripsDiaryDetailsLocationActivity,
        var postName: String,
        var zoomNow: Boolean

    ) :
        AsyncTask<Void?, Void?, Bitmap?>() {
        @Deprecated("Deprecated in Java")
        @SuppressLint("UseCompatLoadingForDrawables", "SuspiciousIndentation")
        override fun onPostExecute(result: Bitmap?) {
            val mImageView = CircleImageView(applicationContext)
            val layoutParams = LinearLayout.LayoutParams(90, 90)
            mImageView.layoutParams = layoutParams
            val matrix = Matrix()
            mImageView.matrix.postRotate(90f)
            val scaledBitmap = Bitmap.createScaledBitmap(result!!, 90, 90, true)
            val rotatedBitmap = Bitmap.createBitmap(
                scaledBitmap,
                0,
                0,
                scaledBitmap.width,
                scaledBitmap.height,
                matrix,
                true
            )
            val mIconGenerator = IconGenerator(applicationContext)
            mIconGenerator.setBackground(resources.getDrawable(R.drawable.circle_drawable))
            mIconGenerator.setContentView(mImageView)
            mImageView.setImageBitmap(rotatedBitmap)

            val iconBitmap: Bitmap = mIconGenerator.makeIcon()
            val markerOptions =
                MarkerOptions().position(loc).icon(BitmapDescriptorFactory.fromBitmap(iconBitmap))
                    .title(postName)
            myMap.addMarker(markerOptions)
            var ZoomLatlong = LatLng(tripDiaryDetailsList[tripDiaryDetailsList.size-1].data[0].latitude.toDouble(), tripDiaryDetailsList[tripDiaryDetailsList.size-1].data[0].longitude.toDouble())
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ZoomLatlong, 15f))


  /*          val mp = MarkerOptions()
            mp.position(LatLng(latitude, longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location))
            mp.title("my position")
            mMap.addMarker(mp)*/

            val urll = getDirectionURL(ZoomLatlong, LatLng(loc.latitude, loc.longitude),"AIzaSyCIVwm44mbRwXpqSX0Sf1XGuxftybnfniU")
            GetDirection(urll).execute()
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void?): Bitmap? {
            Thread.currentThread().priority = Thread.MAX_PRIORITY
            return getBitmapFromURL(imageURL)
        }
    }

    private val onDeletePost = object : PostDeleteInterface {
        @SuppressLint("DiscouragedPrivateApi")
        override fun postDelete(position: Int, secondPosition: Int, item: ConstraintLayout) {
            firstPosition = position
            secondPositionGlobal = secondPosition
            pos = position
            val popupMenu = PopupMenu(this@TripsDiaryDetailsLocationActivity, item)

            popupMenu.inflate(R.menu.deletemenu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.delete -> {
                        showDeleteAlertDialog(
                            this@TripsDiaryDetailsLocationActivity,
                            position,
                            secondPosition
                        )
                        true
                    }
                    else -> {
                        true

                    }
                }
            }
            try {
                val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                popup.isAccessible = true
                val menu = popup.get(popupMenu)
                menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(menu, true)
            } catch (e: Exception) {
                Log.d("error", e.toString())
            } finally {
                popupMenu.show()
            }
        }
    }

    fun showDeleteAlertDialog(mActivity: Context, position: Int, secondPosition: Int) {
        val alertDialog = Dialog(mActivity)
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.setContentView(R.layout.delete_alart)
        alertDialog.setCanceledOnTouchOutside(true)
        alertDialog.setCancelable(true)
        alertDialog.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val btnNo = alertDialog.findViewById<RelativeLayout>(R.id.btnNo)
        val btnYes = alertDialog.findViewById<RelativeLayout>(R.id.btnYes)

        btnNo.setOnClickListener {
            alertDialog.dismiss()
        }

        btnYes.setOnClickListener {
            viewModel.userPostDeleteData(PostDeleteRequest(tripDiaryDetailsList[position].data[secondPosition].postId))

            alertDialog.dismiss()
        }
    }

    /*********************************** Draw Routes between locations ****************************************/

    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url : String) : AsyncTask<Void, Void, List<List<LatLng>>>(){
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {

            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()

            val result = ArrayList<List<LatLng>>()
            try{
                val respObj = Gson().fromJson(data, MapData::class.java)
                val path = ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size){
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            }catch (e:Exception){
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices){
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.GREEN)
                lineoption.startCap(RoundCap())
                lineoption.geodesic(true)
            }
            mMap.addPolyline(lineoption)
        }

    }

    private fun getDirectionURL(origin:LatLng, dest:LatLng, secret: String) : String {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=driving" +
                "&key=$secret"
    }
}