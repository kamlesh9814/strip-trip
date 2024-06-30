package com.app.stripstrips.adapters

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.stripstrips.R
import com.app.stripstrips.activity.TripsDetailsLocationActivity
import com.app.stripstrips.databinding.ActivityTabTripsBinding
import com.app.stripstrips.model.tripAndSharedByOtherListingApi.TripSharedByOtherListingDataItem
import com.app.stripstrips.network.TripsDeleteInterface

class TripsTabAdapter(
    var activity: FragmentActivity,
    private val tripsSharedByOtherList: ArrayList<TripSharedByOtherListingDataItem>,
    var onDeleteClick: TripsDeleteInterface,
    val type: String,
) : RecyclerView.Adapter<TripsTabAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ActivityTabTripsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ActivityTabTripsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            binding.descriptionTv.text = tripsSharedByOtherList[position].tripName
            if (type == "trip") {
               binding.ivShare.isVisible  = false
            } else {
                binding.ivShare.isVisible  = true
                binding.ivShare.text = "Shared By: ${tripsSharedByOtherList[position].sharedBy?.userName?:""}"
            }


            Log.e(TAG,
                "onBindViewHolder:$ thumbImage" + tripsSharedByOtherList[position].thumbImage)

            if (tripsSharedByOtherList[position].thumbImage != "") {
                binding.ivProfile.load(tripsSharedByOtherList[position].thumbImage) {
                    error(R.drawable.human)
                }
            } else {
                binding.ivProfile.load(tripsSharedByOtherList[position].tripImage) {
                    error(R.drawable.human)
                }
            }

            binding.item.setOnClickListener {
                val intent = Intent(activity, TripsDetailsLocationActivity::class.java)
                intent.putExtra("Trip_Id", tripsSharedByOtherList[position].tripId)
                intent.putExtra("thumbImage", tripsSharedByOtherList[position].thumbImage)
                intent.putExtra("from",type)
                activity.startActivity(intent)
            }
            binding.item.setOnLongClickListener {
                onDeleteClick.tripsDelete(position, binding.item)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return tripsSharedByOtherList.size

    }
}