package mor.aliakbar.restaurantnearme.storage.sharedPrefs

import android.content.Context
import javax.inject.Inject

class PreferencesManager @Inject constructor(var context: Context) {

    private val sharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE)

    fun saveIsFakeDataAdd(isFakeDataAdd: Boolean) {
        sharedPreferences.edit().putBoolean("isFakeDataAdded", isFakeDataAdd).apply()
    }

    fun loadIsFakeDataAdd(): Boolean {
        return sharedPreferences.getBoolean("isFakeDataAdded", false)
    }

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("token", token).apply()
    }

    fun loadToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    fun saveUserName(name: String) {
        sharedPreferences.edit().putString("name", name).apply()
    }

    fun loadUserName(): String? {
        return sharedPreferences.getString("name", null)
    }

    fun saveUserId(id: Long) {
        sharedPreferences.edit().putLong("user_id", id).apply()
    }

    fun loadUserId(): Long {
        return sharedPreferences.getLong("user_id", -1)
    }

    fun saveUserFamily(family: String) {
        sharedPreferences.edit().putString("family", family).apply()
    }

    fun loadUserFamily(): String? {
        return sharedPreferences.getString("family", null)
    }

    fun saveUserEmail(email: String) {
        sharedPreferences.edit().putString("email", email).apply()
    }

    fun loadUserEmail(): String? {
        return sharedPreferences.getString("email", null)
    }

    fun saveUserPhone(phone: String) {
        sharedPreferences.edit().putString("phone", phone).apply()
    }

    fun loadUserPhone(): String? {
        return sharedPreferences.getString("phone", null)
    }

    fun saveUserPassword(password: String) {
        sharedPreferences.edit().putString("password", password).apply()
    }

    fun loadUserPassword(): String? {
        return sharedPreferences.getString("password", null)
    }


    fun saveDarkTheme(isDark: Boolean) {
        sharedPreferences.edit().putBoolean("theme", isDark).apply()
    }

    fun loadDarkTheme(): Boolean {
        return sharedPreferences.getBoolean("theme", false)
    }

    fun saveDistanceNotif(distance: Long) {
        sharedPreferences.edit().putLong("distance", distance).apply()
    }

    fun loadDistanceNotif(): Long {
        return sharedPreferences.getLong("distance", 200)
    }

    fun saveMinutesNotif(minutes: Int) {
        sharedPreferences.edit().putInt("minutes", minutes).apply()
    }

    fun loadMinutesNotif(): Int {
        return sharedPreferences.getInt("minutes", 2)
    }


}