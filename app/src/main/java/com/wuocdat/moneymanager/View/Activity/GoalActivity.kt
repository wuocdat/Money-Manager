package com.wuocdat.moneymanager.View.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.wuocdat.moneymanager.Store.GoalStore
import com.wuocdat.moneymanager.Utils.StringUtils
import com.wuocdat.roomdatabase.R
import com.wuocdat.roomdatabase.databinding.ActivityGoalBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GoalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = resources.getString(R.string.your_monthly_goal)

        getCurrentGoal()

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

    private fun getCurrentGoal() {
        CoroutineScope(Dispatchers.IO).launch {
            val savedGoal = GoalStore.read(this@GoalActivity)
            withContext(Dispatchers.Main) {
                binding.activityGoalInputEt.setText(savedGoal.toString())
            }
        }
    }

    private fun updateGoal() {
        val inputValue = binding.activityGoalInputEt.text.toString()
        val newGoal = inputValue.toIntOrNull()
        if (newGoal != null && newGoal != 0)
            CoroutineScope(Dispatchers.IO).launch {
                GoalStore.save(newGoal, this@GoalActivity)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@GoalActivity,
                        resources.getString(R.string.updated_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}