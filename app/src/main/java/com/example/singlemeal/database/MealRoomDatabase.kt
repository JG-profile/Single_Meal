package com.example.singlemeal.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Meal::class, Food::class, MealFoodCrossRef::class), version = 1, exportSchema = false)
public abstract class MealRoomDatabase : RoomDatabase() {

    abstract fun mealDao(): MealDao

    private class MealDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {


        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.mealDao())
                }
            }
        }

        suspend fun populateDatabase(mealDao: MealDao) {
            // Delete all content here.
            mealDao.deleteAllMeals()
            mealDao.deleteAllFoods()
            mealDao.deleteAllCrossRef()

            // Add sample Meals.
            var meal = Meal(
                0,
                "Breakfast",
                "2020/07/02"
            )
            mealDao.insert(meal)
            meal = Meal(0, "Lunch", "2020/07/02")
            mealDao.insert(meal)

            var food = Food(
                0,
                "Rice",
                "Carb",
                "2020/07/01",
                "4"
            )
            mealDao.insert(food)
            food = Food(
                0,
                "chicken",
                "Protein",
                "2020/07/01",
                "5"
            )
            mealDao.insert(food)
            food = Food(
                0,
                "bread",
                "Carb",
                "2020/07/01",
                "2"
            )
            mealDao.insert(food)
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MealRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): MealRoomDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MealRoomDatabase::class.java,
                    "meal_database"
                ).addCallback(
                    MealDatabaseCallback(
                        scope
                    )
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
