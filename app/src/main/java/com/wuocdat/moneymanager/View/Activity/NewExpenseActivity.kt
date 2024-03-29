package com.wuocdat.moneymanager.View.Activity

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_KEYBOARD
import com.google.android.material.timepicker.TimeFormat
import com.wuocdat.moneymanager.Adapters.CategoryAdapter
import com.wuocdat.moneymanager.Interfaces.OnItemSelectedListener
import com.wuocdat.moneymanager.Model.Expense
import com.wuocdat.moneymanager.MoneyManagerApplication
import com.wuocdat.moneymanager.Services.Database
import com.wuocdat.moneymanager.Utils.StringUtils
import com.wuocdat.moneymanager.ViewModel.ExpenseViewModel
import com.wuocdat.moneymanager.ViewModel.ExpenseViewModelFactory
import com.wuocdat.moneymanager.ViewModel.GoalViewModel
import com.wuocdat.moneymanager.ViewModel.GoalViewModelFactory
import com.wuocdat.roomdatabase.R
import com.wuocdat.roomdatabase.databinding.ActivityNewExpenseBinding
import java.util.Calendar


class NewExpenseActivity : AppCompatActivity(), OnItemSelectedListener {

    lateinit var binding: ActivityNewExpenseBinding

    lateinit var adapter: CategoryAdapter

    var position: Int = -1

    private val calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH) + 1
    private var day = calendar.get(Calendar.DAY_OF_MONTH)
    private var hour = calendar.get(Calendar.HOUR_OF_DAY)
    private var minute = calendar.get(Calendar.MINUTE)

    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var goalViewModel: GoalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //set view
        binding = ActivityNewExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //hide keyboard when focus outside of editText
        hideSoftKeyboard()

        //view model
        val viewModelFactory =
            ExpenseViewModelFactory((application as MoneyManagerApplication).repository)
        expenseViewModel =
            ViewModelProvider(this, viewModelFactory)[ExpenseViewModel::class.java]
        val goalViewModelFactory =
            GoalViewModelFactory(
                (application as MoneyManagerApplication).goalRepository,
                (application as MoneyManagerApplication).repository
            )
        goalViewModel =
            ViewModelProvider(this, goalViewModelFactory)[GoalViewModel::class.java]

        binding.newExpenseRC.layoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false)

        adapter = CategoryAdapter(StringUtils.categories, this, true, this)
        binding.newExpenseRC.adapter = adapter

        resetDateTV()

        binding.chooseDateButton.setOnClickListener {

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, monthOfYear, dayOfMonth ->
                    year = selectedYear
                    month = monthOfYear + 1
                    day = dayOfMonth
                    resetDateTV()
                },
                year,
                month - 1,
                day
            )
            datePickerDialog.show()
        }

        binding.chooseTimeButton.setOnClickListener {
            val picker =
                MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(hour)
                    .setMinute(minute)
                    .setTitleText("Select Appointment time")
                    .setInputMode(INPUT_MODE_KEYBOARD)
                    .build()
            picker.addOnPositiveButtonClickListener {
                hour = picker.hour
                minute = picker.minute
                resetDateTV()
            }
            picker.show(supportFragmentManager, "tag")
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }

        binding.AmountTrans.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.newExpActivityAmountInputLayout.helperText =
                    StringUtils.convertToCurrencyFormat(
                        if (text.toString().isEmpty()) 0 else text.toString().toLong()
                    )
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.saveButton.setOnClickListener {
            val nameTrans = binding.nameTransaction.text.toString()
            val transDescription = binding.descriptionTrans.text.toString()
            val transAmount = binding.AmountTrans.text.toString()

            calendar.set(year, month - 1, day, hour, minute)
            val createdTime = calendar.timeInMillis

            if (nameTrans.isNotEmpty() && position != -1 && transAmount.isNotEmpty()) {
                val newExpense = Expense(
                    nameTrans,
                    transDescription,
                    createdTime,
                    transAmount.toLong(),
                    StringUtils.categories[position].categoryName
                )
                expenseViewModel.insert(newExpense).invokeOnCompletion { cause ->
                    if (cause == null) {
                        Database.getGoalViewModel(this, application)
                            .updateGoalByMonthAndYear(month, year)
                    }
                }

                Toast.makeText(
                    applicationContext,
                    getString(R.string.created_exp_successfully),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else
                Toast.makeText(
                    applicationContext,
                    getString(R.string.lack_of_info),
                    Toast.LENGTH_SHORT
                ).show()
        }

    }


    override fun onItemSelected(position: Int) {
        this.position = position
    }

    private fun resetDateTV() {
        binding.dateTV.text =
            "$hour:${String.format("%02d", minute)} ${
                String.format(
                    "%02d",
                    day
                )
            }/${String.format("%02d", month)}/$year"
    }

    private fun hideSoftKeyboard() {
        binding.linearLayout.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
            false
        }
    }

}