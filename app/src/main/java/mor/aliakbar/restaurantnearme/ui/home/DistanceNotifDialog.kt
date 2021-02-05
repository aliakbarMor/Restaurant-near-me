package mor.aliakbar.restaurantnearme.ui.home

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.widget.EditText
import mor.aliakbar.restaurantnearme.R
import mor.aliakbar.restaurantnearme.storage.sharedPrefs.PreferencesManager

class DistanceNotifDialog private constructor() {

    companion object {
        private var instance: DistanceNotifDialog? = null
        fun getInstance(): DistanceNotifDialog {
            if (instance == null) {
                instance = DistanceNotifDialog()
            }
            return instance as DistanceNotifDialog
        }
    }


    fun showDialog(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_distance_notif, null)

        val prefs = PreferencesManager(context)
        val edtDistance: EditText = view.findViewById(R.id.textSleepTime)
        edtDistance.setText(prefs.loadMinutesNotif().toString())

        AlertDialog
            .Builder(context, R.style.DialogTheme)
            .setView(view)
            .setNegativeButton("Cancel") { dialogInterface: DialogInterface, _ ->
                dialogInterface.cancel()
            }
            .setPositiveButton("Ok") { _, _ ->
                prefs.saveMinutesNotif(edtDistance.text.toString().toInt())
            }
            .show()

    }

}