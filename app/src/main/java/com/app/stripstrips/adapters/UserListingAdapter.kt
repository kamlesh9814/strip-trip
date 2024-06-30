package com.app.stripstrips.adapters
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.stripstrips.R
import com.app.stripstrips.databinding.ActivityListBinding
import com.app.stripstrips.model.userListingApi.UserListingDataItem
import com.app.stripstrips.utils.TripSharedInterface

class UserListingAdapter(
    val context: Context,
    private val userShareListing: ArrayList<UserListingDataItem>,
    val OnClickLishner: TripSharedInterface
) : RecyclerView.Adapter<UserListingAdapter.ViewHolder>() {
    var mString: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ActivityListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            binding.descriptionTv.text = userShareListing[position].userName
                binding.emailTv.text = userShareListing[position].email

                holder.binding.ivProfile.load(userShareListing[position].userImage) {
                    error(R.drawable.human)
                }

            if (mString.contains(position.toString())) {
                binding.rememberMeImageView.setImageResource(R.drawable.check_circle)
            } else {
                binding.rememberMeImageView.setImageResource(R.drawable.remember_me_empty)


                binding.item.setOnClickListener {
                    if (mString.contains(position.toString())) {
                        OnClickLishner.selectedShared(
                            position,
                            userShareListing[position].userId.toString()
                        )
                        binding.rememberMeImageView.setImageResource(R.drawable.remember_me_empty)
                        mString.remove(position.toString())

                    } else {
                        OnClickLishner.selectedShared(
                            position,
                            userShareListing[position].userId.toString()
                        )
                        binding.rememberMeImageView.setImageResource(R.drawable.check_circle)
                        mString.add(position.toString())
                    }
                    Log.e("mstring", mString.toString())

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return userShareListing.size
    }

    inner class ViewHolder(val binding: ActivityListBinding) : RecyclerView.ViewHolder(binding.root)
}