package com.wuocdat.moneymanager.View.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.wuocdat.moneymanager.Adapters.TabAdapter
import com.wuocdat.moneymanager.Services.Database
import com.wuocdat.moneymanager.Utils.StringUtils
import com.wuocdat.moneymanager.Utils.TimeUtils
import com.wuocdat.moneymanager.ViewModel.GoalViewModel
import com.wuocdat.roomdatabase.R

class OverviewFragment : Fragment() {

    private lateinit var totalMoneyTextView: TextView
    private lateinit var progress: LinearProgressIndicator
    private lateinit var currentDateView: TextView
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var watchLayout: LinearLayout

    private lateinit var goalVM: GoalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //find views
        totalMoneyTextView = view.findViewById(R.id.overview_fragment_textview_total)
        progress = view.findViewById(R.id.overview_fragment_process)
        currentDateView = view.findViewById(R.id.overview_fragment_current_date)
        viewPager2 = view.findViewById(R.id.overview_fragment_view_pager)
        tabLayout = view.findViewById(R.id.overview_fragment_tab_layout)
        watchLayout = view.findViewById(R.id.frag_watch_goals_layout)

        //set view models
        goalVM = Database.getGoalViewModel(this, requireActivity().application)

        //setup tabLayout
        val adapter = TabAdapter(requireActivity())
        viewPager2.isUserInputEnabled = false
        viewPager2.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> "EXPENSES"
                1 -> "GOALS"
                else -> "DID YOU KNOW?"
            }
        }.attach()

        //set text to view
        currentDateView.text = TimeUtils.timeFormat(System.currentTimeMillis(), "dd, MMMM yyyy")

        //handle watch goals
        watchLayout.setOnClickListener {
            val goalTab = tabLayout.getTabAt(1)
            goalTab?.select()
        }

        //set goal progress
        val currentMonth = TimeUtils.getCurrentMonth().toInt()
        val currentYear = TimeUtils.getCurrentYear().toInt()
        goalVM.getGoalByMonthAndYear(currentMonth, currentYear)
            .observe(requireActivity()) { goal ->
                if (goal !== null) {
                    Log.d("total", goal.currentAmount.toString())
                    totalMoneyTextView.text =
                        StringUtils.convertToCurrencyFormat(goal.currentAmount)
                    progress.progress =
                        ((goal.currentAmount.toDouble() / goal.targetAmount.toDouble()) * 100).toInt()
                } else {
                    progress.progress = 0
                }
            }
    }

}