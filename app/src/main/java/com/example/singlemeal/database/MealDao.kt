package com.example.singlemeal.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.singlemeal.database.Food
import com.example.singlemeal.database.Meal
import com.example.singlemeal.database.MealFoodCrossRef
import com.example.singlemeal.database.MealWithFoods

@Dao
interface MealDao {
    @Query("SELECT * from meal_table ORDER BY date ASC")
    fun getMealsByDate(): LiveData<List<Meal>>

    @Query("SELECT * from food_table WHERE servings > 0 ORDER BY date ASC")
    fun getFoods(): LiveData<List<Food>>

    @Query("SELECT * from food_table WHERE foodId = :foodId")
    fun getThisFood(foodId: Int): List<Food>

    @Query("SELECT * from MealFoodCrossRef WHERE mealId = :id")
    fun getCrossRef(id: Int): LiveData<List<MealFoodCrossRef>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(meal: Meal)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(food: Food)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(mealFood: MealFoodCrossRef)

    @Update
    suspend fun update(meal: Meal)

    @Update
    suspend fun update(food: Food)

    @Update
    suspend fun update(mealFood: MealFoodCrossRef)

    @Query("DELETE FROM meal_table")
    suspend fun deleteAllMeals()

    @Query("DELETE FROM food_table")
    suspend fun deleteAllFoods()

    @Query("DELETE FROM mealfoodcrossref")
    suspend fun deleteAllCrossRef()

    @Query("DELETE FROM meal_table WHERE mealId = :mealId")
    suspend fun deleteMeal(mealId: Int)

    @Query("DELETE FROM MealFoodCrossRef WHERE mealId = :mealId AND foodId = :foodId")
    suspend fun deleteMealFood(mealId: Int, foodId: Int)

    @Transaction
    @Query("SELECT * FROM meal_table WHERE mealId = :mealId")
    fun getMealWithFoods(mealId: Int): LiveData<List<MealWithFoods>>
}