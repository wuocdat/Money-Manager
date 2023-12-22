package com.wuocdat.moneymanager.View.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wuocdat.roomdatabase.R

class GoalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal)
        supportActionBar?.title = resources.getString(R.string.your_monthly_goal)
    }
}