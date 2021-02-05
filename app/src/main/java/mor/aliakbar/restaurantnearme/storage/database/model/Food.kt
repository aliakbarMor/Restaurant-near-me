package mor.aliakbar.restaurantnearme.storage.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Foods")
class Food(
    var restaurantId: Long,
    var name: String
) {

    var image: String? = null
    var price: String = "0"

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0


}