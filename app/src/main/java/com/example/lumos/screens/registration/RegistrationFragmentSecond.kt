package com.example.lumos.screens.registration

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lumos.R
import com.example.lumos.databinding.FragmentRegistrationSecondBinding
import com.example.lumos.local.UserDatabase
import com.example.lumos.repository.NetworkRepository
import com.example.lumos.utils.viewmodelfactory.RegistrationViewModelFactory
import com.example.lumos.viewmodel.RegistrationViewModel
import com.example.lumos.viewmodel.ToolbarTitleViewModel

class RegistrationFragmentSecond : Fragment() {
    private lateinit var binding: FragmentRegistrationSecondBinding
    //create a shared viewmodel between fragments

    private val viewModel by activityViewModels<RegistrationViewModel> {
        RegistrationViewModelFactory(
            NetworkRepository(
                UserDatabase.getDatabase(requireActivity()).userDao()
            )
        )
    }

    private val toolbarTitleViewModel: ToolbarTitleViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_registration_second,
            container,
            false
        )
        binding.regButtonSecond.setOnClickListener {
            val action =
                RegistrationFragmentSecondDirections.actionRegistrationFragmentSecondToLoginFragment()
            val username = binding.regUsername.text.toString()
            val password = binding.regPassword.text.toString()
            val passwordConfirm = binding.regPasswordConfirm.text.toString()
            Log.i(TAG, "$username\n$password\n$passwordConfirm")
            if (checkInputs(password, passwordConfirm)) {
                viewModel.registerSecond(username, password, passwordConfirm)
                //finally call register function in viewmodel to send request to server
                viewModel.register()

                viewModel.level.observe(viewLifecycleOwner, { registerLevel ->
                    if (registerLevel == 1) {
                        //successful
                        Toast.makeText(
                            requireActivity(),
                            "Registration Successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(action)
                        viewModel.level.value = 0
                    } else if (registerLevel == -1) {
                        viewModel.registrationResponseData.observe(
                            viewLifecycleOwner,
                            { receivedData ->
                                if (receivedData.errors?.username != null) {
                                    Toast.makeText(
                                        requireActivity(),
                                        receivedData.errors.username[0],
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                        viewModel.level.value = 0
                    }
                })

            }


        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        toolbarTitleViewModel.changeTitle("Sign Up.")
    }

    fun checkInputs(password: String, passwordConfirm: String): Boolean {
        if (password.length < 8) {
            Toast.makeText(requireActivity(), "Password length too short", Toast.LENGTH_SHORT)
                .show()
            return false
        } else if (!password.equals(passwordConfirm)) {
            Toast.makeText(requireActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        } else
            return true
    }

    companion object {
        const val TAG = "RegistrationFragmentSecond"
    }
}