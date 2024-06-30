package com.app.stripstrips.activity

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.stripstrips.adapters.UserListingAdapter
import com.app.stripstrips.base.MyApplication
import com.app.stripstrips.databinding.ActivityShareListingBinding
import com.app.stripstrips.model.shareTripApi.ShareTripRequest
import com.app.stripstrips.model.userListingApi.UserListingDataItem
import com.app.stripstrips.model.userListingApi.UserListingRequest
import com.app.stripstrips.network.Repository
import com.app.stripstrips.utils.TripSharedInterface
import com.app.stripstrips.viewModel.ViewModel
import com.app.stripstrips.viewModel.ViewModelFactory

class UserShareListingActivity : AppCompatActivity() {
    lateinit var binding: ActivityShareListingBinding
    private var userShareListing: ArrayList<UserListingDataItem> = ArrayList()
    var trip_id = ""
    var mString: ArrayList<String> = ArrayList()
    var finalid = ""
    lateinit var dataInterface: TripSharedInterface
    private val viewModel by viewModels<ViewModel>
    { ViewModelFactory(Application(), Repository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareListingBinding.inflate(layoutInflater)
        trip_id = intent.getStringExtra("Trip_Id")!!
        setInterface()
        onClick()
        observer()
        sendObserver()
        setContentView(binding.root)

    }
    private fun setInterface() {
        dataInterface = object : TripSharedInterface {
            override fun selectedShared(position: Int, userIS: String) {
                if (mString.size > 0) {
                    for (i in 0 until mString.size) {
                            if (userIS==mString[i]){
                            mString.removeAt(i)
                                break
                        } else {
                            if (i == mString.size-1){
                                mString.add(userIS)

                            }
                        }
                    }
                } else {
                    mString.add(userIS)

                }
                finalid = mString.toString()
                finalid = finalid.replace("[", "")
                    .replace("]", "")
                Log.e(TAG, "selectedShared: $position")
                Log.e(TAG, "selectedShared: $finalid")
                Log.e(TAG, "selectedShared: $mString")

            }
        }
    }
    private fun onClick() {
        binding.sendLayout.setOnClickListener {
            if (finalid.isEmpty()) {
                Toast.makeText(this, "Please Select User First", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.userShareTripApiData(ShareTripRequest(trip_id, finalid),this)
            }
        }
        binding.imageView3.setOnClickListener {
            binding.ETSearch.setText("")
        }
        binding.boldBackIv.setOnClickListener {
            finish()
        }

        binding.ETSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.isEmpty() && userShareListing.isNotEmpty()) {
                    binding.listRecyclerView.visibility = View.VISIBLE
                    binding.noDatTv.visibility = View.GONE
                    userShareListing.clear()
                    viewModel.userListingDetailsApiData(UserListingRequest("1", "1000", ""))

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        binding.ETSearch.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.userListingDetailsApiData(
                    UserListingRequest(
                        "1",
                        "1000",
                        binding.ETSearch.text.toString()
                    )
                )
                true

            } else false

        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.userListingDetailsApiData(UserListingRequest("1", "1000", ""))
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun adapterCall(userShareListing: ArrayList<UserListingDataItem>) {
        binding.listRecyclerView.adapter =
            UserListingAdapter(this@UserShareListingActivity, userShareListing, dataInterface)
        binding.listRecyclerView.adapter!!.notifyDataSetChanged()

    }

    private fun observer() {
        viewModel.userListingApi.observe(this) {
            when (it.status) {
                com.app.stripstrips.utils.Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    Log.e(TAG, "observerAPIDATA: ${it.data?.data!!}")
                    userShareListing = it.data.data
                    adapterCall(userShareListing)


                }

                com.app.stripstrips.utils.Status.LOADING -> {
                    MyApplication.showLoader(this)

                }

                com.app.stripstrips.utils.Status.ERROR -> {
                    MyApplication.hideLoader()
                    Log.e(TAG, "observerAPIDATA: ${it.message}")
                    binding.listRecyclerView.visibility = View.GONE
                    binding.noDatTv.visibility = View.VISIBLE


                }
            }
        }
    }

    private fun sendObserver() {
        viewModel.userShareTripApi.observe(this) {
            when (it.status) {
                com.app.stripstrips.utils.Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    finish()
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
}