package mor.aliakbar.restaurantnearme.di.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Provides
    fun provideClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient.Builder): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2/restaurantnearme/")
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}