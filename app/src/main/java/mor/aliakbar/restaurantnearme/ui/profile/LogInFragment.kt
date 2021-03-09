package mor.aliakbar.restaurantnearme.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import dagger.android.support.DaggerFragment
import mor.aliakbar.restaurantnearme.R
import mor.aliakbar.restaurantnearme.databinding.FragmentLogInBinding
import mor.aliakbar.restaurantnearme.ui.ViewModelFactory
import javax.inject.Inject


class LogInFragment : DaggerFragment() {

    private lateinit var binding: FragmentLogInBinding
    private lateinit var viewmodel: LogInViewModel

    @Inject
    lateinit var vmFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_log_in, container, false)
        viewmodel = ViewModelProvider(this, vmFactory).get(LogInViewModel::class.java)
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this

        viewmodel.context = requireContext()


        viewmodel.eventGoToProfileFragment.observe(viewLifecycleOwner) {
            if (it) {

                val inputManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                inputManager!!.hideSoftInputFromWindow(
                    binding.edtLoginPass.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
                )

                val action = LogInFragmentDirections.actionLogInFragmentToProfileFragment()
                activity?.findNavController(R.id.nav_host_fragment)!!.navigate(action)

                viewmodel.eventGoToProfileFragment.value = false
            }
        }

        viewmodel.eventOnSignUpClick.observe(viewLifecycleOwner) {
            if (it) {
                val action = LogInFragmentDirections.actionLogInFragmentToSignUpFragment()
                activity?.findNavController(R.id.nav_host_fragment)!!.navigate(action)
                viewmodel.eventOnSignUpClick.value = false
            }
        }

        return binding.root
    }

}