package mor.aliakbar.restaurantnearme.ui.profile

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mor.aliakbar.restaurantnearme.api.ApiManager
import mor.aliakbar.restaurantnearme.storage.database.model.Message
import mor.aliakbar.restaurantnearme.storage.database.model.User
import mor.aliakbar.restaurantnearme.storage.sharedPrefs.PreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class LogInViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var apiManager: ApiManager

    var user = MutableLiveData(User())
    lateinit var context: Context

    var eventGoToProfileFragment = MutableLiveData(false)
    var eventOnSignUpClick = MutableLiveData(false)

    @Inject
    lateinit var preferences: PreferencesManager

    fun onLogInClick() {
        apiManager.logIn(object : Callback<Message> {
            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                val message = response.body()!!
                when (message.status) {
                    "error" -> {
                        Toast.makeText(context, message.message, Toast.LENGTH_SHORT).show()
                    }
                    "success" -> {

                        preferences.saveToken(message.message)
                        preferences.saveUserId(message.id.toLong())
                        preferences.saveUserName(message.name)
                        preferences.saveUserFamily(message.family)
                        preferences.saveUserPhone(message.phone)
                        preferences.saveUserEmail(user.value!!.email)
                        preferences.saveUserPassword(user.value!!.password)

                        eventGoToProfileFragment.value = true
                    }

                }
            }

            override fun onFailure(call: Call<Message>, t: Throwable) {
                Log.d("aaaaaaa", t.toString())
            }

        }, user.value!!)
    }

    fun onSignUpClick() {
        eventOnSignUpClick.value = true
    }

}