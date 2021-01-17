package com.example.lumos.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.example.lumos.R
import com.example.lumos.databinding.FragmentLoginBinding
import com.example.lumos.repository.NetworkRepository
import com.example.lumos.utils.LoginViewModelFactory
import com.example.lumos.viewmodel.LoginViewModel


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by activityViewModels<LoginViewModel>({
        LoginViewModelFactory(NetworkRepository())
    })
    private lateinit var savedStateHandle: SavedStateHandle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    companion object {
        const val LOGIN_SUCCESSFUL: String = "LOGIN_SUCCESSFUL"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle
        savedStateHandle.set(LOGIN_SUCCESSFUL, false)

        binding.loginButton.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()
            login(username, password)
        }
    }

    private fun login(username: String, password: String) {
        viewModel.loginUser(username, password)
        viewModel.loginState.observe(viewLifecycleOwner, Observer { result ->
            if (result) {
                savedStateHandle.set(LOGIN_SUCCESSFUL, true)
                findNavController().popBackStack()
            }
        })
    }
}