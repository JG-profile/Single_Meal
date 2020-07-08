package com.example.singlemeal

import com.example.singlemeal.database.Food
import com.example.singlemeal.database.Meal

interface FoodClickListener<T> {
    //fun onFoodLongClickListener(foodId: Int, name: String)
    //fun onMealClickListener(meal: Meal)
    fun onFoodClickListener(food: Food)
}