package mor.aliakbar.restaurantnearme.ui.addNewRest

import android.content.Context
import android.content.CursorLoader
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapbox.mapboxsdk.Mapbox
import mor.aliakbar.restaurantnearme.api.ApiManager
import mor.aliakbar.restaurantnearme.storage.database.model.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class AddNewRestViewModel @Inject constructor() : ViewModel() {

    var restaurant = Restaurant()

    @Inject
    lateinit var apiManager: ApiManager

    @Inject
    lateinit var context: Context

    val eventGoToBackToMapFragment = MutableLiveData(false)


    var imagePath: String? = null

    fun insertRest(
        latitude: Double,
        longitude: Double,
        allItemFood: ArrayList<Food>,
        allItemMeal: ArrayList<Meal>
    ) {
        if (imagePath != null)
            uploadImage()
        apiManager.insertRestaurant(
            object : Callback<Message> {
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    val id = response.body()!!.id

                    for (food in allItemFood) {
                        apiManager.insertFood(object : Callback<Message> {
                            override fun onResponse(
                                call: Call<Message>,
                                response: Response<Message>
                            ) {
                                Toast.makeText(
                                    context, "غذای ${food.name} ثبت شد", Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onFailure(call: Call<Message>, t: Throwable) {
                                Timber.i(t.toString())
                            }

                        }, id.toLong(), food.name, food.price)
                    }

                    for (meal in allItemMeal) {
                        apiManager.insertMeal(object : Callback<Message> {
                            override fun onResponse(
                                call: Call<Message>,
                                response: Response<Message>
                            ) {
                                Toast.makeText(
                                    context, "زمان سفارش ${meal.name} ثبت شد", Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onFailure(call: Call<Message>, t: Throwable) {
                                Timber.i(t.toString())
                            }

                        }, id.toLong(), meal.name, meal.startTime, meal.endTime)
                    }
                    eventGoToBackToMapFragment.value = true

                }

                override fun onFailure(call: Call<Message>, t: Throwable) {
                    Timber.i(t.toString())
                }

            }, restaurant.name!!, latitude, longitude, restaurant.imageUrl
        )

    }

    private fun uploadImage() {
        val file = File(imagePath!!)
        restaurant.imageUrl = "http://10.0.2.2/restaurantnearme/img/" + file.name
        val requestFile: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("uploaded_file", file.name, requestFile)

        apiManager.uploadImg(object : Callback<MessageUploadImg> {
            override fun onResponse(
                call: Call<MessageUploadImg>?,
                response: Response<MessageUploadImg>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.error == true) {
                        Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                    } else
                        Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MessageUploadImg>?, t: Throwable?) {
                Timber.i(t.toString())
            }
        }, body)
    }

    fun setRealPathFromURI(contentUri: Uri?) {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val loader =
            CursorLoader(Mapbox.getApplicationContext(), contentUri, projection, null, null, null)
        val cursor: Cursor = loader.loadInBackground()
        val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result: String = cursor.getString(columnIndex)
        cursor.close()
        imagePath = result
    }


}