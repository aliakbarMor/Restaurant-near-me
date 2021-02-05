package mor.aliakbar.restaurantnearme.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import mor.aliakbar.restaurantnearme.di.DaggerApp
import mor.aliakbar.restaurantnearme.di.module.ApiModule
import mor.aliakbar.restaurantnearme.di.module.InjectModule
import mor.aliakbar.restaurantnearme.di.module.RepositoryModule
import mor.aliakbar.restaurantnearme.di.module.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, InjectModule::class, ViewModelModule::class, RepositoryModule::class, ApiModule::class])
interface AppComponent : AndroidInjector<DaggerApp> {

    fun context(): Context

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun context(context: Context): Builder

    }

}