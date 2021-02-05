package mor.aliakbar.restaurantnearme.ui.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.android.support.DaggerFragment
import mor.aliakbar.restaurantnearme.R
import mor.aliakbar.restaurantnearme.databinding.FragmentRestDetailBinding
import mor.aliakbar.restaurantnearme.storage.database.model.Food
import mor.aliakbar.restaurantnearme.viewmodel.ViewModelFactory
import javax.inject.Inject

class RestDetailFragment : DaggerFragment() {

    private var foodAdapter: FoodAdapter? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentRestDetailBinding
    private lateinit var viewModelRest: RestDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rest_detail, container, false)
        viewModelRest = ViewModelProvider(this, viewModelFactory).get(RestDetailViewModel::class.java)
        viewModelRest.init(RestDetailFragmentArgs.fromBundle(requireArguments()).restaurant)
        binding.detailViewModel = viewModelRest
        binding.lifecycleOwner = this

        viewModelRest.foods.observe(viewLifecycleOwner, {
            if (it != null) {
                if (foodAdapter == null) {
                    val itemViewModel =
                        ViewModelProvider(this, viewModelFactory).get(FoodItemViewModel::class.java)
                    foodAdapter = FoodAdapter(it as ArrayList<Food>, itemViewModel)
                    binding.foodRecycler.adapter = foodAdapter

                }
            }
        })
        viewModelRest.eventGoToLogInFragment.observe(viewLifecycleOwner, {
            if (it) {
                val controller = activity?.findNavController(R.id.nav_host_fragment)!!
                val action = RestDetailFragmentDirections.actionRestDetailFragmentToProfileFragment()
                controller.navigate(action)
                viewModelRest.eventGoToLogInFragment.value = false
            }
        })
        viewModelRest.eventUpdateView.observe(viewLifecycleOwner, {
            if (it) {
                binding.notifyPropertyChanged(R.id.img_star1)
                binding.notifyPropertyChanged(R.id.img_star2)
                binding.notifyPropertyChanged(R.id.img_star3)
                binding.notifyPropertyChanged(R.id.img_star4)
                binding.notifyPropertyChanged(R.id.img_star5)

                viewModelRest.eventUpdateView.value = false
            }
        })

        val manager = GridLayoutManager(activity, 2)
        binding.foodRecycler.layoutManager = manager

        return binding.root
    }


}