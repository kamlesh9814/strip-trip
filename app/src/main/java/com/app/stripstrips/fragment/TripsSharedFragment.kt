package com.app.stripstrips.fragment
import android.annotation.SuppressLint
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.stripstrips.R
import com.app.stripstrips.adapters.TripsTabAdapter
import com.app.stripstrips.base.MyApplication
import com.app.stripstrips.databinding.FragmentTripsSharedBinding
import com.app.stripstrips.model.tripAndSharedByOtherListingApi.TripSharedByOtherListingDataItem
import com.app.stripstrips.model.tripAndSharedByOtherListingApi.TripSharedByOtherListingRequest
import com.app.stripstrips.model.tripDeleteApi.TripDeleteRequest
import com.app.stripstrips.network.Repository
import com.app.stripstrips.network.TripsDeleteInterface
import com.app.stripstrips.utils.ConstantsVar
import com.app.stripstrips.viewModel.ViewModel
import com.app.stripstrips.viewModel.ViewModelFactory

class TripsSharedFragment : Fragment() {
    lateinit var binding: FragmentTripsSharedBinding
    private var tripsSharedByOtherList: ArrayList<TripSharedByOtherListingDataItem> = ArrayList()
    var pos = -0
    var tripsTabAdapter: TripsTabAdapter? = null
    private val viewModel by viewModels<ViewModel>
    { ViewModelFactory(Application(), Repository()) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentTripsSharedBinding.inflate(layoutInflater, container, false)
        observer()
        tripDeleteObserver()
        onClick()
        return binding.root
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            viewModel.tripsSharedByOtherApiData(
                TripSharedByOtherListingRequest(
                    "1",
                    "1000",
                    "2",
                    ""

                ),

            )
        }
    }

    private fun onClick() {
        binding.cancelIV.setOnClickListener {
            binding.ETSearch.setText("")
        }

        binding.ETSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.isEmpty() && tripsSharedByOtherList.isNotEmpty()) {
                    binding.TripsTabSharedRecyclerView.visibility = View.VISIBLE
                    binding.noDatTv.visibility = View.GONE
                    tripsSharedByOtherList.clear()
                    viewModel.tripsSharedByOtherApiData(
                        TripSharedByOtherListingRequest
                            ("1", "1000", "2", binding.ETSearch.text.toString()),

                    )

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })



        binding.ETSearch.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.tripsSharedByOtherApiData(
                    TripSharedByOtherListingRequest(
                        "1",
                        "1000", "2",
                        binding.ETSearch.text.toString()

                    ),

                )
                true

            } else false
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun adapterCall(tripsSharedByOtherList: ArrayList<TripSharedByOtherListingDataItem>) {
        tripsTabAdapter =
            TripsTabAdapter(requireActivity(), tripsSharedByOtherList, deleteDataClick, "tripShared")
        binding.TripsTabSharedRecyclerView.adapter = tripsTabAdapter
        binding.TripsTabSharedRecyclerView.adapter!!.notifyDataSetChanged()
    }

    private var deleteDataClick = object : TripsDeleteInterface {
        override fun tripsDelete(position: Int, item: ConstraintLayout) {
            pos = position
            val popupMenu = PopupMenu(activity, item)

            popupMenu.inflate(R.menu.deletemenu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
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

    private fun observer() {
        viewModel.tripsSharedByOtherApi.observe(viewLifecycleOwner) {
            when (it.status) {
                com.app.stripstrips.utils.Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    tripsSharedByOtherList = it.data?.data!!
                    adapterCall(tripsSharedByOtherList)


                }
                com.app.stripstrips.utils.Status.LOADING -> {
                    MyApplication.showLoader(requireContext())

                }
                com.app.stripstrips.utils.Status.ERROR -> {
                    MyApplication.hideLoader()
                    if (tripsSharedByOtherList.size == 0) {
                        binding.TripsTabSharedRecyclerView.visibility = View.GONE
                        binding.noDatTv.visibility = View.VISIBLE

                    } else {
                        binding.TripsTabSharedRecyclerView.visibility = View.VISIBLE
                        binding.noDatTv.visibility = View.INVISIBLE
                    }
                    binding.TripsTabSharedRecyclerView.visibility = View.GONE
                    binding.noDatTv.visibility = View.VISIBLE
                    
                }
            }
        }
    }

    private fun tripDeleteObserver() {
        viewModel.tripDeletedApi.observe(viewLifecycleOwner) {
            when (it.status) {
                com.app.stripstrips.utils.Status.SUCCESS -> {
                    MyApplication.hideLoader()
                    tripsTabAdapter!!.notifyItemRemoved(pos)
                    tripsTabAdapter!!.notifyItemChanged(pos)
                    tripsSharedByOtherList.removeAt(pos)

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
            viewModel.userTripDeletedData(TripDeleteRequest(tripsSharedByOtherList[position].tripId,"2"))
            alertDialog.dismiss()
        }
    }
}