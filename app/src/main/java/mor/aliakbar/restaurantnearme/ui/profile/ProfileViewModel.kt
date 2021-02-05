package mor.aliakbar.restaurantnearme.ui.profile

import android.content.Context
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mor.aliakbar.restaurantnearme.storage.database.model.User
import mor.aliakbar.restaurantnearme.storage.sharedPrefs.PreferencesManager
import javax.inject.Inject

class ProfileViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var preferences: PreferencesManager

    var user = MutableLiveData(User())

    fun initUser() {
        user.value!!.id = preferences.loadUserId()
        user.value!!.name = preferences.loadUserName()!!
        user.value!!.family = preferences.loadUserFamily()!!
        user.value!!.phoneNumber = preferences.loadUserPhone()!!
        user.value!!.email = preferences.loadUserEmail()!!
        user.value!!.password = preferences.loadUserPassword()!!
    }

    fun checkToken(): String? {
        return preferences.loadToken()
    }

}