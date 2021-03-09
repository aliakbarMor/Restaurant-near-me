package mor.aliakbar.restaurantnearme.api

import mor.aliakbar.restaurantnearme.storage.database.model.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @FormUrlEncoded
    @POST("login.php")
    fun logIn(
        @Field("email") email: String,
        @Field("password") pass: String
    ): Call<Message>

    @FormUrlEncoded
    @POST("signup.php")
    fun signUp(
        @Field("name") name: String,
        @Field("family") family: String,
        @Field("email") email: String,
        @Field("password") pass: String,
        @Field("phone") phone: String
    ): Call<Message>

    @GET("restaurant.php")
    fun getRestaurant(): Call<List<Restaurant>>

    @GET("food.php")
    fun getFoods(
        @Query("rest_id") rest_id: Long
    ): Call<List<Food>>

    @GET("meal.php")
    fun getMeals(): Call<ArrayList<Meal>>

    @GET("point.php")
    fun isVote(
        @Query("rest_id") rest_id: Long,
        @Query("user_id") user_id: Long
    ): Call<Vote>

    @GET("point.php")
    fun vote(
        @Query("rest_id") rest_id: Long,
        @Query("user_id") user_id: Long,
        @Query("score") score: Int
    ): Call<Vote>

    @GET("point.php")
    fun getAllVote(
        @Query("rest_id") rest_id: Long,
    ): Call<List<Vote>>

    @GET("insertRestaurant.php")
    fun insertRest(
        @Query("name") name: String,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("image_url") image_url: String?
    ): Call<Message>

    @GET("insertFood.php")
    fun insertFood(
        @Query("rest_id") rest_id: Long,
        @Query("name") name: String,
        @Query("price") price: String
    ): Call<Message>

    @GET("insertMeal.php")
    fun insertMeal(
        @Query("rest_id") rest_id: Long,
        @Query("name") name: String,
        @Query("start_time") start_time: String,
        @Query("end_time") end_time: String
    ): Call<Message>

    @Multipart
    @POST("upload.php")
    fun uploadImage(@Part file: MultipartBody.Part?): Call<MessageUploadImg>?

}