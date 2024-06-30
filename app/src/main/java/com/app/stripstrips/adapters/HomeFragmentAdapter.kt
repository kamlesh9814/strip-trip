package com.app.stripstrips.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.stripstrips.commonInterface.OnClickLishner
import com.app.stripstrips.databinding.ActivityImageVideoBinding
import com.app.stripstrips.model.homeApi.HomeDataItem
import com.google.android.exoplayer2.ui.StyledPlayerView

class HomeFragmentAdapter(
    val context: Context, private val imageCollection: ArrayList<HomeDataItem>?,
    private val callBack: AdapterOnClick,
    val OnClickLishner: OnClickLishner
) :
    RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ActivityImageVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.starLike.setOnClickListener {
         OnClickLishner.onClick(position, "1")

        }
        holder.binding.IVQuestion.setOnClickListener {
            OnClickLishner.onClick(position, "0")
        }
    }

    override fun getItemCount(): Int {
        return imageCollection!!.size

    }

        interface AdapterOnClick {
        fun onClick(position: Int, exoPlayersViews: StyledPlayerView)
    }

    inner class ViewHolder(val binding: ActivityImageVideoBinding) :
        RecyclerView.ViewHolder(binding.root)
}