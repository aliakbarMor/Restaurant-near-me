package mor.aliakbar.restaurantnearme.utils

import android.content.Context
import mor.aliakbar.restaurantnearme.storage.database.AppRepository
import mor.aliakbar.restaurantnearme.storage.database.model.Food
import mor.aliakbar.restaurantnearme.storage.database.model.Meal
import mor.aliakbar.restaurantnearme.storage.database.model.Restaurant
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object Utility {

    fun createSampleDataRest(context: Context) {
        val list = ArrayList<Restaurant>()

        val restaurant = Restaurant()
        restaurant.name = "پیتزا ناخدا"
        restaurant.latitude = 35.77043794219583
        restaurant.longitude = 51.35005865925981

        val restaurant1 = Restaurant()
        restaurant1.name = "ساندویچ ارغنون"
        restaurant1.latitude = 35.771449563109364
        restaurant1.longitude = 51.35675121460002

        val restaurant2 = Restaurant()
        restaurant2.name = "شرین پلو"
        restaurant2.latitude = 35.768713873817106
        restaurant2.longitude = 51.35656548558599

        val restaurant3 = Restaurant()
        restaurant3.name = "هایدا"
        restaurant3.latitude = 35.76570566619532
        restaurant3.longitude = 51.35782272771652

        val restaurant4 = Restaurant()
        restaurant4.name = "لانجین"
        restaurant4.latitude = 35.76533169788835
        restaurant4.longitude = 51.35847977618209

        val restaurant5 = Restaurant()
        restaurant5.name = "سه فر"
        restaurant5.latitude = 35.76533169788835
        restaurant5.longitude = 51.35847977618209

        val restaurant6 = Restaurant()
        restaurant6.name = "دشت بهشت"
        restaurant6.latitude = 35.792132858129854
        restaurant6.longitude = 51.37956241712678

        val restaurant7 = Restaurant()
        restaurant7.name = "مسلم"
        restaurant7.latitude = 35.67751883777864
        restaurant7.longitude = 51.41957358593019

        val restaurant8 = Restaurant()
        restaurant8.name = "فست فود ارکیده"
        restaurant8.latitude = 36.31687522264103
        restaurant8.longitude = 59.49167293529223

        val restaurant9 = Restaurant()
        restaurant9.name = "کافه پیانو"
        restaurant9.latitude = 36.31642027847425
        restaurant9.longitude = 59.49570853947195

        list.add(restaurant)
        list.add(restaurant1)
        list.add(restaurant2)
        list.add(restaurant3)
        list.add(restaurant4)
        list.add(restaurant6)
        list.add(restaurant7)
        list.add(restaurant8)
        list.add(restaurant9)

        AppRepository.getInstance(context).insertAllRestaurant(list)
    }

    fun createSampleDataFood(context: Context) {
        val foods = ArrayList<Food>()

        val food = Food(1, "پیتزا مخصوص")
        val food1 = Food(1, "پیتزا پپرونی")
        val food2 = Food(1, "پیتزا قارچ و گوشت")
        val food3 = Food(1, "پیتزا سبزیجات")
        val food4 = Food(1, "پیتزا سیر و استیک")

        val food5 = Food(2, "هات داگ اسپشیال")
        val food6 = Food(2, "هات داگ معمولی")
        val food7 = Food(2, "بندری ویژه")
        val food8 = Food(2, "ساندویچ استیک کبابی")
        val food9 = Food(2, "ساندویچ مرغ پستو")

        val food10 = Food(3, "جوجه کباب مخصوص ران")
        val food11 = Food(3, "کباب کوبیده مخصوص دو سیخ")
        val food12 = Food(3, "زرشک پلو با مرغ سرخ کرده")
        val food13 = Food(3, "چلو خورشت قورمه سبزی")
        val food14 = Food(3, "چلو خورشت قیمه")

        val food15 = Food(4, "کالباس خشک مخصوص")
        val food16 = Food(4, "ژامبون قارچ و گوشت")
        val food17 = Food(4, "ژامبون گوشت ژیگو")
        val food18 = Food(4, "ژامبون مرغ مخصوص")
        val food19 = Food(4, "ژامبون بوقلمون")

        val food20 = Food(5, "استیک فیله")
        val food21 = Food(5, "پاستا چیکن آلفردو")
        val food22 = Food(5, "پن کیک")
        val food23 = Food(5, "وافل شکلات")
        val food24 = Food(5, "کرپ موز و شکلات")

        val food25 = Food(6, "چیکن برگر دبل")
        val food26 = Food(6, "اسموکی برگر دبل")
        val food27 = Food(6, "چیز برگر سینگل")
        val food28 = Food(6, "سیب زمینی با سس قارچ")
        val food29 = Food(6, "ماشروم برگر")

        val food30 = Food(8, "کالباس خشک مخصوص")
        val food31 = Food(8, "ژامبون قارچ و گوشت")
        val food32 = Food(8, "ژامبون گوشت ژیگو")
        val food33 = Food(8, "ژامبون مرغ مخصوص")
        val food34 = Food(8, "ژامبون بوقلمون")

        val food35 = Food(9, "استیک فیله")
        val food36 = Food(9, "پاستا چیکن آلفردو")
        val food37 = Food(9, "پن کیک")
        val food38 = Food(9, "وافل شکلات")
        val food39 = Food(9, "کرپ موز و شکلات")

        foods.add(food)
        foods.add(food1)
        foods.add(food2)
        foods.add(food3)
        foods.add(food4)
        foods.add(food5)
        foods.add(food6)
        foods.add(food7)
        foods.add(food8)
        foods.add(food9)
        foods.add(food10)
        foods.add(food11)
        foods.add(food12)
        foods.add(food13)
        foods.add(food14)
        foods.add(food15)
        foods.add(food16)
        foods.add(food17)
        foods.add(food18)
        foods.add(food19)
        foods.add(food20)
        foods.add(food21)
        foods.add(food22)
        foods.add(food23)
        foods.add(food24)
        foods.add(food25)
        foods.add(food26)
        foods.add(food27)
        foods.add(food28)
        foods.add(food29)
        foods.add(food30)
        foods.add(food31)
        foods.add(food32)
        foods.add(food33)
        foods.add(food34)
        foods.add(food35)
        foods.add(food36)
        foods.add(food37)
        foods.add(food38)
        foods.add(food39)

        AppRepository.getInstance(context).insertAllFood(foods)
    }

    fun createSampleDataMeal(context: Context) {
        val list = ArrayList<Meal>()

        val meal = Meal(1, "ناهار", "1:00:00 PM", "5:30:00 PM")
        val meal1 = Meal(1, "شام", "7:00:00 PM", "11:30:00 PM")

        val meal2 = Meal(2, "ناهار", "1:00:00 PM", "3:30:00 PM")
        val meal3 = Meal(2, "شام", "7:00:00 PM", "11:30:00 PM")

        val meal4 = Meal(3, "ناهار", "1:00:00 PM", "3:30:00 PM")
        val meal5 = Meal(3, "شام", "7:00:00 PM", "11:30:00 PM")

        val meal6 = Meal(4, "ناهار", "1:00:00 PM", "3:30:00 PM")
        val meal7 = Meal(4, "شام", "7:00:00 PM", "11:30:00 PM")

        val meal8 = Meal(5, "صبحانه", "7:00:00 AM", "10:30:00 AM")
        val meal9 = Meal(5, "ناهار", "1:00:00 PM", "3:30:00 PM")
        val meal10 = Meal(5, "شام", "7:00:00 PM", "11:30:00 PM")

        val meal11 = Meal(6, "ناهار", "1:00:00 PM", "3:30:00 PM")
        val meal12 = Meal(6, "شام", "7:00:00 PM", "11:30:00 PM")

        val meal13 = Meal(8, "ناهار", "1:00:00 PM", "3:30:00 PM")
        val meal14 = Meal(8, "شام", "7:00:00 PM", "11:30:00 PM")

        val meal15 = Meal(9, "صبحانه", "7:00:00 AM", "10:30:00 AM")
        val meal16 = Meal(9, "ناهار", "1:00:00 PM", "3:30:00 PM")
        val meal17 = Meal(9, "شام", "7:00:00 PM", "11:30:00 PM")

        list.add(meal)
        list.add(meal1)
        list.add(meal2)
        list.add(meal3)
        list.add(meal4)
        list.add(meal5)
        list.add(meal6)
        list.add(meal7)
        list.add(meal8)
        list.add(meal9)
        list.add(meal10)
        list.add(meal11)
        list.add(meal12)
        list.add(meal13)
        list.add(meal14)
        list.add(meal15)
        list.add(meal16)
        list.add(meal17)

        AppRepository.getInstance(context).insertAllMeals(list)
    }

    fun calculationDistance(
        startLat: Double,
        startLng: Double,
        endLat: Double,
        endLng: Double
    ): Long {
        val radius = 6371 // radius of earth in Km
        val dLat = Math.toRadians(endLat - startLat)
        val dLon = Math.toRadians(endLng - startLng)
        val a = (sin(dLat / 2) * sin(dLat / 2)
                + (cos(Math.toRadians(startLat))
                * cos(Math.toRadians(endLat)) * sin(dLon / 2)
                * sin(dLon / 2)))
        val c = 2 * asin(sqrt(a))
        val valueResult = radius * c        // To kilometers
        val newFormat = DecimalFormat("####")
//            val kmInDec: Int = Integer.valueOf(newFormat.format(valueResult))
        val meter = valueResult * 1000

        return newFormat.format(meter).toLong()
    }

    fun comparisonTimes(meals: List<Meal>): String? {
        val sdf = SimpleDateFormat("hh:mm:ss aa", Locale.US)

        val dateFormat = SimpleDateFormat("hh:mm:ss aa", Locale.US).format(Date())
        val dateNow = sdf.parse(dateFormat)!!

        for (meal in meals) {
            val startTime = sdf.parse(meal.startTime)!!
            val endTime = sdf.parse(meal.endTime)!!

            if (dateNow.after(startTime) && dateNow.before(endTime)) {
                return meal.name
            }
        }

        return null
    }
}