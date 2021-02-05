package mor.aliakbar.restaurantnearme.ui.food

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import mor.aliakbar.restaurantnearme.R
import mor.aliakbar.restaurantnearme.storage.database.model.Food
import javax.inject.Inject

class FoodItemViewModel @Inject constructor() : ViewModel() {

    lateinit var food: Food

    fun init(food: Food) {
        this.food = food
    }

    companion object {
        @JvmStatic
        @BindingAdapter("app:loadImageFood")
        fun loadImgFood(imageView: ImageView, path: String?) {
            Thread {
                if (path != null) {
                    val glide = Glide
                        .with(imageView.context)
                        .load(path)
                    imageView.post { glide.into(imageView) }
                } else {
                    imageView.post { imageView.setImageResource(R.drawable.ic_food) }
                }
            }.start()
        }
    }
}
