package com.xan.nrcdemo

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.xan.nrcdemo.nrc.nrcFragment
import com.xan.nrcdemo.passport.passportFragment

class FragmentAdapter(activity: AppCompatActivity, val itemsCount: Int) :
    FragmentStateAdapter(activity) {
    lateinit var fragment:Fragment
    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> fragment =  nrcFragment()
            1 -> fragment = passportFragment()
        }
        return fragment
    }
}