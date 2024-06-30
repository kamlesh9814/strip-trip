package com.app.stripstrips.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.stripstrips.R
import com.app.stripstrips.activity.TripsDiaryDetailsLocationActivity
import com.app.stripstrips.databinding.ActivityTripsdiaryBinding
import com.app.stripstrips.model.tripsDiaryListingApi.TripsDiaryDataItem
import com.app.stripstrips.network.OnDeleteClick

class TripsDiaryFragmentAdapter(
    val activity: FragmentActivity, private val trisDiaryList: ArrayList<TripsDiaryDataItem>,
    private val onDeleteClick: OnDeleteClick
) : RecyclerView.Adapter<TripsDiaryFragmentAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ActivityTripsdiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("DiscouragedPrivateApi")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {

            binding.tvLondonDiaries.text = trisDiaryList[position].tripName
            binding.descTV.text = trisDiaryList[position].tripDescreption
            binding.ivProfile.load(trisDiaryList[position].tripImage) {
                placeholder(R.drawable.human)
            }

            binding.viewClick.setOnClickListener {

                if (trisDiaryList[position].totalPostCount == "0") {
                    Toast.makeText(activity, "No trip add from the trip diary", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(activity, TripsDiaryDetailsLocationActivity::class.java)
                    intent.putExtra("Trip-Diary-Id", trisDiaryList[position].diaryId)
                    activity.startActivity(intent)
                }
            }

            binding.viewClick.setOnLongClickListener {
                onDeleteClick.selectDelete(position, binding.item)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return trisDiaryList.size
    }

    inner class ViewHolder(val binding: ActivityTripsdiaryBinding) :
        RecyclerView.ViewHolder(binding.root)
}