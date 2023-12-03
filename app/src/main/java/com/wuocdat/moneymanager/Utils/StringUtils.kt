package com.wuocdat.moneymanager.Utils

import com.wuocdat.moneymanager.Data.CategoryItem
import com.wuocdat.roomdatabase.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Currency
import java.util.Date
import java.util.Locale

class StringUtils {

    companion object {

        fun convertToCurrencyFormat(number: Long): String {
            val format = NumberFormat.getCurrencyInstance()

            return format.format(number)
        }

        val categories = arrayListOf(
            CategoryItem(
                R.drawable.ic_medicine,
                "Medicine",
                R.color.red500,
                R.color.red200
            ),
            CategoryItem(
                R.drawable.ic_shopping,
                "Shopping",
                R.color.yellow500,
                R.color.yellow200
            ),
            CategoryItem(
                R.drawable.ic_device,
                "Device",
                R.color.green500,
                R.color.green200
            ),
            CategoryItem(
                R.drawable.ic_commuting,
                "Commuting",
                R.color.purple500,
                R.color.purple200
            ),
            CategoryItem(
                R.drawable.ic_drink,
                "Drink",
                R.color.blue_green500,
                R.color.blue_green200
            ),
            CategoryItem(
                R.drawable.ic_fashion,
                "Fashion",
                R.color.pink500,
                R.color.pink200
            ),
            CategoryItem(
                R.drawable.ic_food,
                "Food",
                R.color.darker_blue500,
                R.color.darker_blue200
            ),
            CategoryItem(
                R.drawable.ic_haircut,
                "Haircut",
                R.color.darker_green500,
                R.color.darker_green200
            ),
            CategoryItem(
                R.drawable.ic_repair,
                "Repair",
                R.color.darker_grey500,
                R.color.darker_grey200
            ),
            CategoryItem(
                R.drawable.ic_other,
                "Others",
                R.color.blue500,
                R.color.blue200
            ),
        )

        fun capFirstCharacter(text: String): String {
            return text.replaceFirstChar { it.uppercase() }
        }

        val MONTHS_OF_YEAR = arrayListOf(
            "Jan",
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        )
    }

}