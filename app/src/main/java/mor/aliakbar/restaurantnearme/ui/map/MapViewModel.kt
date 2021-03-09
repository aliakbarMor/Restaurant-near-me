package mor.aliakbar.restaurantnearme.ui.map

import android.content.Context
import androidx.lifecycle.*
import com.mapbox.api.directions.v5.models.DirectionsResponse
import com.mapbox.api.directions.v5.models.DirectionsRoute
import mor.aliakbar.restaurantnearme.api.ApiManager
import mor.aliakbar.restaurantnearme.notification.AppNotification
import mor.aliakbar.restaurantnearme.storage.database.AppRepository
import mor.aliakbar.restaurantnearme.storage.database.model.Restaurant
import mor.aliakbar.restaurantnearme.storage.database.model.Meal
import mor.aliakbar.restaurantnearme.storage.sharedPrefs.PreferencesManager
import mor.aliakbar.restaurantnearme.utils.NetworkUtility
import mor.aliakbar.restaurantnearme.utils.Utility
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Inject

class MapViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var repository: AppRepository

    @Inject
    lateinit var apiManager: ApiManager

    @Inject
    lateinit var sharedPrefs: PreferencesManager


    private var isNotificationSend: Boolean = false
    private var lastRestNotifSend: Restaurant? = null

    private var minute: Int = 2

    private var lastLatitude: Double? = null
    private var lastLongitude: Double? = null


    val restaurants = MutableLiveData<List<Restaurant>>(ArrayList())
    var currentRoute = MutableLiveData<DirectionsRoute?>(null)

    fun init() {
        getRestaurant()
        restaurants.observeForever {
        }

    }

    private fun getRestaurant() {
//        addFakeData()
        if (NetworkUtility.isOnline(context)) {
            var restaurantsNotSaveInServer: List<Restaurant>? = null
            Executors.newSingleThreadExecutor().execute {
                restaurantsNotSaveInServer = repository.getRestaurantsByStoredOnServer(false)
            }

            apiManager.getRestaurants(object : Callback<List<Restaurant>> {
                override fun onResponse(
                    call: Call<List<Restaurant>>,
                    response: Response<List<Restaurant>>
                ) {
                    repository.deleteAllRestaurants()
                    restaurants.value = response.body()
                    restaurants.value = restaurants.value!! + restaurantsNotSaveInServer!!
                    repository.insertAllRestaurant(restaurants.value as ArrayList<Restaurant>)
                    restaurants.value?.sortedBy { restaurant ->
                        restaurant.distanceWithMe
                    }
                }

                override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                    getRestaurantsFromDb()
                    Timber.i(t.toString())
                }
            })

            apiManager.getMeals(object : Callback<ArrayList<Meal>> {
                override fun onResponse(
                    call: Call<ArrayList<Meal>>,
                    response: Response<ArrayList<Meal>>
                ) {
                    repository.deleteAllMeals()
                    response.body()?.let { repository.insertAllMeals(it) }
                }

                override fun onFailure(call: Call<ArrayList<Meal>>, t: Throwable) {
                    Timber.i(t.toString())
                }
            })
        } else {
            getRestaurantsFromDb()
        }
    }

    private fun getRestaurantsFromDb() {
        repository.getAllRestaurant().observeForever {
            restaurants.value = it
            restaurants.value!!.sortedBy { restaurant ->
                restaurant.distanceWithMe
            }
        }
    }

    fun updateLocation(latitude: Double?, longitude: Double?) {
        var firstTime = false
        if (latitude == null || longitude == null) {
            return
        } else if (lastLatitude == null || lastLongitude == null) {
            lastLatitude = latitude
            lastLongitude = longitude
            firstTime = true
        }

        val distance =
            Utility.calculationDistance(latitude, longitude, lastLatitude!!, lastLongitude!!)

        if (distance < 2 && !firstTime) {
            return
        }

        lastLatitude = latitude
        lastLongitude = longitude

        val speedMeterPerSecond = distance / 1    // 1 is Time to call this method

        minute = sharedPrefs.loadMinutesNotif()

        var theNearestRestaurant: Restaurant? = null
        var theNearestRestaurantDistance: Long? = null

        for (rest in restaurants.value!!) {
            val calculationByDistance = Utility.calculationDistance(
                latitude, longitude, rest.latitude, rest.longitude
            )
            repository.updateColumnDistanceWithMe(calculationByDistance, rest.id)

            if (theNearestRestaurantDistance == null || theNearestRestaurantDistance > calculationByDistance) {
                theNearestRestaurantDistance = calculationByDistance
                theNearestRestaurant = rest
            }
        }

        if (theNearestRestaurantDistance != null) {
            val timeToReachTheDestination = if (speedMeterPerSecond != 0L) {
                theNearestRestaurantDistance / speedMeterPerSecond
            } else
                10000000

            if (timeToReachTheDestination > minute && isNotificationSend) {
                isNotificationSend = false
            } else if (timeToReachTheDestination < minute && !isNotificationSend) {
                lastRestNotifSend = theNearestRestaurant
                sendNotification(theNearestRestaurant!!)
            }
        }
        if (lastRestNotifSend != null && lastRestNotifSend!!.name != theNearestRestaurant!!.name) {
            sendNotification(theNearestRestaurant)
        }

    }

    private fun sendNotification(theNearestRestaurant: Restaurant) {
        var meals: List<Meal>? = null
        Executors.newCachedThreadPool().execute {
            meals = repository.getMealsByRestaurantId(theNearestRestaurant.id)
        }
        while (meals == null) {
            Thread.sleep(10)
        }
        val mealsTime = Utility.comparisonTimes(meals!!)

        val notification = AppNotification.getInstance(context)!!
        notification.showNotification(theNearestRestaurant, mealsTime)
        isNotificationSend = true
    }

    fun callback(): Callback<DirectionsResponse?> {
        return object : Callback<DirectionsResponse?> {
            override fun onResponse(
                call: Call<DirectionsResponse?>?,
                response: Response<DirectionsResponse?>
            ) {
                if (response.body() == null) {
                    return
                } else if (response.body()!!.routes().size < 1) {
                    return
                }
                currentRoute.value = response.body()!!.routes()[0]
            }

            override fun onFailure(call: Call<DirectionsResponse?>?, throwable: Throwable) {
            }
        }
    }

    fun addFakeData() {
        if (!repository.loadIsFakeDataAdd()) {
            Utility.createSampleDataRest(context)
            Utility.createSampleDataFood(context)
            Utility.createSampleDataMeal(context)
            repository.saveIsFakeDataAdd(true)
        }
    }

}