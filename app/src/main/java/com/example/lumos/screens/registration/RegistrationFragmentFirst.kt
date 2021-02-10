package com.example.lumos.screens.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lumos.R
import com.example.lumos.databinding.FragmentRegistrationFirstBinding
import com.example.lumos.local.UserDatabase
import com.example.lumos.repository.NetworkRepository
import com.example.lumos.utils.RegistrationViewModelFactory
import com.example.lumos.viewmodel.RegistrationViewModel


class RegistrationFragmentFirst : Fragment() {
    private lateinit var binding: FragmentRegistrationFirstBinding
    private lateinit var firstName: String
    private lateinit var lastName: String
    private lateinit var email: String

    private val viewModel by activityViewModels<RegistrationViewModel> {
        RegistrationViewModelFactory(
            NetworkRepository(
                UserDatabase.getDatabase(requireActivity()).userDao()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_registration_first,
            container,
            false
        )
        binding.regButtonFirst.setOnClickListener {
            firstName = binding.regFirstName.text.toString()
            lastName = binding.regLastName.text.toString()
            email = binding.regEmail.text.toString()

            //check input
            if (checkInput()) {
                //apply changes to viewModel
                viewModel.registerFirst(firstName, lastName, email)
                //navigate to next fragment
                val action =
                    RegistrationFragmentFirstDirections.actionRegistrationFragmentFirstToRegistrationFragmentSecond()
                findNavController().navigate(action)
            }
        }

        return binding.root
    }

    //check inputs
    fun checkInput(): Boolean {
        if (firstName.isBlank() || lastName.isBlank() || email.isBlank()) {
            Toast.makeText(requireActivity(), "Empty fields", Toast.LENGTH_SHORT).show()
            return false
        } else if (!email.contains('@')) {
            Toast.makeText(requireActivity(), "Incorrect Email Entered", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}