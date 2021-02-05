package mor.aliakbar.restaurantnearme.di.module

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
annotation class ViewModelKeys(val value: KClass<out ViewModel>) {

}