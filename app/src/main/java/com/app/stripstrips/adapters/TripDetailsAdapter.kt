package com.app.stripstrips.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.stripstrips.R
import com.app.stripstrips.activity.TripDetailActivity
import com.app.stripstrips.databinding.ActivityBottomsheetDialogueBinding
import com.app.stripstrips.databinding.ActivityTripsdiaryBinding
import com.app.stripstrips.model.tripDetails.DataItem
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TripDetailsAdapter(
    val activity: FragmentActivity,
    private val trisDetailList: ArrayList<DataItem>,
    var tripImage: String,
    var thumbImage: String
) :
    RecyclerView.Adapter<TripDetailsAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ActivityTripsdiaryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ActivityTripsdiaryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {

            binding.tvLondonDiaries.text = trisDetailList[position].postName
            binding.descTV.text = trisDetailList[position].postDescription
            if (trisDetailList[position].thumbImage != "" )
              {  //image
                binding.ivProfile.load(trisDetailList[position].thumbImage.toString()) {
                    error(R.drawable.human)
                }

            } else {
                binding.ivProfile.load(trisDetailList[position].postImage.toString()) {
                    error(R.drawable.human)
                }
            }

            binding.viewClick.setOnClickListener {
                val dates = convertDateFormat(trisDetailList[position].postDate!!)
                val intent = Intent(activity, TripDetailActivity::class.java)
                intent.putExtra("post_id", trisDetailList[position].postId)
                intent.putExtra("postName", trisDetailList[position].postName)
                intent.putExtra("postDescription", trisDetailList[position].postDescription)
                intent.putExtra("location", trisDetailList[position].location)
                intent.putExtra("updatedAt", dates)
                intent.putExtra("postImage", trisDetailList[position].postImage)
                intent.putExtra("thumbImage", trisDetailList[position].thumbImage)
                activity.startActivity(intent)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertDateFormat(date: String): String {
        val dates: Date? = SimpleDateFormat("yyyy-MM-dd").parse(date)
        return SimpleDateFormat("MMM dd, yyyy").format(dates!!)
    }

    override fun getItemCount(): Int {
        return trisDetailList.size
    }
}