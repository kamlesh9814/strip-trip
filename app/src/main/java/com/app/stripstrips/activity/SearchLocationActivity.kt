package com.app.stripstrips.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.stripstrips.base.MyApplication
import com.app.stripstrips.databinding.ActivitySearchLocationBinding
import com.app.stripstrips.model.LatlongApiModel.PredictionsItem
import com.app.stripstrips.network.LocationtemClickListner
import com.app.stripstrips.network.Repository
import com.app.stripstrips.utils.ConstantsVar.GOOGLE_PLACES_LAT_LONG
import com.app.stripstrips.utils.ConstantsVar.GOOGLE_PLACES_SEARCH
import com.app.stripstrips.utils.ConstantsVar.PLACES_API_KEY
import com.app.stripstrips.utils.EasyLocationProvider
import com.app.stripstrips.utils.Status
import com.app.stripstrips.viewModel.ViewModel
import com.app.stripstrips.viewModel.ViewModelFactory
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.naijadocs.adapter.SearchLocationAdapter
import java.util.*

class SearchLocationActivity : AppCompatActivity() {
    var binding: ActivitySearchLocationBinding? = null
    var TAG = this@SearchLocationActivity.javaClass.simpleName
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2
    val mLocationArrayList: ArrayList<PredictionsItem?> = ArrayList()
    var mAdapter: SearchLocationAdapter? = null

    var easyLocationProvider: EasyLocationProvider? = null
    var isCurrentLocation = true
    var mAccessFineLocation = Manifest.permission.ACCESS_FINE_LOCATION
    var mAccessCourseLocation = Manifest.permission.ACCESS_COARSE_LOCATION
    var city = ""
    var state = ""
    var zip_code = ""
    var street = ""
    var value = ""
    var latitude = 0.0
    var longitude = 0.0

    private val viewModel by viewModels<ViewModel>
    { ViewModelFactory(Application(), Repository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchLocationBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        value = intent.getStringExtra("value").toString()
        init()
        setUpSearch()
        observer()
        latLongObserver()
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onResume() {
        super.onResume()
        getLocation()
    }

    fun init() {
        binding!!.imgCancelIV.setOnClickListener {
            performCloseClick()
        }

        binding!!.currentLocationLL.setOnClickListener {
            if (checkPermission()) {
                getLocationLatLong()
            } else {
                requestPermission28()
            }
        }
    }


    /*********
     * Support for Marshmallows Version
     * 1) ACCESS_FINE_LOCATION
     * 2) ACCESS_COARSE_LOCATION
     */
    private fun checkPermission(): Boolean {
        val mlocationFineP = ContextCompat.checkSelfPermission(this, mAccessFineLocation)
        val mlocationCourseP =
            ContextCompat.checkSelfPermission(this, mAccessCourseLocation)
        return mlocationFineP == PackageManager.PERMISSION_GRANTED && mlocationCourseP == PackageManager.PERMISSION_GRANTED
    }

    val REQUEST_PERMISSION_CODE = 919
    private fun requestPermission28() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(mAccessFineLocation, mAccessCourseLocation),
            REQUEST_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                if (grantResults.size <= 0) {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.e(TAG, "User interaction was cancelled.")
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocationLatLong()
                } else {
                    // Permission denied.
                    Toast.makeText(this, "permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }

        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    fun getLocationLatLong() {
        easyLocationProvider = EasyLocationProvider.Builder(this)
            .setInterval(3000)
            .setFastestInterval(1000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setListener(object : EasyLocationProvider.EasyLocationCallback {
                override fun onGoogleAPIClient(googleApiClient: GoogleApiClient?, message: String) {
                    Log.e("EasyLocationProvider", "onGoogleAPIClient: $message")
                }

                override fun onLocationUpdated(mlatitude: Double, mlongitude: Double) {
                    Log.e(TAG, "onLocationUpdated:: Latitude: $mlatitude Longitude: $mlongitude")
                    if (isCurrentLocation) {
                        //dynamically
                        isCurrentLocation = false
                        latitude = mlatitude
                        longitude = mlongitude
                        if (latitude != null && longitude != null) {
                            try {

                                val addresses: List<Address>
                                val geocoder = Geocoder(this@SearchLocationActivity, Locale.getDefault())
                                addresses = geocoder.getFromLocation(
                                    latitude,
                                    longitude,
                                    1
                                )!! // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                                if (addresses != null && addresses.size > 0) {
                                    if (addresses[0].locality != null) {
                                        city = addresses[0].locality
                                    }

                                    if (addresses[0].adminArea != null) {
                                        state = addresses[0].adminArea
                                    }

                                    if (addresses[0].postalCode != null) {
                                        zip_code = addresses[0].postalCode
                                    }

                                    if (addresses[0].getAddressLine(0) != null) {
                                            street = addresses[0].getAddressLine(0)
                                    }
                                    val intentReturn = Intent()
                                    intentReturn.putExtra("street", street)
                                    intentReturn.putExtra("city", city)
                                    intentReturn.putExtra("state", state)
                                    intentReturn.putExtra("zip_code", zip_code)
                                    intentReturn.putExtra("latitude", mlatitude.toString())
                                    intentReturn.putExtra("longitude", mlongitude.toString())
                                    setResult(RESULT_OK, intentReturn)
                                    finish()
                                }

                            } catch (e: Exception) {
                                dismissProgressDialog()
                                Log.e(TAG, "**Error**" + e.message)
                            }
                        }
                    }
                }

                override fun onLocationUpdateRemoved() {
                    Log.e("EasyLocationProvider", "onLocationUpdateRemoved")
                }
            }).build()
        lifecycle.addObserver(easyLocationProvider!!)
    }

    fun getUserAddressFromLatLong(mlatitude: Double, mlongitude: Double) {
        try {

            val addresses: List<Address>
            val geocoder = Geocoder(this, Locale.getDefault())
            addresses = geocoder.getFromLocation(
                mlatitude,
                mlongitude,
                1
            )!! // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            if (addresses != null && addresses.size > 0) {
                if (addresses[0].locality != null) {
                    city = addresses[0].locality
                }

                if (addresses[0].adminArea != null) {
                    state = addresses[0].adminArea
                }

                if (addresses[0].postalCode != null) {
                    zip_code = addresses[0].postalCode
                }

                if (addresses[0].getAddressLine(0) != null) {
                    street = addresses[0].getAddressLine(0)
                }
                val intentReturn = Intent()
                intentReturn.putExtra("street", street)
                intentReturn.putExtra("city", city)
                intentReturn.putExtra("state", state)
                intentReturn.putExtra("zip_code", zip_code)
                intentReturn.putExtra("latitude", mlatitude.toString())
                intentReturn.putExtra("longitude", mlongitude.toString())
                setResult(RESULT_OK, intentReturn)
                finish()
            }

        } catch (e: Exception) {
            dismissProgressDialog()
            Log.e(TAG, "**Error**" + e.message)
        }
    }

    private fun setUpSearch() {
        binding!!.editSearchET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length > 0) {
                    binding!!.imgCancelIV.visibility = View.VISIBLE
                } else {
                    binding!!.imgCancelIV.visibility = View.GONE
                }
            }
        })


