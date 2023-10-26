package com.wuocdat.moneymanager.View.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.wuocdat.moneymanager.Adapters.ExpenseAdapter
import com.wuocdat.moneymanager.Helper.SwipeHelper
import com.wuocdat.moneymanager.MoneyManagerApplication
import com.wuocdat.moneymanager.Services.Database
import com.wuocdat.moneymanager.Utils.StringUtils
import com.wuocdat.moneymanager.Utils.TimeUtils
import com.wuocdat.moneymanager.ViewModel.ExpenseViewModel
import com.wuocdat.moneymanager.ViewModel.ExpenseViewModelFactory
import com.wuocdat.moneymanager.ViewModel.GoalViewModel
import com.wuocdat.roomdatabase.R

class DetailActivity : AppCompatActivity() {

    private lateinit var openCalendarButton: ImageView
    private lateinit var dateRangeTextView: TextView
    private lateinit var menu: AutoCompleteTextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var endDateTV: TextView

    private var selectedCategory: String = "all"

    private var startTime: Long = MaterialDatePicker.thisMonthInUtcMilliseconds()
    private var endTime: Long = MaterialDatePicker.todayInUtcMilliseconds()

    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var goalViewModel: GoalViewModel

    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val defaultCategory = intent.getStringExtra("category")
        if (defaultCategory !== null) selectedCategory = defaultCategory

        //view model
        val viewModelFactory =
            ExpenseViewModelFactory((application as MoneyManagerApplication).repository)
        expenseViewModel =
            ViewModelProvider(this, viewModelFactory)[ExpenseViewModel::class.java]
        goalViewModel = Database.getGoalViewModel(this, application)

        //find by id
        openCalendarButton = findViewById(R.id.detail_activity_calendar_imageView)
        dateRangeTextView = findViewById(R.id.detail_activity_date_range_textView)
        menu = findViewById(R.id.detail_activity_menu)
        recyclerView = findViewById(R.id.detail_activity_recyclerView)
        endDateTV = findViewById(R.id.detail_activity_end_date_textView)

        //config recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        expenseAdapter = ExpenseAdapter(this)
        recyclerView.adapter = expenseAdapter

        setTextToPickerTextView()

        //setMenu
        val options =
            listOf(getString(R.string.all_categories)).plus(StringUtils.categories.map { item -> item.categoryName })
        val adapter = ArrayAdapter(this, R.layout.item_dropdown_category, options)
        menu.setAdapter(adapter)
        menu.setText(defaultCategory ?: getString(R.string.all_categories), false)
        menu.setOnItemClickListener { parent, _, position, _ ->
            val selectedItem = parent.getItemAtPosition(position).toString()
            selectedCategory = if (selectedItem == "All Categories") "all" else selectedItem
            expenseViewModel.setCategory(selectedCategory)
        }

        openCalendarButton.setOnClickListener {
            showDateRangePicker()
        }

        //get expenses
        expenseViewModel.getExpensesByCreatedTimeAndCategory(startTime, endTime, selectedCategory)
            .observe(this) { expenses ->
                expenseAdapter.setExpense(expenses)
            }


        //swipe
        object : SwipeHelper(this, recyclerView, false) {

            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder?,
                underlayButtons: MutableList<UnderlayButton>?
            ) {

                // Flag Button
                underlayButtons?.add(UnderlayButton(
                    "Delete",
                    AppCompatResources.getDrawable(
                        this@DetailActivity,
                        R.drawable.ic_delete_2
                    ),
                    Color.parseColor("#b2bec3"), Color.parseColor("#34495e")

                ) { pos: Int ->
//                        adapter.notifyItemChanged(pos)
                    val currentExpense = expenseAdapter.getExpense(pos)
                    expenseViewModel.delete(currentExpense)
                        .invokeOnCompletion { cause: Throwable? ->
                            if (cause == null) {
                                goalViewModel.updateGoalByMonthAndYear(
                                    TimeUtils.timeFormat(currentExpense.createdTime, "MM").toInt(),
                                    TimeUtils.timeFormat(currentExpense.createdTime, "yyyy")
                                        .toInt(),
                                )
                            }
                        }
                })

                // More Button
                underlayButtons?.add(UnderlayButton(
                    "Edit",
                    AppCompatResources.getDrawable(
                        this@DetailActivity,
                        R.drawable.ic_edit_2
                    ),
                    Color.parseColor("#dfe6e9"), Color.parseColor("#34495e")

                ) { pos: Int ->
//                        adapter.notifyItemChanged(pos)
                    val intent = Intent(this@DetailActivity, ExpenseActivity::class.java)
                    intent.putExtra("id", expenseAdapter.getExpense(pos).id)
                    startActivity(intent)
                })
            }
        }
    }

    private fun showDateRangePicker() {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Pick date range")
            .setSelection(Pair(startTime, endTime))
            .build()

        dateRangePicker.show(supportFragmentManager, "date_range_picker")

        dateRangePicker.addOnPositiveButtonClickListener { dateRange ->
            val startDate = dateRange.first
            val endDate = dateRange.second
            onDateRangeSelected(startDate, endDate)
        }
    }

    private fun onDateRangeSelected(startDate: Long?, endDate: Long?) {
        startDate?.let {
            this.startTime = startDate
            expenseViewModel.setStartTime(startDate)
        }
        endDate?.let {
            this.endTime = endDate
            expenseViewModel.setEndTime(endDate)
        }
        setTextToPickerTextView()
    }

    private fun setTextToPickerTextView() {
        dateRangeTextView.text = TimeUtils.timeFormat(startTime, "dd-MM-yyyy")
        endDateTV.text = TimeUtils.timeFormat(endTime, "dd-MM-yyyy")
    }

}






