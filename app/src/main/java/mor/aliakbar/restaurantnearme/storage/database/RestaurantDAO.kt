package mor.aliakbar.restaurantnearme.storage.database

import androidx.lifecycle.LiveData
import androidx.room.*
import mor.aliakbar.restaurantnearme.storage.database.model.Restaurant

@Dao
interface RestaurantDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRestaurant(restaurant: Restaurant)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(restaurants: ArrayList<Restaurant>)

    @Query("DELETE FROM restaurants")
    fun deleteAll()

    @Query("SELECT * FROM restaurants WHERE id = :id")
    fun getRestaurantById(id: Long): Restaurant

    @Query("SELECT * FROM restaurants")
    fun getAll(): LiveData<List<Restaurant>>

    @Query("SELECT * FROM restaurants")
    fun getAllRest(): List<Restaurant>

    @Query("UPDATE restaurants SET distanceWithMe=:distanceWithMe WHERE id = :id")
    fun update(distanceWithMe: Long, id: Long)


}