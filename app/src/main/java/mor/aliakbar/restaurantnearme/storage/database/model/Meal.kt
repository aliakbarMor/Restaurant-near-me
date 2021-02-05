package mor.aliakbar.restaurantnearme.storage.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "Meals")
class  Meal(
    @SerializedName("rest_id")
    var restaurantId: Long = 0,
    var name: String ,
    @SerializedName("start_time")
    var startTime: String ,
    @SerializedName("end_time")
    var endTime: String
){
    @PrimaryKey(autoGenerate = true)
    var id :Long= 0
}
