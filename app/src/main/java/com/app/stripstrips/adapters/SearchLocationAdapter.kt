package com.naijadocs.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.stripstrips.R
import com.app.stripstrips.model.LatlongApiModel.PredictionsItem
import com.app.stripstrips.network.LocationtemClickListner
import kotlin.collections.ArrayList


class SearchLocationAdapter(var mActivity: Activity?, var mArrayList:  ArrayList<PredictionsItem?>?, var mLocationtemClickListner : LocationtemClickListner) :
    RecyclerView.Adapter<SearchLocationAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(mActivity).inflate(R.layout.item_search_location, null)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var mPredictionsItem : PredictionsItem = mArrayList!!.get(position)!!
        holder.txtTitleTV.setText(mPredictionsItem.structuredFormatting?.mainText)
        holder.txtDescriptionTV.setText(mPredictionsItem.structuredFormatting?.secondaryText)
        holder.itemView.setOnClickListener{
            mLocationtemClickListner.onItemClickListner(mPredictionsItem)
        }
    }

    override fun getItemCount(): Int {
        if (mArrayList != null && mArrayList!!.size > 0)
            return mArrayList!!.size
        else
            return 0
    }

    class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var txtTitleTV: TextView
        var txtDescriptionTV: TextView

        init {
            txtTitleTV = itemView.findViewById(R.id.txtTitleTV)
            txtDescriptionTV = itemView.findViewById(R.id.txtDescriptionTV)
        }
    }


}

