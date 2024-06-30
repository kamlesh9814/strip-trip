package com.app.stripstrips.fragment

import android.annotation.SuppressLint
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.stripstrips.R
import com.app.stripstrips.activity.AddTripsDiaryActivity
import com.app.stripstrips.adapters.TripsDiaryFragmentAdapter
import com.app.stripstrips.base.MyApplication
import com.app.stripstrips.databinding.FragmentTripsDiaryBinding
import com.app.stripstrips.model.tripDiaryDeleatedApi.TripDiaryDeleatedRequest
import com.app.stripstrips.model.tripsDiaryListingApi.TripsDiaryDataItem
import com.app.stripstrips.model.tripsDiaryListingApi.TripsDiaryListingRequest
import com.app.stripstrips.network.OnDeleteClick
import com.app.stripstrips.network.Repository
import com.app.stripstrips.utils.ConstantsVar
import com.app.stripstrips.viewModel.ViewModel
import com.app.stripstrips.viewModel.ViewModelFactory

class TripsDiaryFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentTripsDiaryBinding
    private var trisDiaryList: ArrayList<TripsDiaryDataItem> = ArrayList()
    var diaryId = ""
    var pos = -0
    var tripsDiaryFragmentAdapter: TripsDiaryFragmentAdapter? = null
    private val viewModel by viewModels<ViewModel>
    { ViewModelFactory(Application(), Repository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        viewModel.tripDiaryListingApiData(TripsDiaryListingRequest("1", "1000"),requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTripsDiaryBinding.inflate(layoutInflater, container, false)
        listeners()
        observer()
        tripDeleteObserver()
        return binding.root
    }

    private fun listeners() {
        binding.IVPlus.setOnClickListener(this)

    }

    private fun adapterCall(trisDiaryList: ArrayList<TripsDiaryDataItem>) {
        tripsDiaryFragmentAdapter =
            TripsDiaryFragmentAdapter(requireActivity(), trisDiaryList, deleteDataCallBack)
        binding.TripsTabSharedRecyclerView.adapter = tripsDiaryFragmentAdapter

    }

    private val deleteDataCallBack = object : OnDeleteClick {
        @SuppressLint("DiscouragedPrivateApi")
        override fun selectDelete(position: Int, item: ConstraintLayout) {
            pos = position
            val popupMenu = PopupMenu(activity, item)
            popupMenu.inflate(R.menu.popupmenu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.edit -> {
                        startActivity(
                            Intent(activity, AddTripsDiaryActivity::class.java).apply {
                                putExtra("tripDiaryId", trisDiaryList[position].diaryId)
                                putExtra("tripImage", trisDiaryList[position].tripImage)
                                putExtra("tripName", trisDiaryList[position].tripName)
                                putExtra("tripDescreption", trisDiaryList[position].tripDescreption)
                            })

                        true
                    }
                    R.id.delete -> {
                        showDeleteAlertDialog(requireActivity(), position, item)
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

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.IVPlus -> {
                startActivity(Intent(requireContext(), AddTripsDiaryActivity::class.java))
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observer() {
        viewModel.tripDiaryListingApi.observe(viewLifecycleOwner) {
            when (it.status) {
                com.app.stripstrips.utils.Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    trisDiaryList = it.data?.data!!
                    adapterCall(trisDiaryList)
                    binding.TripsTabSharedRecyclerView.visibility = View.VISIBLE
                    binding.tvNoDataFound.visibility = View.GONE

                }
                com.app.stripstrips.utils.Status.LOADING -> {
                    MyApplication.showLoader(requireContext())

                }
                com.app.stripstrips.utils.Status.ERROR -> {
                    MyApplication.hideLoader()

                    if (trisDiaryList.size == 0) {
                        binding.TripsTabSharedRecyclerView.visibility = View.GONE
                        binding.tvNoDataFound.visibility = View.VISIBLE

                    } else {
                        binding.TripsTabSharedRecyclerView.visibility = View.VISIBLE
                        binding.tvNoDataFound.visibility = View.INVISIBLE
                        binding.TripsTabSharedRecyclerView.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun tripDeleteObserver() {
        viewModel.tripDiaryDeletedApi.observe(viewLifecycleOwner) {
            when (it.status) {
                com.app.stripstrips.utils.Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    trisDiaryList.removeAt(pos)
                    tripsDiaryFragmentAdapter!!.notifyItemRemoved(pos)
                    tripsDiaryFragmentAdapter!!.notifyItemChanged(pos)

                    if (trisDiaryList.size == 0) {
                        binding.TripsTabSharedRecyclerView.visibility = View.GONE
                        binding.tvNoDataFound.visibility = View.VISIBLE

                    } else {
                        binding.TripsTabSharedRecyclerView.visibility = View.VISIBLE
                        binding.tvNoDataFound.visibility = View.INVISIBLE
                    }
                }
                com.app.stripstrips.utils.Status.LOADING -> {
                    MyApplication.showLoader(requireContext())

                }

                com.app.stripstrips.utils.Status.ERROR -> {
                    MyApplication.hideLoader()
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    fun showDeleteAlertDialog(mActivity: Context, position: Int, item: ConstraintLayout) {
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
            viewModel.userTripDiaryDeletedData(TripDiaryDeleatedRequest(trisDiaryList[position].diaryId))
            alertDialog.dismiss()
        }
    }
}