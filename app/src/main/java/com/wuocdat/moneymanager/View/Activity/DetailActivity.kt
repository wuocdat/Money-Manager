package com.wuocdat.moneymanager.View.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.wuocdat.moneymanager.Adapters.ExpenseAdapter
import com.wuocdat.moneymanager.Data.DetailInterface
import com.wuocdat.moneymanager.MoneyManagerApplication
import com.wuocdat.moneymanager.Services.Database
import com.wuocdat.moneymanager.Utils.StringUtils
import com.wuocdat.moneymanager.Utils.TimeUtils
import com.wuocdat.moneymanager.ViewModel.ExpenseViewModel
import com.wuocdat.moneymanager.ViewModel.ExpenseViewModelFactory
import com.wuocdat.moneymanager.ViewModel.GoalViewModel
import com.wuocdat.roomdatabase.R
import com.wuocdat.roomdatabase.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity(), DetailInterface {

    private lateinit var binding: ActivityDetailBinding

    private lateinit var openCalendarButton: ImageView
    private lateinit var dateRangeTextView: TextView
    private lateinit var menu: AutoCompleteTextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var endDateTV: TextView

    private var selectedCategory: String = "all"
    private var currentItemPos: Int = 0

    private var startTime: Long = MaterialDatePicker.thisMonthInUtcMilliseconds()
    private var endTime: Long = MaterialDatePicker.todayInUtcMilliseconds()

    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var goalViewModel: GoalViewModel
    private lateinit var dialog: BottomSheetDialog

    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val defaultCategory = intent.getStringExtra("category")
        if (defaultCategory !== null) selectedCategory = defaultCategory


        dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
        val closeImg: ImageView = view.findViewById(R.id.sheet_close_img)
        val editLayout: LinearLayout = view.findViewById(R.id.sheet_edit_layout)
        val deleteLayout: LinearLayout = view.findViewById(R.id.sheet_delete_layout)

        closeImg.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(view)
        editLayout.setOnClickListener {
            val intent = Intent(this@DetailActivity, ExpenseActivity::class.java)
            intent.putExtra("id", expenseAdapter.getExpense(currentItemPos).id)
            intent.putExtra("edit_screen", true)
            startActivity(intent)
        }
        deleteLayout.setOnClickListener {
            val currentExpense = expenseAdapter.getExpense(currentItemPos)
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
            dialog.dismiss()
        }

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
        expenseAdapter = ExpenseAdapter(this, this)
        recyclerView.adapter = expenseAdapter

        setTextToPickerTextView()

        //setMenu
        val options =
            listOf(getString(R.string.all_categories)).plus(StringUtils.categories.map { item ->
                resources.getString(
                    item.nameResId
                )
            })
        val adapter = ArrayAdapter(this, R.layout.item_dropdown_category, options)
        menu.setAdapter(adapter)
        menu.setText(
            resources.getString(StringUtils.getNameResIdByCategoryName(defaultCategory)),
            false
        )
        menu.setOnItemClickListener { _, _, position, _ ->
            selectedCategory =
                if (position == 0) "all" else StringUtils.categories[position - 1].categoryName
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

    override fun onClickItem(pos: Int) {
        val intent = Intent(this@DetailActivity, ExpenseActivity::class.java)
        intent.putExtra("id", expenseAdapter.getExpense(pos).id)
        startActivity(intent)
    }

    override fun onClickMoreItem(pos: Int) {
        dialog.show()
        currentItemPos = pos
    }
}






