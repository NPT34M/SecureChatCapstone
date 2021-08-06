package com.demo.securechatcapstone.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPageAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {
    private val fragments = ArrayList<Fragment>()
    private val titles = ArrayList<String>()
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments.get(position)
    }

    public fun addFragment(f: Fragment, title: String) {
        fragments.add(f)
        titles.add(title)
    }

    public fun getTitle(position: Int): CharSequence {
        return titles.get(position)
    }
}