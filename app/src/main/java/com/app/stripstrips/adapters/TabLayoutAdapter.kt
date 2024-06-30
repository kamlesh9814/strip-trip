package com.app.stripstrips.adapters

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.app.stripstrips.fragment.TripsSharedFragment
import com.app.stripstrips.fragment.TripsTabFragment


@Suppress("DEPRECATION")
class TabLayoutAdapter(
    var mContext: Context, fragmentManager: FragmentManager?, var mTotalTabs: Int,) :FragmentStatePagerAdapter(fragmentManager!!) {

    override fun getItem(position: Int): Fragment {
        Log.d("asasas", position.toString() + "")
        return when (position) {
            0 -> TripsTabFragment()
            1 -> TripsSharedFragment()
            else -> null!!
        }
    }

    override fun getCount(): Int {
        return mTotalTabs
    }
}