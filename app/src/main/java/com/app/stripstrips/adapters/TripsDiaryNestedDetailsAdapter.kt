package com.app.stripstrips.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.stripstrips.activity.TripDiaryActivity
import com.app.stripstrips.databinding.ItemDateRvBinding
import com.app.stripstrips.model.tripsDiaryDetailsApi.TripsData
import com.app.stripstrips.network.PostDeleteInterface
import com.bumptech.glide.Glide

git remote add origin https://github.com/kamlesh9814/strip-trip.git
class TripsDiaryNestedDetailsAdapter(
    private val tripDiaryDetailsList: ArrayList<TripsData>,
    var context: Context,
    var date: String,
    var onDeletePost: PostDeleteInterface,
    val firstposition: Int,

    ) :
    RecyclerView.Adapter<TripsDiaryNestedDetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDateRvBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {

            binding.tvCity.text = tripDiaryDetailsList[holder.bindingAdapterPosition].location
            binding.tvLondonDiaries.text =
                tripDiaryDetailsList[holder.bindingAdapterPosition].postName
            binding.postDescriptionTv.text =
                tripDiaryDetailsList[holder.bindingAdapterPosition].postDescription
            Glide.with(context)
                .load(tripDiaryDetailsList[position].postImage)
                .into(holder.binding.ivProfile)


            holder.itemView.setOnClickListener {
                val intent = Intent(context, TripDiaryActivity::class.java)
                Log.e("TAG", "onBindViewHolder: ${tripDiaryDetailsList[position].postImage}")
                intent.putExtra("post_ids", tripDiaryDetailsList[position].postId)
                intent.putExtra("postNames", tripDiaryDetailsList[position].postName)
                intent.putExtra("postDescriptions", tripDiaryDetailsList[position].postDescription)
                intent.putExtra("locations", tripDiaryDetailsList[position].location)
                intent.putExtra("updatedAts", date)
                intent.putExtra("postImages", tripDiaryDetailsList[position].postImage)
                intent.putExtra("tripImages", "tripImage")
                intent.putExtra("thumbImages", "thumbImage")
                context.startActivity(intent)
            }

           holder.binding.item.setOnLongClickListener {
                onDeletePost.postDelete(firstposition, position, binding.item)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return tripDiaryDetailsList.size
    }

    inner class ViewHolder(val binding: ItemDateRvBinding) :
        RecyclerView.ViewHolder(binding.root)
}