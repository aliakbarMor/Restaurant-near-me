package mor.aliakbar.restaurantnearme.storage.database.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Restaurants")
class Restaurant() : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var name: String? = ""
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    @SerializedName("image_url")
    var imageUrl: String? = null
    var distanceWithMe: Long = 1000000
    var quality: Float = 4.5F


    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        name = parcel.readString()
        latitude = parcel.readDouble()
        longitude = parcel.readDouble()
        imageUrl = parcel.readString()
        distanceWithMe = parcel.readLong()
        quality = parcel.readFloat()

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(imageUrl)
        parcel.writeLong(distanceWithMe)
        parcel.writeFloat(quality)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Restaurant> {
        override fun createFromParcel(parcel: Parcel): Restaurant {
            return Restaurant(parcel)
        }

        override fun newArray(size: Int): Array<Restaurant?> {
            return arrayOfNulls(size)
        }
    }

}
