package mor.aliakbar.restaurantnearme.ui.home

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import mor.aliakbar.restaurantnearme.R
import mor.aliakbar.restaurantnearme.storage.database.AppRepository
import mor.aliakbar.restaurantnearme.storage.database.model.Meal
import mor.aliakbar.restaurantnearme.storage.database.model.Restaurant
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Inject

class RestaurantItemViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var baseUrl: String

    lateinit var restaurant: Restaurant
    lateinit var meal: String
    lateinit var distanceWithMe: String
    lateinit var quality: String

    fun init(restaurant: Restaurant) {
        this.restaurant = restaurant

        meal = ""
        var meals: List<Meal>? = null
        Executors.newCachedThreadPool().execute {
            meals = AppRepository.getInstance(context).getMealsByRestaurantId(restaurant.id)
        }
        while (meals == null) {
            Thread.sleep(10)
        }

        for (m in meals!!) {
            meal += m.name + ", "
        }
        if (meal.length > 1) {
            meal = meal.removeRange(meal.length - 2, meal.length)
        }

        distanceWithMe = restaurant.distanceWithMe.toString() + " متر"
        quality = String.format("%.1f", restaurant.quality) + " "

    }

    companion object {
        @JvmStatic
        @BindingAdapter("app:loadImg")
        fun loadImage(imageView: ImageView, path: String?) {
            if (path != null) {
                Thread {
                    val glide = Glide
                        .with(imageView.context)
                        .load(path)
                        .circleCrop()

                    imageView.post { glide.into(imageView) }
                }.start()
            } else {
                imageView.setImageResource(R.drawable.rest_placeholder)
            }
        }
    }
}
