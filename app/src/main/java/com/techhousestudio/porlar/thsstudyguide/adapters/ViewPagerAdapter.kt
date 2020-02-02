package com.techhousestudio.porlar.thsstudyguide.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    private val mFragmentList = arrayListOf<Fragment>()
    fun addFrag(fragment: Fragment) {
        mFragmentList.add(fragment)
    }

    override fun getItemCount(): Int = mFragmentList.size
    override fun createFragment(position: Int): Fragment = mFragmentList[position]
}