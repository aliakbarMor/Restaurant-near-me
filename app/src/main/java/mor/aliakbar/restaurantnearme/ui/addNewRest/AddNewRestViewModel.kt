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
import mor.aliakbar.restaurantnearme.storage.database.AppRepository
import mor.aliakbar.restaurantnearme.storage.database.model.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.File
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.random.Random

class AddNewRestViewModel @Inject constructor() : ViewModel() {

    var restaurantSaveInDb = MutableLiveData(Restaurant())

    @Inject
    lateinit var apiManager: ApiManager

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var repository: AppRepository

    val eventGoToBackToMapFragment = MutableLiveData(false)

    fun insertRestInToDb(
        latitude: Double,
        longitude: Double,
        allItemFood: ArrayList<Food>,
        allItemMeal: ArrayList<Meal>
    ) {
        restaurantSaveInDb.value!!.id = Random(System.nanoTime()).nextLong(100000 - 1000 + 1) + 1000
        restaurantSaveInDb.value!!.latitude = latitude
        restaurantSaveInDb.value!!.longitude = longitude
        restaurantSaveInDb.value!!.isInServer = false


        repository.insertRestaurant(restaurantSaveInDb.value!!)
        for (food in allItemFood) {
            food.restaurantId = restaurantSaveInDb.value!!.id
        }
        repository.insertAllFood(allItemFood)
        for (meal in allItemMeal) {
            meal.restaurantId = restaurantSaveInDb.value!!.id
            meal.isInServer = false
        }
        repository.insertAllMeals(allItemMeal)

        eventGoToBackToMapFragment.value = true
    }

    fun insertRestsInToServer() {
        var restaurantsNotStoredOnServer: List<Restaurant>? = null
        Executors.newCachedThreadPool().execute {
            restaurantsNotStoredOnServer = repository.getRestaurantsByStoredOnServer(false)
        }
        while (restaurantsNotStoredOnServer == null) {
            Thread.sleep(10)
        }

        for (index in restaurantsNotStoredOnServer!!.indices) {

            if (restaurantsNotStoredOnServer!![index].imagePathLocal != null)
                uploadImage(restaurantsNotStoredOnServer!![index])
            apiManager.insertRestaurant(
                object : Callback<Message> {
                    override fun onResponse(call: Call<Message>, response: Response<Message>) {
                        val id = response.body()!!.id
                        repository.deleteRestaurant(restaurantsNotStoredOnServer!![index].id)

                        var allItemFood: List<Food>? = null
                        var allItemMeal: List<Meal>? = null
                        Executors.newCachedThreadPool().execute {
                            allItemFood =
                                repository.getFoodByRestaurantId(restaurantsNotStoredOnServer!![index].id)
                            allItemMeal =
                                repository.getMealsByRestaurantId(restaurantsNotStoredOnServer!![index].id)
                        }
                        while (allItemFood == null)
                            Thread.sleep(100)

                        for (food in allItemFood!!) {
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
                        for (meal in allItemMeal!!) {
                            apiManager.insertMeal(object : Callback<Message> {
                                override fun onResponse(
                                    call: Call<Message>,
                                    response: Response<Message>
                                ) {
                                    repository.deleteMeal(restaurantsNotStoredOnServer!![index].id)
                                    Toast.makeText(
                                        context,
                                        "زمان سفارش ${meal.name} ثبت شد",
                                        Toast.LENGTH_SHORT
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

                },
                restaurantsNotStoredOnServer!![index].name!!,
                restaurantsNotStoredOnServer!![index].latitude,
                restaurantsNotStoredOnServer!![index].longitude,
                restaurantsNotStoredOnServer!![index].imageUrl
            )

        }
    }

    private fun uploadImage(restaurant: Restaurant) {
        val file = File(restaurant.imagePathLocal!!)
        restaurant.imageUrl = file.name
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
        restaurantSaveInDb.value!!.imagePathLocal = result
    }


}