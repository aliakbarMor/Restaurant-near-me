package mor.aliakbar.restaurantnearme.storage.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mor.aliakbar.restaurantnearme.storage.database.model.Meal

@Dao
interface MealDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMeal(meal: Meal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(meals: ArrayList<Meal>)

    @Query("SELECT * FROM meals WHERE restaurantId = :restaurantId")
    fun getMealsByRestaurantId(restaurantId: Long): List<Meal>

    @Query("DELETE FROM meals WHERE isInServer = :isInServer")
    fun deleteAll(isInServer: Boolean = true)

    @Query("DELETE FROM meals WHERE id = :id")
    fun delete(id: Long)
}