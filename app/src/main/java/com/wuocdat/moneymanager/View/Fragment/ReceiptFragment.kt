package com.wuocdat.moneymanager.View.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wuocdat.moneymanager.MoneyManagerApplication
import com.wuocdat.moneymanager.Utils.StringUtils
import com.wuocdat.moneymanager.Utils.TimeUtils
import com.wuocdat.moneymanager.ViewModel.ExpenseViewModel
import com.wuocdat.moneymanager.ViewModel.ExpenseViewModelFactory
import com.wuocdat.roomdatabase.R

class ReceiptFragment : Fragment() {

    private lateinit var dateTextView: TextView
    private lateinit var dateTimeTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var totalTextView: TextView
    private lateinit var moveToEditButton: FloatingActionButton
    private lateinit var cancelBtn: Button

    private lateinit var expenseViewModel: ExpenseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_receipt, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val showEditScreen = requireActivity().intent.getBooleanExtra("edit_screen", false)
        if (showEditScreen) moveToEditScreen(false)

        val viewModelFactory =
            ExpenseViewModelFactory((requireActivity().application as MoneyManagerApplication).repository)
        expenseViewModel =
            ViewModelProvider(this, viewModelFactory)[ExpenseViewModel::class.java]

        dateTextView = view.findViewById(R.id.receipt_fragment_date)
        dateTimeTextView = view.findViewById(R.id.receipt_fragment_date_time)
        titleTextView = view.findViewById(R.id.receipt_fragment_title)
        descriptionTextView = view.findViewById(R.id.receipt_fragment_description)
        totalTextView = view.findViewById(R.id.receipt_fragment_money)
        moveToEditButton = view.findViewById(R.id.receipt_fragment_floatingActionButton)
        cancelBtn = view.findViewById(R.id.receipt_fragment_cancel_btn)

        moveToEditButton.setOnClickListener {
            moveToEditScreen(true)
        }

        cancelBtn.setOnClickListener {
            requireActivity().finish()
        }

        val intent = requireActivity().intent
        val expenseId = intent.getIntExtra("id", 0)

        expenseViewModel.getExpenseById(expenseId).observe(requireActivity()) { expense ->
            dateTextView.text = TimeUtils.timeFormat(expense.createdTime)
            dateTimeTextView.text = TimeUtils.timeFormat(expense.createdTime)
            titleTextView.text = expense.expenseTitle
//            descriptionTextView.text = expense.description
            totalTextView.text = StringUtils.convertToCurrencyFormat(expense.money)
        }
    }

    private fun moveToEditScreen(withBackStack: Boolean) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        val editFragment = EditFragment()
        transaction.replace(R.id.expense_activity_fragment_container, editFragment)
        if (withBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

}