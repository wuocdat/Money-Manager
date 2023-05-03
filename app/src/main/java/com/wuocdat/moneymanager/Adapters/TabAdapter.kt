package com.wuocdat.moneymanager.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wuocdat.moneymanager.View.Fragment.GoalFragment
import com.wuocdat.moneymanager.View.Fragment.TransactionFragment

class TabAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TransactionFragment()
            1 -> GoalFragment()
            else -> TransactionFragment()
        }
    }
}