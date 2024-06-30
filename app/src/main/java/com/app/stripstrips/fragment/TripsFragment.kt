package com.app.stripstrips.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.stripstrips.adapters.TabLayoutAdapter
import com.app.stripstrips.databinding.FragmentTripsBinding
import com.google.android.material.tabs.TabLayout

class TripsFragment : Fragment() {
    lateinit var binding: FragmentTripsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTripsBinding.inflate(inflater, container, false)
        tabLayout()
        return binding.root
    }

    private fun tabLayout() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Trips"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Trips shared by others"))
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = TabLayoutAdapter(
            requireActivity(),
            requireActivity().supportFragmentManager,
            binding.tabLayout.tabCount
        )
        binding.viewPager.adapter = adapter
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }
}