package com.example.singlemeal.database

import android.app.Application
import androidx.lifecycle.*
import com.example.singlemeal.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MealViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: MealRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allMealsByDate: LiveData<List<Meal>>
    val allFoods: LiveData<List<Food>>
    lateinit var allMealFoods: LiveData<List<MealWithFoods>>


    init {
        val mealsDao = MealRoomDatabase.getDatabase(application, viewModelScope).mealDao()
        repository = MealRepository(mealsDao)
        allMealsByDate = repository.allMealsByDate
        allFoods = repository.allFoods
    }

    private val nameQueryLiveData = MutableLiveData<Int>()

    val getMealFoods: LiveData<List<MealWithFoods>> = Transformations.switchMap(nameQueryLiveData){
            mealId -> repository.mealWithFoods(mealId)
    }

    fun mealWithFoods(mealId: Int): LiveData<List<MealWithFoods>>
    {
        //nameQueryLiveData.value = mealId
        return repository.mealWithFoods(mealId)
    }

    fun allCrossRef(mealId: Int): LiveData<List<MealFoodCrossRef>>
    {
        return repository.allCrossRef(mealId)
    }

    fun getThisFood(foodId: Int): List<Food>
    {
        return repository.thisFood(foodId)
    }
    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(meal: Meal) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(meal)
    }

    fun update(meal: Meal) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(meal)
    }

    fun insert(food: Food) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(food)
    }

    fun update(food: Food) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(food)
    }

    fun insert(mealFood: MealFoodCrossRef) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(mealFood)
    }

    fun update(mealFood: MealFoodCrossRef) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(mealFood)
    }

    fun deleteSelected(mealId: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteSelected(mealId)
    }

    fun deleteSelected(mealId: Int, foodId: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteSelected(mealId, foodId)
    }
}