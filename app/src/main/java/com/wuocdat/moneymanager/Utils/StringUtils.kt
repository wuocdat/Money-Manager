package com.wuocdat.moneymanager.Utils

import com.wuocdat.moneymanager.Data.CategoryItem
import com.wuocdat.roomdatabase.R
import java.text.NumberFormat

class StringUtils {

    companion object {

        fun convertToCurrencyFormat(number: Long): String {
            val format = NumberFormat.getCurrencyInstance()

            return format.format(number)
        }

        val categories = arrayListOf(
            CategoryItem(
                R.drawable.ic_medicine,
                R.string.medicine,
                "Medicine",
                R.color.red500,
                R.color.red200
            ),
            CategoryItem(
                R.drawable.ic_shopping,
                R.string.shopping,
                "Shopping",
                R.color.yellow500,
                R.color.yellow200
            ),
            CategoryItem(
                R.drawable.ic_device,
                R.string.device,
                "Device",
                R.color.green500,
                R.color.green200
            ),
            CategoryItem(
                R.drawable.ic_commuting,
                R.string.commuting,
                "Commuting",
                R.color.purple500,
                R.color.purple200
            ),
            CategoryItem(
                R.drawable.ic_drink,
                R.string.drink,
                "Drink",
                R.color.blue_green500,
                R.color.blue_green200
            ),
            CategoryItem(
                R.drawable.ic_fashion,
                R.string.fashion,
                "Fashion",
                R.color.pink500,
                R.color.pink200
            ),
            CategoryItem(
                R.drawable.ic_food,
                R.string.food,
                "Food",
                R.color.darker_blue500,
                R.color.darker_blue200
            ),
            CategoryItem(
                R.drawable.ic_haircut,
                R.string.haircut,
                "Haircut",
                R.color.darker_green500,
                R.color.darker_green200
            ),
            CategoryItem(
                R.drawable.ic_repair,
                R.string.repair,
                "Repair",
                R.color.darker_grey500,
                R.color.darker_grey200
            ),
            CategoryItem(
                R.drawable.ic_other,
                R.string.others,
                "Others",
                R.color.blue500,
                R.color.blue200
            ),
        )

        fun getNameResIdByCategoryName(name: String?): Int {
            return categories.find { item -> item.categoryName == name }?.nameResId
                ?: R.string.all_categories
        }

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