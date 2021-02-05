package mor.aliakbar.restaurantnearme.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import mor.aliakbar.restaurantnearme.ui.home.HomeFragment
import mor.aliakbar.restaurantnearme.ui.food.RestDetailFragment
import mor.aliakbar.restaurantnearme.ui.addNewRest.AddNewRestaurantFragment
import mor.aliakbar.restaurantnearme.ui.map.MapFragment
import mor.aliakbar.restaurantnearme.ui.profile.LogInFragment
import mor.aliakbar.restaurantnearme.ui.profile.ProfileFragment
import mor.aliakbar.restaurantnearme.ui.profile.SignUpFragment

@Module
abstract class InjectModule {

    @ContributesAndroidInjector
    abstract fun injectHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun injectMapFragment(): MapFragment

    @ContributesAndroidInjector
    abstract fun injectRestDetailFragment(): RestDetailFragment

    @ContributesAndroidInjector
    abstract fun injectProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun injectLogInFragment(): LogInFragment

    @ContributesAndroidInjector
    abstract fun injectSignUpFragment(): SignUpFragment

    @ContributesAndroidInjector
    abstract fun injectAddNewRestaurantFragment(): AddNewRestaurantFragment
}