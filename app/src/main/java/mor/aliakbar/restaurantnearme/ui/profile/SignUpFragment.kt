package mor.aliakbar.restaurantnearme.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import mor.aliakbar.restaurantnearme.R
import mor.aliakbar.restaurantnearme.databinding.FragmentSignUpBinding
import mor.aliakbar.restaurantnearme.utils.EmailUtility.isEmailValid
import mor.aliakbar.restaurantnearme.viewmodel.ViewModelFactory
import javax.inject.Inject

class SignUpFragment : DaggerFragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewmodel: SignUpViewModel

    @Inject
    lateinit var vmFactory: ViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        viewmodel = ViewModelProvider(this, vmFactory).get(SignUpViewModel::class.java)
        viewmodel.context = requireContext()
        binding.viewmodel = viewmodel
        binding.lifecycleOwner = this


        viewmodel.eventOnSignUpClick.observe(viewLifecycleOwner) {
            if (it) {
                if (viewmodel.user.value!!.name.isEmpty() || viewmodel.user.value!!.family.isEmpty() ||
                    viewmodel.user.value!!.phoneNumber.isEmpty() || viewmodel.user.value!!.email.isEmpty() ||
                    !isEmailValid(viewmodel.user.value!!.email) || viewmodel.user.value!!.password.length < 4 ||
                    viewmodel.user.value!!.password != viewmodel.repeatPassword.value
                ) {
                    if (viewmodel.user.value!!.name.isEmpty()) {
                        binding.edtName.error = "این فیلد یاید پرشود"
                    }
                    if (viewmodel.user.value!!.family.isEmpty()) {
                        binding.edtFamily.error = "این فیلد یاید پرشود"
                    }
                    if (viewmodel.user.value!!.phoneNumber.isEmpty()) {
                        binding.edtPhoneNumber.error = "این فیلد یاید پرشود"
                    }
                    if (viewmodel.user.value!!.email.isEmpty()) {
                        binding.edtEmail.error = "این فیلد یاید پرشود"
                    } else if (!isEmailValid(viewmodel.user.value!!.email)) {
                        binding.edtEmail.error = "ایمیل نامعتبر است"
                    }
                    if (viewmodel.user.value!!.password != viewmodel.repeatPassword.value) {
                        binding.edtPass.error = "کلمه عبور و تکرار آن یکسان نیستند"
                        binding.edtRepeatPass.error = "کلمه عبور و تکرار آن یکسان نیستند"
                    } else if (viewmodel.user.value!!.password.length < 4) {
                        binding.edtPass.error = "کلمه عبور باید حداقل 4 کارکتر باشد"
                        binding.edtRepeatPass.error = "کلمه عبور باید حداقل 4 کارکتر باشد"
                    }
                } else {
                    viewmodel.signUp()
                }
                viewmodel.eventOnSignUpClick.value = false
            }
        }

        viewmodel.eventGoToProfileFragment.observe(viewLifecycleOwner) {
            if (it) {
                val action = SignUpFragmentDirections.actionSignUpFragmentToProfileFragment()
                activity?.findNavController(R.id.nav_host_fragment)!!.navigate(action)

                viewmodel.eventGoToProfileFragment.value = false
            }
        }

        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

}