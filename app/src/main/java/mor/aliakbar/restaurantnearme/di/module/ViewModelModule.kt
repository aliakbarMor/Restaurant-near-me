package mor.aliakbar.restaurantnearme.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import mor.aliakbar.restaurantnearme.ui.food.RestDetailViewModel
import mor.aliakbar.restaurantnearme.ui.food.FoodItemViewModel
import mor.aliakbar.restaurantnearme.ui.home.HomeViewModel
import mor.aliakbar.restaurantnearme.ui.home.RestaurantItemViewModel
import mor.aliakbar.restaurantnearme.ui.addNewRest.AddNewRestViewModel
import mor.aliakbar.restaurantnearme.ui.map.MapViewModel
import mor.aliakbar.restaurantnearme.ui.profile.LogInViewModel
import mor.aliakbar.restaurantnearme.ui.profile.ProfileViewModel
import mor.aliakbar.restaurantnearme.ui.profile.SignUpViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKeys(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKeys(RestDetailViewModel::class)
    abstract fun bindDetailFragmentViewModel(restDetailViewModel: RestDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKeys(RestaurantItemViewModel::class)
    abstract fun bindRestaurantItemViewModel(restaurantItemViewModel: RestaurantItemViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKeys(FoodItemViewModel::class)
    abstract fun bindFoodItemViewModel(foodItemViewModel: FoodItemViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKeys(MapViewModel::class)
    abstract fun bindMapViewModel(mapViewModel: MapViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKeys(ProfileViewModel::class)
    abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKeys(SignUpViewModel::class)
    abstract fun bindSignUpViewModel(signUpViewModel: SignUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKeys(LogInViewModel::class)
    abstract fun bindLogInViewModel(logInViewModel: LogInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKeys(AddNewRestViewModel::class)
    abstract fun bindAddNewRestaurantViewModel(addNewRestaurantViewModel: AddNewRestViewModel): ViewModel


}