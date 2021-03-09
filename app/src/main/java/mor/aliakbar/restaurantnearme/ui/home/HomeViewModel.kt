package mor.aliakbar.restaurantnearme.ui.home

import android.content.Context
import androidx.lifecycle.*
import mor.aliakbar.restaurantnearme.api.ApiManager
import mor.aliakbar.restaurantnearme.storage.database.AppRepository
import mor.aliakbar.restaurantnearme.storage.database.model.Restaurant
import mor.aliakbar.restaurantnearme.storage.sharedPrefs.PreferencesManager
import javax.inject.Inject

class HomeViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var sharedPrefs: PreferencesManager

    @Inject
    lateinit var repository: AppRepository

    @Inject
    lateinit var apiManager: ApiManager

    private val _restaurants = MutableLiveData<List<Restaurant>>()
    val restaurants: LiveData<List<Restaurant>>
        get() = _restaurants


    fun init() {
        getRestaurant()
    }

    private fun getRestaurant() {
        repository.getAllRestaurant().observeForever {
            _restaurants.value = it
        }
    }

}