package mor.aliakbar.restaurantnearme.api

import mor.aliakbar.restaurantnearme.storage.database.model.Restaurant
import mor.aliakbar.restaurantnearme.storage.database.model.*
import okhttp3.MultipartBody
import retrofit2.Callback
import retrofit2.Retrofit
import javax.inject.Inject

class ApiManager @Inject constructor() {

    @Inject
    lateinit var retrofit: Retrofit

    fun getRestaurants(callback: Callback<List<Restaurant>>) {
        retrofit.create(ApiService::class.java)
            .getRestaurant()
            .enqueue(callback)
    }

    fun getFoods(callback: Callback<List<Food>>, rest_id: Long) {
        retrofit.create(ApiService::class.java)
            .getFoods(rest_id)
            .enqueue(callback)
    }

    fun getMeals(callback: Callback<ArrayList<Meal>>) {
        retrofit.create(ApiService::class.java)
            .getMeals()
            .enqueue(callback)
    }

    fun signUp(callback: Callback<Message>, user: User) {
        retrofit.create(ApiService::class.java)
            .signUp(user.name, user.family, user.email, user.password, user.phoneNumber)
            .enqueue(callback)
    }

    fun logIn(callback: Callback<Message>, user: User) {
        retrofit.create(ApiService::class.java)
            .logIn(user.email, user.password)
            .enqueue(callback)

    }

    fun isVote(callback: Callback<Vote>, rest_id: Long, user_id: Long) {
        retrofit.create(ApiService::class.java)
            .isVote(rest_id, user_id)
            .enqueue(callback)
    }

    fun vote(callback: Callback<Vote>, rest_id: Long, user_id: Long, score: Int) {
        retrofit.create(ApiService::class.java)
            .vote(rest_id, user_id, score)
            .enqueue(callback)
    }

    fun getAllVotes(callback: Callback<List<Vote>>, rest_id: Long) {
        retrofit.create(ApiService::class.java)
            .getAllVote(rest_id)
            .enqueue(callback)
    }

    fun insertRestaurant(
        callback: Callback<Message>,
        name: String, latitude: Double, longitude: Double, image_url: String?
    ) {
        retrofit.create(ApiService::class.java)
            .insertRest(name, latitude, longitude, image_url)
            .enqueue(callback)
    }

    fun insertFood(callback: Callback<Message>, rest_id: Long, name: String, price: String) {
        retrofit.create(ApiService::class.java)
            .insertFood(rest_id, name, price)
            .enqueue(callback)
    }

    fun insertMeal(
        callback: Callback<Message>, rest_id: Long, name: String, startTime: String, endTime: String
    ) {
        retrofit.create(ApiService::class.java)
            .insertMeal(rest_id, name, startTime, endTime)
            .enqueue(callback)
    }

    fun uploadImg(callback: Callback<MessageUploadImg>, file: MultipartBody.Part?) {
        retrofit.create(ApiService::class.java)
            .uploadImage(file)
            ?.enqueue(callback)
    }

}