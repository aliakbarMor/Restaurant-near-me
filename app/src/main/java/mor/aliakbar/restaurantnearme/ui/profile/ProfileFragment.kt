package mor.aliakbar.restaurantnearme.ui.profile

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import dagger.android.support.DaggerFragment
import mor.aliakbar.restaurantnearme.R
import mor.aliakbar.restaurantnearme.databinding.FragmentProfileBinding
import mor.aliakbar.restaurantnearme.viewmodel.ViewModelFactory
import javax.inject.Inject

class ProfileFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var binding: FragmentProfileBinding
    lateinit var viewmodel: ProfileViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        viewmodel = ViewModelProvider(this, viewModelFactory).get(ProfileViewModel::class.java)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        val token = viewmodel.checkToken()
        if (token == null) {
            val action = ProfileFragmentDirections.actionProfileFragmentToLogInFragment()
            activity?.findNavController(R.id.nav_host_fragment)!!.navigate(action)
            return binding.root
        }

        viewmodel.initUser()


        return binding.root
    }


}
