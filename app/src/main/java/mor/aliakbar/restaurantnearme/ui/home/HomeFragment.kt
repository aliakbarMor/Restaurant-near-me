package mor.aliakbar.restaurantnearme.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.layout_switch.view.*
import mor.aliakbar.restaurantnearme.R
import mor.aliakbar.restaurantnearme.databinding.FragmentHomeBinding
import mor.aliakbar.restaurantnearme.storage.database.model.Restaurant
import mor.aliakbar.restaurantnearme.storage.sharedPrefs.PreferencesManager
import mor.aliakbar.restaurantnearme.ui.ViewModelFactory
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.ArrayList

class HomeFragment : DaggerFragment(), RestListener,
    NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    @Inject
    lateinit var sharedPrefs: PreferencesManager

    private lateinit var navController: NavController

    private var restaurantsAdapter: RestaurantsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
        viewModel.init()
        binding.homeViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.restaurants.observe(viewLifecycleOwner, {
            if (it != null) {
                if (restaurantsAdapter == null) {
                    val itemViewModel = ViewModelProvider(this, viewModelFactory)
                        .get(RestaurantItemViewModel::class.java)
                    restaurantsAdapter =
                        RestaurantsAdapter(it as ArrayList<Restaurant>, itemViewModel, this)
                    binding.recyclerView.adapter = restaurantsAdapter
                } else {
                    restaurantsAdapter!!.updateData(it as ArrayList<Restaurant>)
                    restaurantsAdapter!!.notifyDataSetChanged()
                }
            }
        })


        navController = activity?.findNavController(R.id.nav_host_fragment)!!
        addNavViewMenu()
        setOnBackClick()

        binding.navView.menu.findItem(R.id.theme).actionView.switch_layout.isChecked =
            sharedPrefs.loadDarkTheme()

        Thread {
            binding.navView.menu.findItem(R.id.theme)
                .actionView
                .switch_layout
                .setOnCheckedChangeListener { _, isChecked ->
                    if (!isChecked) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        sharedPrefs.saveDarkTheme(false)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        sharedPrefs.saveDarkTheme(true)

                    }
                }
        }.start()


        return binding.root
    }

    override fun onRestaurantClicked(restaurant: Restaurant) {
        val controller = activity?.findNavController(R.id.nav_host_fragment)!!
        val action = HomeFragmentDirections.actionHomeFragmentToRestDetailFragment(restaurant)
        controller.navigate(action)
    }

    override fun onRestaurantLongClicked(restaurant: Restaurant) {
//        TODO
    }

    override fun onPause() {
        super.onPause()
        restaurantsAdapter = null
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment()
                navController.navigate(action)
            }
            R.id.distanceNotification -> {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                DistanceNotifDialog.getInstance().showDialog(requireContext())
            }

        }
        return false
    }

    private fun addNavViewMenu() {
        binding.imageMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.navView.setNavigationItemSelectedListener(this)
    }

    private fun setOnBackClick() {
        activity?.onBackPressedDispatcher?.addCallback {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else if (!navController.popBackStack()) {
                this.handleOnBackPressed()
            }
        }
    }


}