package mor.aliakbar.restaurantnearme.storage.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import mor.aliakbar.restaurantnearme.storage.database.model.Food
import mor.aliakbar.restaurantnearme.storage.database.model.Meal
import mor.aliakbar.restaurantnearme.storage.database.model.Restaurant

@Database(entities = [Restaurant::class, Meal::class, Food::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract val restaurantDAO: RestaurantDAO
    abstract val foodDAO: FoodDAO
    abstract val mealDAO: MealDAO

    companion object {
        private const val DATABASE_NAME = "app.db"

        @Volatile
        private var instance: AppDatabase? = null
        private var LOCK = Any()

        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(LOCK) {
                    if (instance == null) {
                        instance =
                            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                                .build()
                    }
                }
            }
            return instance
        }
    }


}