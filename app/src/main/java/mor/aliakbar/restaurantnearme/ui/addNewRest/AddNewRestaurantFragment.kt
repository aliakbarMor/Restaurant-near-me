package mor.aliakbar.restaurantnearme.ui.addNewRest

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.mapbox.mapboxsdk.Mapbox.getApplicationContext
import dagger.android.support.DaggerFragment
import mor.aliakbar.restaurantnearme.R
import mor.aliakbar.restaurantnearme.api.ApiManager
import mor.aliakbar.restaurantnearme.databinding.FragmentAddNewRestaurantBinding
import mor.aliakbar.restaurantnearme.storage.database.model.Food
import mor.aliakbar.restaurantnearme.storage.database.model.Meal
import mor.aliakbar.restaurantnearme.ui.ViewModelFactory
import timber.log.Timber
import javax.inject.Inject


class AddNewRestaurantFragment : DaggerFragment() {

    @Inject
    lateinit var apiManager: ApiManager

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentAddNewRestaurantBinding
    private lateinit var viewModel: AddNewRestViewModel

    private var addFoodAdapter: AddFoodAdapter? = null
    private var addMealAdapter: AddMealAdapter? = null

    companion object {
        private const val REQUEST_EXTERNAL_STORAGE = 1
        private val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_new_restaurant, container, false
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddNewRestViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this


        addFoodAdapter()
        binding.txtAddFood.setOnClickListener {
            addFoodAdapter()
        }
        addMealAdapter()
        binding.txtAddMeal.setOnClickListener {
            addMealAdapter()
        }
        binding.imageRest.setOnClickListener {
            val verifyStoragePermissions = verifyStoragePermissions()
            Timber.i(verifyStoragePermissions.toString())
            if (verifyStoragePermissions()) {
                showImagePopup()
            }
        }

        binding.confirm.setOnClickListener {
            val allItemFood = addFoodAdapter!!.getAllItem()
            val allItemMeal = addMealAdapter!!.getAllItem()

            val latlng = AddNewRestaurantFragmentArgs.fromBundle(requireArguments()).latlng
            viewModel.insertRestInToDb(latlng.latitude, latlng.longitude, allItemFood, allItemMeal)
        }
        binding.storeRestaurantsOnTheServer.setOnClickListener{
            viewModel.insertRestsInToServer()

        }


        viewModel.eventGoToBackToMapFragment.observe(viewLifecycleOwner) {
            if (it)
                activity?.findNavController(R.id.nav_host_fragment)?.popBackStack()
        }


        return binding.root
    }

    private fun addFoodAdapter() {
        if (addFoodAdapter == null) {
            val list = ArrayList<Food>()
            list.add(Food(0, ""))
            addFoodAdapter = AddFoodAdapter(list)
            binding.recyclerAddFood.adapter = addFoodAdapter
        } else {
            addFoodAdapter!!.addItem()
            addFoodAdapter!!.notifyDataSetChanged()
        }
    }

    private fun addMealAdapter() {
        if (addMealAdapter == null) {
            val list = ArrayList<Meal>()
            list.add(Meal(0, "", "", ""))
            addMealAdapter = AddMealAdapter(list)
            binding.recyclerAddMeal.adapter = addMealAdapter
        } else {
            addMealAdapter!!.addItem()
            addMealAdapter!!.notifyDataSetChanged()
        }
    }


    private fun showImagePopup() {
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_PICK
        val chooserIntent = Intent.createChooser(galleryIntent, "Choose image")
        startActivityForResult(chooserIntent, 100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), "Unable to pick image", Toast.LENGTH_LONG)
                    .show()
                return
            }
            val imageUriLocal: Uri? = data.data
            binding.imageRest.setImageURI(imageUriLocal)
            viewModel.setRealPathFromURI(imageUriLocal)
        }
    }


    private fun verifyStoragePermissions(): Boolean {
        val permission = ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
            false
        } else
            true
    }


}