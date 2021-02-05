package mor.aliakbar.restaurantnearme.di

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import mor.aliakbar.restaurantnearme.BuildConfig
import mor.aliakbar.restaurantnearme.di.component.DaggerAppComponent
import timber.log.Timber
import timber.log.Timber.DebugTree


class DaggerApp: DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().context(applicationContext).build()
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}