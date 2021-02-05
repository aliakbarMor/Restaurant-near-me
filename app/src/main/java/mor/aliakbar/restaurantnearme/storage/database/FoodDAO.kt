package mor.aliakbar.restaurantnearme.storage.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mor.aliakbar.restaurantnearme.storage.database.model.Food

@Dao
interface FoodDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFood(food: Food)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(foods: ArrayList<Food>)

    @Query("SELECT * FROM foods WHERE id = :id and restaurantId = :restaurantId")
    fun getFoodById(id: Long, restaurantId: Long): Food

    @Query("SELECT * FROM foods WHERE restaurantId = :restaurantId")
    fun getFoodByRestaurantId(restaurantId: Long): List<Food>

}