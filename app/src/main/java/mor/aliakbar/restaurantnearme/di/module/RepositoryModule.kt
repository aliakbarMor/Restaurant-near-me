package mor.aliakbar.restaurantnearme.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import mor.aliakbar.restaurantnearme.storage.database.AppRepository
import mor.aliakbar.restaurantnearme.storage.sharedPrefs.PreferencesManager
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Module
class RepositoryModule {

    @Provides
    fun getAppRepository(context: Context): AppRepository {
        return AppRepository.getInstance(context)
    }

}