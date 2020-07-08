package com.example.singlemeal.database

import androidx.lifecycle.LiveData
import com.example.singlemeal.database.*

class MealRepository (private val mealDao: MealDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allMealsByDate: LiveData<List<Meal>> = mealDao.getMealsByDate()
    val allFoods: LiveData<List<Food>> = mealDao.getFoods()
    //val allCrossRef: LiveData<List<MealFoodCrossRef>> = mealDao.getCrossRef()

    fun mealWithFoods (mealId: Int): LiveData<List<MealWithFoods>>
    {
        return mealDao.getMealWithFoods(mealId)
    }

    fun allCrossRef (mealId: Int): LiveData<List<MealFoodCrossRef>>
    {
        return mealDao.getCrossRef(mealId)
    }

    fun thisFood (foodId: Int): List<Food>
    {
        return mealDao.getThisFood(foodId)
    }

    suspend fun insert(meal: Meal) {
        mealDao.insert(meal)
    }

    suspend fun insert(food: Food) {
        mealDao.insert(food)
    }

    suspend fun insert(mealFood: MealFoodCrossRef) {
        mealDao.insert(mealFood)
    }

    suspend fun update(meal: Meal) {
        mealDao.update(meal)
    }

    suspend fun update(food: Food) {
        mealDao.update(food)
    }

    suspend fun update(mealFood: MealFoodCrossRef) {
        mealDao.update(mealFood)
    }

    suspend fun deleteSelected(id: Int){
        mealDao.deleteMeal(id)
    }

    suspend fun deleteSelected(mealId: Int, foodId: Int){
        mealDao.deleteMealFood(mealId, foodId)
    }
}