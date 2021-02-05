package mor.aliakbar.restaurantnearme.ui.map

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression.*
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_map.*
import mor.aliakbar.restaurantnearme.R
import mor.aliakbar.restaurantnearme.databinding.FragmentMapBinding
import mor.aliakbar.restaurantnearme.storage.sharedPrefs.PreferencesManager
import mor.aliakbar.restaurantnearme.utils.NetworkUtility
import mor.aliakbar.restaurantnearme.viewmodel.ViewModelFactory
import timber.log.Timber
import javax.inject.Inject


class MapFragment : DaggerFragment(), PermissionsListener {

    //region variables
    private lateinit var permissionsManager: PermissionsManager

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var shp: PreferencesManager

    private lateinit var binding: FragmentMapBinding
    private lateinit var viewModel: MapViewModel

    private var mapView: MapView? = null
    private lateinit var mapbox: MapboxMap

    private var navigationMapRoute: NavigationMapRoute? = null

    private var numberRestRoute: Int = 0
    //endregion

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MapViewModel::class.java)
        viewModel.init()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        mapView = binding.mapview
        mapView!!.onCreate(savedInstanceState)
        mapView!!.getMapAsync { mapboxMap ->
            mapbox = mapboxMap
            if (shp.loadDarkTheme())
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            val style = if (shp.loadDarkTheme()) {
                getString(R.string.navigation_guidance_night)
            } else
                getString(R.string.navigation_guidance_day)
            mapbox.setStyle(style) {
                enableLocationComponent(it)
                addRestIcon(it)

                mapbox.addOnMapClickListener { point ->
                    val screenPoint = mapbox.projection.toScreenLocation(point)
                    val features = mapbox.queryRenderedFeatures(screenPoint, "LAYER_ID")
                    if (features.isNotEmpty()) {
                        val selectedFeature = features[0]
                        val name = selectedFeature.getStringProperty("name")
                        Toast.makeText(context, "رستوران $name", Toast.LENGTH_SHORT).show()
                    }
                    return@addOnMapClickListener true
                }
                mapbox.addOnMapLongClickListener { point ->

                    val controller = activity?.findNavController(R.id.nav_host_fragment)!!
                    val action =
                        MapFragmentDirections.actionMapFragmentToAddNewRestaurantFragment(point)
                    controller.navigate(action)

                    return@addOnMapLongClickListener true
                }
            }
        }

        viewModel.currentRoute.observe(viewLifecycleOwner) { directionsRoute ->
            if (directionsRoute == null) {
                return@observe
            }
            // Draw the route on the map
            if (navigationMapRoute != null) {
                navigationMapRoute!!.removeRoute()
            } else {
                navigationMapRoute =
                    NavigationMapRoute(
                        null,
                        binding.mapview,
                        mapbox,
                        R.style.NavigationMapRoute
                    )
            }
            navigationMapRoute!!.addRoute(directionsRoute)

            binding.startNavigation.setOnClickListener {
                val options = NavigationLauncherOptions.builder()
                    .directionsRoute(directionsRoute)
                    .build()
                NavigationLauncher.startNavigation(requireActivity(), options)
            }

        }

        binding.nearestRestaurant.setOnClickListener {
            if (mapbox.locationComponent.lastKnownLocation == null) {
                return@setOnClickListener
            }
            binding.nearestRestaurant.visibility = View.GONE
            binding.startNavigation.visibility = View.VISIBLE
            binding.nextRestaurant.visibility = View.VISIBLE
            getRoute(numberRestRoute)
        }

        binding.nextRestaurant.setOnClickListener {
            numberRestRoute++
            getRoute(numberRestRoute)
        }

        if (!NetworkUtility.isOnline(requireContext())) {
            Toast.makeText(requireContext(), "Turn on the network", Toast.LENGTH_LONG).show()
        }



        return binding.root
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        Timber.e("onRequestPermissionsResult")
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Toast.makeText(
            requireContext(),
            "This app needs location permissions to show its functionality",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            Timber.e("user location permission granted")
            enableLocationComponent(mapbox.style!!)
        } else {
            Timber.e("user location permission not granted")
            Toast.makeText(
                requireContext(), "user location permission not granted", Toast.LENGTH_LONG
            ).show()
            activity?.finish()
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {

            val customLocationComponentOptions = LocationComponentOptions.builder(requireContext())
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(requireContext(), R.color.mapbox_blue))
                .build()

            val locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(requireContext(), loadedMapStyle)
                    .locationComponentOptions(customLocationComponentOptions)
                    .build()

            mapbox.locationComponent.apply {
                activateLocationComponent(locationComponentActivationOptions)
                isLocationComponentEnabled = true
                cameraMode = CameraMode.TRACKING
                renderMode = RenderMode.COMPASS
            }

            Thread {
                while (true) {
                    Thread.sleep(1000)
                    viewModel.updateLocation(
                        mapbox.locationComponent.lastKnownLocation?.latitude,
                        mapbox.locationComponent.lastKnownLocation?.longitude
                    )
                }
            }.start()


        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(activity)
        }
    }

    private fun getRoute(numberRestRoute: Int) {
        val destinationPoint = Point.fromLngLat(
            viewModel.restaurants.value!![numberRestRoute].longitude,
            viewModel.restaurants.value!![numberRestRoute].latitude
        )
        val originPoint = Point.fromLngLat(
            mapbox.locationComponent.lastKnownLocation!!.longitude,
            mapbox.locationComponent.lastKnownLocation!!.latitude
        )

        NavigationRoute.builder(context)
            .accessToken(Mapbox.getAccessToken()!!)
            .origin(originPoint)
            .destination(destinationPoint)
            .build()
            .getRoute(viewModel.callback())
    }

    private fun addRestIcon(loadedMapStyle: Style) {
        val symbolLayerIconFeatureList: MutableList<Feature> = ArrayList()
        for (rest in viewModel.restaurants.value!!) {
            val fromGeometry =
                Feature.fromGeometry(Point.fromLngLat(rest.longitude, rest.latitude))
            fromGeometry.addStringProperty("name", rest.name)
            symbolLayerIconFeatureList.add(fromGeometry)
        }

        loadedMapStyle.addImage(
            "ICON_ID",
            BitmapFactory.decodeResource(this.resources, R.drawable.mapbox_marker_icon_default)
        )
        loadedMapStyle.addSource(
            GeoJsonSource(
                "SOURCE_ID",
                FeatureCollection.fromFeatures(symbolLayerIconFeatureList)
            )
        )
        loadedMapStyle.addLayer(
            SymbolLayer("LAYER_ID", "SOURCE_ID")
                .withProperties(
                    iconImage("ICON_ID"),
                    iconAllowOverlap(true),
                    iconIgnorePlacement(true)
                )
        )
    }


}