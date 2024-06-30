package com.app.stripstrips.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.stripstrips.databinding.ActivityCaliforniaViewBinding
import com.app.stripstrips.model.tripsDiaryDetailsApi.TripsDiaryListingDataItem
import com.app.stripstrips.network.PostDeleteInterface
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TripsDiaryDetailsAdapter(
    val activity: FragmentActivity,
    private val tripDiaryDetailsList: ArrayList<TripsDiaryListingDataItem>,
    var tripImage: String,
    var thumbImage: String,
    var onDeletePost: PostDeleteInterface
) :
    RecyclerView.Adapter<TripsDiaryDetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ActivityCaliforniaViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {

            binding.dateTv.text = convertDateFormat(tripDiaryDetailsList[position].date)

            /*binding.item.setOnLongClickListener {
                onDeletePost.postDelete(position, binding.item)
                true
            }*/

            /** setup nested RV**/
            binding.tripsNestedRV.adapter = TripsDiaryNestedDetailsAdapter(
                tripDiaryDetailsList[position].data,
                activity,
                convertDateFormat(tripDiaryDetailsList[position].date),
                onDeletePost, position
            )
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun convertDateFormat(date: String): String {
        val dates: Date? = SimpleDateFormat("yyyy-MM-dd").parse(date)
        return SimpleDateFormat("MMM dd, yyyy").format(dates)
    }

    override fun getItemCount(): Int {
        return tripDiaryDetailsList.size
    }

    inner class ViewHolder(val binding: ActivityCaliforniaViewBinding) :
        RecyclerView.ViewHolder(binding.root)
}