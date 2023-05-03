package com.wuocdat.moneymanager.View.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wuocdat.moneymanager.Adapters.GoalAdapter
import com.wuocdat.moneymanager.Services.Database
import com.wuocdat.roomdatabase.R

class GoalFragment : Fragment() {

    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.goal_fragment_recyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val adapter = GoalAdapter()
        recyclerView.adapter = adapter

        Database.getGoalViewModel(requireActivity(), requireActivity().application).getAllGoals()
            .observe(requireActivity(), Observer { goals ->
                if (goals != null) {
                    adapter.setGoalList(goals.reversed())
                }
            })

    }

}