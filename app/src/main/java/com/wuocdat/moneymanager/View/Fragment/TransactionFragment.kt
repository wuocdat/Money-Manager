package com.wuocdat.moneymanager.View.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wuocdat.moneymanager.Adapters.CategoryAdapter
import com.wuocdat.moneymanager.Adapters.StatisticAdapter
import com.wuocdat.moneymanager.Interfaces.OnItemSelectedListener
import com.wuocdat.moneymanager.Services.Database
import com.wuocdat.moneymanager.Utils.StringUtils
import com.wuocdat.moneymanager.Utils.TimeUtils
import com.wuocdat.moneymanager.View.Activity.DetailActivity
import com.wuocdat.roomdatabase.R

class TransactionFragment : Fragment(), OnItemSelectedListener {

    lateinit var categoryRView: RecyclerView
    lateinit var statisticRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // find views
        categoryRView = view.findViewById(R.id.transaction_fragment_category_recycler_view)
        statisticRecyclerView = view.findViewById(R.id.transaction_fragment_statistic_rv)

        // set category recyclerview
        val categoryAdapter = CategoryAdapter(StringUtils.categories, requireContext(), false, this)
        categoryRView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        categoryRView.adapter = categoryAdapter

        // setup statistic recyclerview
        statisticRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val statisticAdapter = StatisticAdapter(requireActivity())
        statisticRecyclerView.adapter = statisticAdapter

        // fetch category statistic and add data to recyclerview
        Database.getExpenseViewModel(this, requireActivity().application)
            .getCategoryStatisticByMonthAndYear(TimeUtils.getMonthAndYearStr())
            .observe(requireActivity(), Observer { statistics ->
                statisticAdapter.setStatistic(statistics)
            })
    }

    override fun onItemSelected(position: Int) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra("category", StringUtils.categories[position].categoryName)
        startActivity(intent)
    }
}