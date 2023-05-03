package com.wuocdat.moneymanager.View.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wuocdat.moneymanager.Model.Goal
import com.wuocdat.moneymanager.MoneyManagerApplication
import com.wuocdat.moneymanager.Utils.MNConstants
import com.wuocdat.moneymanager.Utils.TimeUtils
import com.wuocdat.moneymanager.ViewModel.ExpenseViewModel
import com.wuocdat.moneymanager.ViewModel.ExpenseViewModelFactory
import com.wuocdat.moneymanager.ViewModel.GoalViewModel
import com.wuocdat.moneymanager.ViewModel.GoalViewModelFactory
import com.wuocdat.roomdatabase.R

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var fabButton: FloatingActionButton

    private lateinit var goalViewModel: GoalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Welcome back Jenny! "

        val sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val firstLoginTime = sharedPreferences.getLong(MNConstants.FIRST_TIME, -1L)

        if (firstLoginTime == -1L && sharedPreferences != null) {
            val sharedPrefEditor = sharedPreferences.edit()
            sharedPrefEditor.putLong(MNConstants.FIRST_TIME, System.currentTimeMillis())
            sharedPrefEditor.apply()
        }

        fabButton = findViewById(R.id.fab)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.background = null

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)

        fabButton.setOnClickListener {
            val intent = Intent(this, NewExpenseActivity::class.java)
            startActivity(intent)
        }

        //check whether current month goal available or not
        val viewModelFactory =
            GoalViewModelFactory(
                (application as MoneyManagerApplication).goalRepository,
                (application as MoneyManagerApplication).repository
            )
        goalViewModel =
            ViewModelProvider(this, viewModelFactory).get(GoalViewModel::class.java)

        val currentMonth = TimeUtils.getCurrentMonth().toInt()
        val currentYear = TimeUtils.getCurrentYear().toInt()

        goalViewModel.getGoalByMonthAndYear(currentMonth, currentYear)
            .observe(this, Observer { goal ->
                if (goal == null) {
                    val goalOfCurrentMonth = Goal(
                        MNConstants.DEFAULT_GOAL_AMOUNT.toLong(),
                        0L,
                        currentMonth,
                        currentYear
                    )
                    goalViewModel.insert(goalOfCurrentMonth)
                }
            })

    }

}