        binding!!.editSearchET.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchLocation()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun searchLocation() {
        if (isNetworkAvailable(this)) {
            executePlacesRequest()
        } else {
            Toast.makeText(this, "internet_connection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performCloseClick() {
        binding?.editSearchET?.setText("")
        binding?.imgCancelIV?.visibility = View.GONE
        mLocationArrayList?.clear()
        mAdapter?.notifyDataSetChanged()
        binding?.noDataTV?.visibility = View.GONE
    }

    var progressDialog: Dialog? = null

    fun dismissProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            progressDialog!!.dismiss()
        }
    }

    private fun executePlacesRequest() {
        val mSearhText = binding?.editSearchET?.text.toString().trim { it <= ' ' }
        val mApiUrl: String =
            GOOGLE_PLACES_SEARCH + "input=" + mSearhText + "&fields=name,address_component,place_id&key=" + PLACES_API_KEY
        viewModel.locationEditApiData(mApiUrl)
    }


    private fun setAdapter() {
        mAdapter = SearchLocationAdapter(this, mLocationArrayList, mPlayDatesItemClickListner)
        binding?.locationsRV?.layoutManager = LinearLayoutManager(this)
        binding?.locationsRV?.setHasFixedSize(true)
        binding?.locationsRV?.adapter = mAdapter
    }

    var mPlayDatesItemClickListner: LocationtemClickListner = object : LocationtemClickListner {
        override fun onItemClickListner(mModel: PredictionsItem) {
            val mApiUrl: String =
                GOOGLE_PLACES_LAT_LONG + mModel.placeId + "&fields=name,geometry&types=establishment" + "&key=" + PLACES_API_KEY
            viewModel.latLongEditApiData(mApiUrl)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (easyLocationProvider != null) {
            easyLocationProvider!!.removeUpdates()
            lifecycle.removeObserver(easyLocationProvider!!)
        }
    }

    fun isNetworkAvailable(mContext: Context): Boolean {
        val connectivityManager =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun observer() {
        viewModel.locationEditApi.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    var mPlacesModel = it.data
                    mLocationArrayList?.clear()
                    if (mPlacesModel?.status == "OK") {
                        mLocationArrayList?.addAll(mPlacesModel.predictions!!)
                        if (mLocationArrayList!!.size > 0) {
                            setAdapter()
                            binding?.noDataTV?.visibility = View.GONE
                        } else {
                            binding?.noDataTV?.visibility = View.VISIBLE
                        }
                    } else {
                        binding?.noDataTV?.visibility = View.VISIBLE

                    }


                }
                Status.LOADING -> {
                    MyApplication.showLoader(this@SearchLocationActivity)

                }
                Status.ERROR -> {
                    MyApplication.hideLoader()
                    Log.e("tag", it.message!!)
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun latLongObserver() {
        viewModel.LatLongApi.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    val mPlacesLatLongModel = it.data
                    if (mPlacesLatLongModel?.status == "OK") {
                        Log.e(TAG, "onResponse: " + mPlacesLatLongModel)
                        getUserAddressFromLatLong(
                            mPlacesLatLongModel.result!!.geometry!!.location!!.lat!!,
                            mPlacesLatLongModel.result.geometry!!.location!!.lng!!
                        )
                    }
                }
                Status.LOADING -> {
                    MyApplication.showLoader(this@SearchLocationActivity)

                }
                Status.ERROR -> {
                    MyApplication.hideLoader()
                    Log.e("tag", it.message!!)
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /** Location Code **/
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled(this)) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)!!
                        latitude = location.latitude
                        longitude = location.longitude
                    }
                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun isLocationEnabled(context: Context): Boolean {
        var locationMode = 0
        val locationProviders: String
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            locationMode = try {
                Settings.Secure.getInt(context.contentResolver, Settings.Secure.LOCATION_MODE)
            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
                return false
            }
            locationMode != Settings.Secure.LOCATION_MODE_OFF
        } else {
            locationProviders = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED
            )
            !TextUtils.isEmpty(locationProviders)
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }
}