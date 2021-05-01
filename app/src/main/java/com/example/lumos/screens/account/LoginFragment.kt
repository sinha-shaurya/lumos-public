package com.example.lumos.screens.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.example.lumos.MainActivity
import com.example.lumos.R
import com.example.lumos.databinding.FragmentLoginBinding
import com.example.lumos.local.UserDatabase
import com.example.lumos.repository.NetworkRepository
import com.example.lumos.utils.LoginStatus
import com.example.lumos.utils.viewmodelfactory.LoginViewModelFactory
import com.example.lumos.viewmodel.LoginViewModel


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by activityViewModels {
        LoginViewModelFactory(
            NetworkRepository(
                UserDatabase.getDatabase(requireActivity()).userDao()
            )
        )
    }
    private lateinit var savedStateHandle: SavedStateHandle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        //hide/disable the back button in toolbar

        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

        binding.registerButton.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegistrationFragmentFirst()
            findNavController().navigate(action)
        }

        binding.loginUiButton.setOnClickListener {
            binding.apply {
                usernameInput.visibility = View.VISIBLE
                usernameInputLayout.visibility = View.VISIBLE
                passwordInputLayout.visibility = View.VISIBLE
                passwordInput.visibility = View.VISIBLE
                loginButton.visibility = View.VISIBLE
                loginUiButton.visibility = View.GONE
                registerButton.visibility = View.GONE

            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.usernameInput.isVisible) {
                        binding.apply {
                            usernameInput.visibility = View.GONE
                            usernameInputLayout.visibility = View.GONE
                            passwordInput.visibility = View.GONE
                            passwordInputLayout.visibility = View.GONE
                            loginButton.visibility = View.GONE
                            loginUiButton.visibility = View.VISIBLE
                            registerButton.visibility = View.VISIBLE
                        }
                    } else {
                        isEnabled = false
                        activity?.onBackPressed()
                    }
                }

            })
    }

    @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
    private fun login(username: String, password: String) {
        //login user
        viewModel.loginUser(username, password)
        viewModel.loginStatus.value = LoginStatus.LOADING
        //observe loginStatus for changes
        viewModel.loginStatus.observe(viewLifecycleOwner) { status ->
            //check for different status changes
            when (status) {
                LoginStatus.SUCCESS -> {
                    savedStateHandle.set(LOGIN_SUCCESSFUL, true)
                    val navController = findNavController()
                    println(navController)
                    navController.popBackStack(R.id.accountFragment, false)
                }

                LoginStatus.FAILURE -> {
                    //This means an API error or network issue has occurred
                    binding.loginButton.isClickable = true
                    binding.apply {
                        usernameInput.isEnabled = true
                        passwordInput.isEnabled = true
                    }
                    val errorMessage = viewModel.error.value
                    if (errorMessage != null)
                        Toast.makeText(requireActivity(), errorMessage.message, Toast.LENGTH_SHORT)
                            .show()
                    else
                        Toast.makeText(
                            requireActivity(), "Incorrect username or password", Toast.LENGTH_SHORT
                        )
                            .show()
                }

                LoginStatus.LOADING -> {
                    //prevent user clicks when network request is being performed

                    binding.apply {
                        loginButton.isClickable = false
                        usernameInput.isEnabled = false
                        passwordInput.isEnabled = false
                    }

                    Toast.makeText(requireActivity(), "Loading", Toast.LENGTH_SHORT).show()
                }
                LoginStatus.NOT_LOGGED_IN -> binding.apply {
                    //show toast for failed login
                    Toast.makeText(
                        requireContext(),
                        "Incorrect username or password",
                        Toast.LENGTH_SHORT
                    ).show()
                    loginButton.isClickable = true
                    usernameInput.isEnabled = true
                    passwordInput.isEnabled = true
                }
            }
        }
        //viewModel.loginStatus.value = LoginStatus.NOT_LOGGED_IN
    }


    companion object {
        const val LOGIN_SUCCESSFUL: String = "LOGIN_SUCCESSFUL"
    }


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        //so that it checks on loading as well
        viewModel.loginStatus.observe(viewLifecycleOwner) {
            when (it) {
                LoginStatus.SUCCESS -> findNavController().popBackStack()
                LoginStatus.LOADING -> toggleButtons(false)
                else -> {
                    //do nothing
                    toggleButtons(state = true)
                }
            }
        }

    }

    private fun toggleButtons(state: Boolean) {
        binding.apply {
            loginUiButton.isEnabled = state
            loginButton.isEnabled = state
        }
    }
}