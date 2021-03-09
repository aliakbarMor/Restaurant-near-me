package mor.aliakbar.restaurantnearme.ui.food

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import mor.aliakbar.restaurantnearme.R
import mor.aliakbar.restaurantnearme.api.ApiManager
import mor.aliakbar.restaurantnearme.storage.database.AppRepository
import mor.aliakbar.restaurantnearme.storage.database.model.Food
import mor.aliakbar.restaurantnearme.storage.database.model.Restaurant
import mor.aliakbar.restaurantnearme.storage.database.model.Vote
import mor.aliakbar.restaurantnearme.storage.sharedPrefs.PreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class RestDetailViewModel @Inject constructor() : ViewModel() {

    //region variables
    @Inject
    lateinit var context: Context

    @Inject
    lateinit var baseUrl: String

    @Inject
    lateinit var repository: AppRepository

    @Inject
    lateinit var apiManager: ApiManager

    @Inject
    lateinit var shp: PreferencesManager

    lateinit var restaurant: Restaurant
    var foods = MutableLiveData<List<Food>>()

    var score = MutableLiveData(0)
    val eventGoToLogInFragment = MutableLiveData(false)
    val eventUpdateView = MutableLiveData(false)

    var percent5Star = MutableLiveData(0)
    var percent4Star = MutableLiveData(0)
    var percent3Star = MutableLiveData(0)
    var percent2Star = MutableLiveData(0)
    var percent1Star = MutableLiveData(0)
    var numberOfPoint = MutableLiveData(0)
    var overallScore = MutableLiveData(.0F)
    //endregion

    fun init(restaurant: Restaurant) {
        this.restaurant = restaurant

        apiManager.getFoods(object : Callback<List<Food>> {
            override fun onResponse(call: Call<List<Food>>, response: Response<List<Food>>) {
                foods.value = response.body()
            }

            override fun onFailure(call: Call<List<Food>>, t: Throwable) {
                Timber.i(t.toString())
            }

        }, restaurant.id)

        if (shp.loadToken() != null) {
            apiManager.isVote(object : Callback<Vote> {
                override fun onResponse(call: Call<Vote>, response: Response<Vote>) {
                    val body = response.body()
                    if (body!!.status == "success") {
                        score.value = body.score
                    }
                }

                override fun onFailure(call: Call<Vote>, t: Throwable) {
                    Timber.i(t.toString())
                }
            }, restaurant.id, shp.loadUserId())
        }

        apiManager.getAllVotes(object : Callback<List<Vote>> {
            override fun onResponse(call: Call<List<Vote>>, response: Response<List<Vote>>) {
                val votes = response.body()
                if (votes != null) {
                    numberOfPoint.value = votes.size
                    var totalPoint = 0
                    var numberPoint1 = 0
                    var numberPoint2 = 0
                    var numberPoint3 = 0
                    var numberPoint4 = 0
                    var numberPoint5 = 0
                    for (v in votes) {
                        totalPoint += v.score
                        if (v.score == 1)
                            numberPoint1++
                        if (v.score == 2)
                            numberPoint2++
                        if (v.score == 3)
                            numberPoint3++
                        if (v.score == 4)
                            numberPoint4++
                        if (v.score == 5)
                            numberPoint5++
                    }
                    if (numberOfPoint.value != 0) {
                        percent5Star.value = (numberPoint5 * 100) / numberOfPoint.value!!
                        percent4Star.value = (numberPoint4 * 100) / numberOfPoint.value!!
                        percent3Star.value = (numberPoint3 * 100) / numberOfPoint.value!!
                        percent2Star.value = (numberPoint2 * 100) / numberOfPoint.value!!
                        percent1Star.value = (numberPoint1 * 100) / numberOfPoint.value!!
                        overallScore.value =
                            String.format("%.1f", totalPoint.toFloat() / numberOfPoint.value!!)
                                .toFloat()
                    }
                }
            }

            override fun onFailure(call: Call<List<Vote>>, t: Throwable) {
                Timber.i(t.toString())
            }
        }, restaurant.id)

    }

    companion object {
        @JvmStatic
        @BindingAdapter("app:loadImgRest")
        fun loadImgRest(imageView: ImageView, url: String?) {
            val glide = Glide
                .with(imageView.context)
            if (url != null) {
                glide
                    .load(url)
                    .centerCrop()
                    .into(imageView)
            } else {
                if (PreferencesManager(imageView.context).loadDarkTheme())
                    glide.load(R.drawable.horizontal_icon_food_night)
                        .centerCrop()
                        .into(imageView)
                else
                    glide.load(R.drawable.horizontal_icon_food)
                        .centerCrop()
                        .into(imageView)
            }
        }
    }

    fun getUrl(s: String): String? {
        return if (restaurant.imageUrl != null) {
            baseUrl + "img/" + restaurant.imageUrl
        } else null
    }

    fun onVoteStarClick(point: Int) {
        if (shp.loadToken() == null) {
            eventGoToLogInFragment.value = true
        } else {
            if (score.value == 0) {
                apiManager.vote(object : Callback<Vote> {

                    override fun onResponse(call: Call<Vote>, response: Response<Vote>) {
                        if (response.body()!!.status == "success") {
                            Toast.makeText(context, "امتیاز شما ثبت شد", Toast.LENGTH_SHORT).show()
                            score.value = point
                            eventUpdateView.value = true
                            Timber.d(score.value.toString())
                        }
                    }

                    override fun onFailure(call: Call<Vote>, t: Throwable) {
                        Timber.i(t.toString())
                    }

                }, restaurant.id, shp.loadUserId(), point)
            } else
                Toast.makeText(context, "شما قبلا امتیاز داده اید", Toast.LENGTH_SHORT).show()
        }

    }

}
