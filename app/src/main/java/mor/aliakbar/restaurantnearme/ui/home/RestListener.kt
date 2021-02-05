package mor.aliakbar.restaurantnearme.ui.home

import mor.aliakbar.restaurantnearme.storage.database.model.Restaurant


interface RestListener {

    fun onRestaurantClicked(restaurant: Restaurant)

    fun onRestaurantLongClicked(restaurant: Restaurant)

}