package com.wuocdat.moneymanager.View.Activity

import android.app.AlertDialog
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davidmiguel.numberkeyboard.NumberKeyboard
import com.davidmiguel.numberkeyboard.NumberKeyboardListener
import com.google.android.material.textfield.TextInputEditText
import com.wuocdat.moneymanager.Adapters.CategoryAdapter
import com.wuocdat.moneymanager.Interfaces.OnItemSelectedListener
import com.wuocdat.moneymanager.Model.Expense
import com.wuocdat.moneymanager.MoneyManagerApplication
import com.wuocdat.moneymanager.Utils.StringUtils
import com.wuocdat.moneymanager.Utils.TimeUtils
import com.wuocdat.moneymanager.ViewModel.ExpenseViewModel
import com.wuocdat.moneymanager.ViewModel.ExpenseViewModelFactory
import com.wuocdat.moneymanager.ViewModel.GoalViewModel
import com.wuocdat.moneymanager.ViewModel.GoalViewModelFactory
import com.wuocdat.roomdatabase.R

class NewExpenseActivity : AppCompatActivity(), NumberKeyboardListener, OnItemSelectedListener {

    private lateinit var textView: TextView
    private lateinit var numberKeyboard: NumberKeyboard
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: CategoryAdapter
    private lateinit var editIcon: ImageView
    private lateinit var title: TextView

    private var amount: Long = 0L
    var position: Int = -1
    var titleString = "New expense"

    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var goalViewModel: GoalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_expense)

        //view model
        val viewModelFactory =
            ExpenseViewModelFactory((application as MoneyManagerApplication).repository)
        expenseViewModel =
            ViewModelProvider(this, viewModelFactory).get(ExpenseViewModel::class.java)
        val goalViewModelFactory =
            GoalViewModelFactory((application as MoneyManagerApplication).goalRepository)
        goalViewModel =
            ViewModelProvider(this, goalViewModelFactory).get(GoalViewModel::class.java)

        //findId
        textView = findViewById(R.id.new_expense_textView)
        numberKeyboard = findViewById(R.id.new_expense_numberKeyboard)
        recyclerView = findViewById(R.id.new_expense_recyclerView)
        editIcon = findViewById(R.id.new_expense_edit_icon)
        title = findViewById(R.id.new_expense_title)

        //dialog
        editIcon.setOnClickListener {
            setDialog()
        }

        numberKeyboard.setLeftAuxButtonIcon(R.drawable.ic_playlist_add)

        recyclerView.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

        adapter = CategoryAdapter(StringUtils.categories, this, true, this)
        recyclerView.adapter = adapter

        numberKeyboard.setListener(this)

    }

    private fun setDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("Enter expense title")
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_with_edittext, null)
        val editText: TextInputEditText = dialogLayout.findViewById(R.id.alert_dialog_editText)
        builder.setView(dialogLayout)
        builder.setPositiveButton("OK") { dialogInterface, i ->
            if (editText.text.toString().isNotEmpty()) {
                titleString = editText.text.toString()
                title.text = StringUtils.capFirstCharacter(titleString)
            }
        }
        builder.show()
    }

    override fun onLeftAuxButtonClicked() {

        if (amount != 0L && position != -1) {
            val expense = Expense(
                titleString,
                "todo",
                System.currentTimeMillis(),
                amount,
                StringUtils.categories[position].categoryName
            )

            val currentMonth = TimeUtils.getCurrentMonth().toInt()
            val currentYear = TimeUtils.getCurrentYear().toInt()
            expenseViewModel.insert(expense).invokeOnCompletion { cause: Throwable? ->
                if (cause == null) {
                    goalViewModel.updateAmountByMonthAndYear(amount, currentMonth, currentYear)
                }
            }
            finish()
        } else
            Toast.makeText(
                applicationContext,
                "Please fill out the information completely!",
                Toast.LENGTH_LONG
            )
                .show()
    }

    override fun onNumberClicked(number: Int) {
        amount = if (amount == 0L) {
            number.toLong()
        } else {
            (amount.toString() + number.toString()).toLong()
        }
        textView.text = StringUtils.convertToCurrencyFormat(amount)

    }

    override fun onRightAuxButtonClicked() {
        amount = if (amount < 10L) {
            0L
        } else {
            val numberString = amount.toString()
            numberString.substring(0, numberString.length - 1).toLong()
        }
        textView.text = StringUtils.convertToCurrencyFormat(amount)
    }

    override fun onItemSelected(position: Int) {
        this.position = position
    }

}