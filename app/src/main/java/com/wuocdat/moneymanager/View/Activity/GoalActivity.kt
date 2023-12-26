package com.wuocdat.moneymanager.View.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.wuocdat.moneymanager.Utils.MNConstants
import com.wuocdat.moneymanager.Utils.StringUtils
import com.wuocdat.roomdatabase.R
import com.wuocdat.roomdatabase.databinding.ActivityGoalBinding

class GoalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoalBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.your_monthly_goal)

        // get saved goal value
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val savedGoalValue = sharedPreferences.getLong(
            resources.getString(R.string.goal_share_preference_key),
            MNConstants.DEFAULT_GOAL_AMOUNT
        )
        binding.activityGoalInputEt.setText(savedGoalValue.toString())

        val withCancelBtn =
            intent.getBooleanExtra(MNConstants.WITH_CANCEL_BTN_GOAL_ACTIVITY_KEY, false)
        if (!withCancelBtn) {
            binding.goalActivityCancelBtn.visibility = View.GONE
            binding.goalActivitySaveBtn.text = resources.getString(R.string.next)
        }

        binding.goalActivityCancelBtn.setOnClickListener { handleFinish() }

        binding.activityGoalInputEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.goalActivityGoalInputLayout.helperText =
                    StringUtils.convertToCurrencyFormat(
                        if (text.toString().isEmpty()) 0 else text.toString().toLong()
                    )
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        binding.goalActivitySaveBtn.setOnClickListener {
            updateGoal()
        }
    }

    private fun updateGoal() {
        val inputValue = binding.activityGoalInputEt.text.toString()
        val newGoal = inputValue.toLongOrNull()
        if (newGoal != null && newGoal != 0L) {
            val sharedPrefEditor = sharedPreferences.edit()
            sharedPrefEditor.putLong(
                resources.getString(R.string.goal_share_preference_key),
                newGoal
            )
            sharedPrefEditor.apply()
            handleFinish()
        }
    }

    private fun handleFinish() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}