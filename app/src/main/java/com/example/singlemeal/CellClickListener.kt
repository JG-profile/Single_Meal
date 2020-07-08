package com.example.singlemeal

import com.example.singlemeal.database.Food
import com.example.singlemeal.database.Meal

interface CellClickListener {
    fun onCellLongClickListener(id: Int, name: String)
    fun onCellClickListener(meal: Meal)
    //fun onFoodClickListener(food: Food)
}