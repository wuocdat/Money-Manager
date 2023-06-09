package com.wuocdat.moneymanager.View.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.wuocdat.moneymanager.Model.Expense
import com.wuocdat.moneymanager.MoneyManagerApplication
import com.wuocdat.moneymanager.Utils.StringUtils
import com.wuocdat.moneymanager.Utils.TimeUtils
import com.wuocdat.moneymanager.ViewModel.ExpenseViewModel
import com.wuocdat.moneymanager.ViewModel.ExpenseViewModelFactory
import com.wuocdat.roomdatabase.R

class EditFragment : Fragment() {

    private lateinit var titleEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var moneyEditText: TextInputEditText
    private lateinit var dateEditText: TextInputEditText
    private lateinit var saveButton: Button

    private lateinit var expenseViewModel: ExpenseViewModel

    private var currentCategory = ""
    private var currentId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val viewModelFactory =
            ExpenseViewModelFactory((requireActivity().application as MoneyManagerApplication).repository)
        expenseViewModel =
            ViewModelProvider(this, viewModelFactory)[ExpenseViewModel::class.java]

        titleEditText = view.findViewById(R.id.edit_fragment_title)
        descriptionEditText = view.findViewById(R.id.edit_fragment_description)
        moneyEditText = view.findViewById(R.id.edit_fragment_amount)
        dateEditText = view.findViewById(R.id.edit_fragment_date)
        saveButton = view.findViewById(R.id.edit_fragment_save_button)

        val intent = requireActivity().intent
        val expenseId = intent.getIntExtra("id", 0)

        expenseViewModel.getExpenseById(expenseId).observe(requireActivity()) { expense ->
            titleEditText.setText(expense.expenseTitle)
            descriptionEditText.setText(expense.description)
            moneyEditText.setText(expense.money.toString())
            dateEditText.setText(TimeUtils.timeFormat(expense.createdTime))
            currentCategory = expense.category
            currentId = expense.id
        }

        saveButton.setOnClickListener {

            val newTitle = titleEditText.text.toString()
            val newDescription = descriptionEditText.text.toString()
            val newAmount: Long = moneyEditText.text.toString().toLong()

            val dateString = dateEditText.text.toString()

            if (TimeUtils.isDateFormat(dateString)) {

                val newDate: Long = TimeUtils.textToTimestamp(dateString)
                val dedicateExpense =
                    Expense(newTitle, newDescription, newDate, newAmount, currentCategory)
                dedicateExpense.id = currentId

                expenseViewModel.update(dedicateExpense)

                Toast.makeText(
                    requireContext().applicationContext,
                    "Expense Information is updated",
                    Toast.LENGTH_SHORT
                )
                    .show()
                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack()
            } else
                Toast.makeText(
                    requireContext(),
                    "Date invalid",
                    Toast.LENGTH_SHORT
                )
                    .show()
        }

    }

}