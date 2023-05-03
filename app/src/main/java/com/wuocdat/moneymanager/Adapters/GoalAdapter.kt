package com.wuocdat.moneymanager.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.wuocdat.moneymanager.Model.Goal
import com.wuocdat.moneymanager.Utils.StringUtils
import com.wuocdat.moneymanager.Utils.TimeUtils
import com.wuocdat.roomdatabase.R

class GoalAdapter : RecyclerView.Adapter<ViewHolder>() {

    companion object {
        const val PENDING_GOAL_TYPE = 1
        const val GOAL_TYPE = 2
    }

    private var goals: List<Goal> = ArrayList()

    class GoalViewHolder(itemView: View) : ViewHolder(itemView) {

        val goalMontTV: TextView = itemView.findViewById(R.id.goal_item_month_tv)
        val amountTV: TextView = itemView.findViewById(R.id.goal_item_amount_tv)
        val doneImg: ImageView = itemView.findViewById(R.id.goal_item_done_img)
        val brokeImg: ImageView = itemView.findViewById(R.id.goal_item_broke)
    }

    class PendingGoalViewHolder(itemView: View) : ViewHolder(itemView) {

        val goalMontTV: TextView = itemView.findViewById(R.id.goal_item_month_tv)
        val amountTV: TextView = itemView.findViewById(R.id.goal_item_amount_tv)
        val currentDateTV: TextView = itemView.findViewById(R.id.goal_item_current_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == PENDING_GOAL_TYPE) {
            val view: View =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_pedding_goal, parent, false)
            PendingGoalViewHolder(view)
        } else {
            val view: View =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_goal, parent, false)
            GoalViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentGoal = goals[position]
        if (holder is GoalViewHolder) {
            holder.goalMontTV.text = TimeUtils.timeFormat(
                "${currentGoal.year}-${currentGoal.month}"
            )
            holder.amountTV.text =
                StringUtils.convertToCurrencyFormat(currentGoal.currentAmount)
            if (currentGoal.currentAmount > currentGoal.targetAmount) {
                holder.doneImg.visibility = View.GONE
                holder.brokeImg.visibility = View.VISIBLE
            } else {
                holder.doneImg.visibility = View.VISIBLE
                holder.brokeImg.visibility = View.GONE
            }
        } else if (holder is PendingGoalViewHolder) {
            holder.goalMontTV.text = TimeUtils.timeFormat(
                "${currentGoal.year}-${currentGoal.month}"
            )
            holder.amountTV.text =
                StringUtils.convertToCurrencyFormat(currentGoal.currentAmount)
            holder.currentDateTV.text =
                TimeUtils.timeFormat(System.currentTimeMillis(), "dd, MMMM yyyy")
        }

    }

    override fun getItemCount(): Int = goals.size

    override fun getItemViewType(position: Int): Int {
        val currentGoal = goals[position]
        val currentMonth = TimeUtils.getCurrentMonth().toInt()
        val currentYear = TimeUtils.getCurrentYear().toInt()
        return if (currentGoal.month == currentMonth && currentGoal.year == currentYear)
            PENDING_GOAL_TYPE else GOAL_TYPE
    }

    fun setGoalList(goals: List<Goal>) {
        this.goals = goals
        notifyDataSetChanged()
    }
}