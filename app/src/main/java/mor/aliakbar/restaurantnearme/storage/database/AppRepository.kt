package mor.aliakbar.restaurantnearme.storage.database

import android.content.Context
import androidx.lifecycle.LiveData
import mor.aliakbar.restaurantnearme.storage.database.model.Food
import mor.aliakbar.restaurantnearme.storage.database.model.Meal
import mor.aliakbar.restaurantnearme.storage.database.model.Restaurant
import mor.aliakbar.restaurantnearme.storage.sharedPrefs.PreferencesManager
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject

class AppRepository @Inject constructor(context: Context) {

    companion object {
        private var instance: AppRepository? = null
        fun getInstance(context: Context): AppRepository {
            if (instance == null) {
                instance = AppRepository(context)
            }
            return instance as AppRepository
        }
    }

    var shp = PreferencesManager(context)
    private var db: AppDatabase = AppDatabase.getInstance(context)!!
    var executor: Executor = Executors.newSingleThreadExecutor()

    fun insertRestaurant(restaurant: Restaurant) {
        executor.execute { db.restaurantDAO.insertRestaurant(restaurant) }
    }

    fun insertAllRestaurant(restaurants: ArrayList<Restaurant>) {
        executor.execute { db.restaurantDAO.insertAll(restaurants) }
    }

    fun deleteAllRestaurants() {
        executor.execute { db.restaurantDAO.deleteAll() }
    }

    fun getRestaurantById(id: Long): Restaurant {
        return db.restaurantDAO.getRestaurantById(id)
    }

    fun getAllRestaurant(): LiveData<List<Restaurant>> {
        return db.restaurantDAO.getAll()
    }

    fun getAllRestaurants(): List<Restaurant> {
        return db.restaurantDAO.getAllRest()
    }

    fun insertFood(food: Food) {
        executor.execute { db.foodDAO.insertFood(food) }
    }

    fun insertAllFood(foods: ArrayList<Food>) {
        executor.execute { db.foodDAO.insertAll(foods) }
    }

    fun getFoodById(id: Long, restaurantId: Long): Food {
        return db.foodDAO.getFoodById(id, restaurantId)
    }

    fun getFoodByRestaurantId(restaurantId: Long): List<Food> {
        return db.foodDAO.getFoodByRestaurantId(restaurantId)
    }

    fun saveIsFakeDataAdd(isFakeDataAdd: Boolean) {
        shp.saveIsFakeDataAdd(isFakeDataAdd)
    }

    fun loadIsFakeDataAdd(): Boolean? {
        return shp.loadIsFakeDataAdd()
    }

    fun updateColumnDistanceWithMe(distanceWithMe: Long, id: Long) {
        executor.execute { db.restaurantDAO.update(distanceWithMe, id) }

    }

    fun insertAllMeals(meals: ArrayList<Meal>) {
        executor.execute { db.mealDAO.insertAll(meals) }
    }

    fun getMealsByRestaurantId(restaurantId: Long): List<Meal> {
        return db.mealDAO.getMealsByRestaurantId(restaurantId)
    }
    fun deleteAllMeals() {
        executor.execute { db.mealDAO.deleteAll() }
    }

}