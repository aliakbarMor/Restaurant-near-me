package mor.aliakbar.restaurantnearme.ui.profile

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import mor.aliakbar.restaurantnearme.api.ApiManager
import mor.aliakbar.restaurantnearme.storage.database.model.Message
import mor.aliakbar.restaurantnearme.storage.database.model.User
import mor.aliakbar.restaurantnearme.storage.sharedPrefs.PreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class SignUpViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var preferences: PreferencesManager

    @Inject
    lateinit var apiManager: ApiManager

    lateinit var context: Context

    var user = MutableLiveData(User())
    var repeatPassword = MutableLiveData("")

    var eventOnSignUpClick = MutableLiveData(false)
    var eventGoToProfileFragment = MutableLiveData(false)


    fun onSignUpClick() {
        eventOnSignUpClick.value = true
    }

    fun signUp() {
        apiManager.signUp(object : Callback<Message> {
            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                val message = response.body()
                when (message!!.status) {
                    "error" -> {
                        Toast.makeText(context, message.message, Toast.LENGTH_SHORT).show()
                    }
                    "success" -> {
                        Toast.makeText(context, "ثبت نام با موفقیت انجام شد", Toast.LENGTH_LONG)
                            .show()
                        preferences.saveToken(message.message)
                        preferences.saveUserId(message.id.toLong())
                        preferences.saveUserName(user.value!!.name)
                        preferences.saveUserFamily(user.value!!.family)
                        preferences.saveUserEmail(user.value!!.email)
                        preferences.saveUserPhone(user.value!!.phoneNumber)
                        preferences.saveUserPassword(user.value!!.password)

                        eventGoToProfileFragment.value = true
                    }
                }

            }

            override fun onFailure(call: Call<Message>, t: Throwable) {
                Log.d("LOGGG", t.toString())

            }
        }, user.value!!)

    }
}
