package com.example.lumos.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.lumos.R
import com.example.lumos.databinding.FragmentAccountBinding
import com.example.lumos.repository.NetworkRepository
import com.example.lumos.utils.LoginViewModelFactory
import com.example.lumos.viewmodel.LoginViewModel


class AccountFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels<LoginViewModel>({
        LoginViewModelFactory(NetworkRepository())
    })
    private lateinit var binding: FragmentAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentAccountBinding>(
            inflater,
            R.layout.fragment_account,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        viewModel.loginState.observe(viewLifecycleOwner, Observer {
            //user has/is logged in
            if (it == true) {
                binding.userNameText.text = viewModel.userCurrent.value?.userName ?: ""
            }
            //user has not logged in
            else {
                navController.navigate(R.id.loginFragment)
            }
        })
        binding.logoutButon.setOnClickListener {
            viewModel.loginState.value=false
            viewModel.userCurrent.value=null
            navController.navigate(R.id.loginFragment)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navController = findNavController()
        val currentBackStackEntry = navController.currentBackStackEntry!!
        val savedStateHandle = currentBackStackEntry.savedStateHandle
        savedStateHandle.getLiveData<Boolean>(LoginFragment.LOGIN_SUCCESSFUL)
            .observe(currentBackStackEntry, Observer { success ->
                if (!success) {
                    val startDestination = navController.graph.startDestination
                    val navOptions = NavOptions.Builder().setPopUpTo(startDestination, true)
                        .build()
                    navController.navigate(startDestination, null, navOptions)
                }
            })
    }
}