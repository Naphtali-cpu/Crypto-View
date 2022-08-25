package com.example.cryptoflow.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.cryptoflow.onboarding.FirstScreen
import com.example.cryptoflow.onboarding.LastScreen
import com.example.cryptoflow.onboarding.SecondScreen

class LauncherViewPager(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val fragments = listOf(
        FirstScreen(),
        SecondScreen(),
        LastScreen()
    )

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }
}