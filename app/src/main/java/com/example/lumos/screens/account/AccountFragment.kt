package com.example.lumos.screens.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.lumos.R
import com.example.lumos.databinding.FragmentAccountBinding
import com.example.lumos.local.UserDatabase
import com.example.lumos.network.adapters.BookmarkItemAdapter
import com.example.lumos.network.dataclasses.practice.AnsweredQuestion
import com.example.lumos.repository.NetworkRepository
import com.example.lumos.utils.LoginStatus
import com.example.lumos.utils.viewmodelfactory.LoginViewModelFactory
import com.example.lumos.utils.viewmodelfactory.QuestionViewModelFactory
import com.example.lumos.viewmodel.LoginViewModel
import com.example.lumos.viewmodel.QuestionViewModel
import com.example.lumos.viewmodel.ToolbarTitleViewModel


class AccountFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels {
        LoginViewModelFactory(
            NetworkRepository(
                UserDatabase.getDatabase(requireActivity()).userDao()
            )
        )
    }

    private val questionViewModel: QuestionViewModel by activityViewModels {
        QuestionViewModelFactory(
            NetworkRepository(
                UserDatabase.getDatabase(requireActivity()).userDao()
            )
        )
    }

    private val toolbarTitleViewModel: ToolbarTitleViewModel by activityViewModels()

    private var _binding: FragmentAccountBinding? = null
    val binding get() = _binding!!

    val adapter = BookmarkItemAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(
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



        binding.bookmarkList.adapter = adapter

        viewModel.loginStatus.observe(viewLifecycleOwner) { status ->
            @Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
            when (status) {
                //check for login status
                //success
                LoginStatus.SUCCESS -> {
                    viewModel.getUserData()
                    binding.logoutButton.isVisible = true
                    viewModel.localData.observe(viewLifecycleOwner) {
                        binding.userNameText.text = it.userName
                    }
                    retrieveQuestions()

                    questionViewModel.savedPosts.observe(viewLifecycleOwner) {
                        adapter.submitList(it)
                    }

                }
                LoginStatus.NOT_LOGGED_IN -> {
                    val action = AccountFragmentDirections.actionAccountFragmentToLoginFragment()
                    navController.navigate(action)
                }
                LoginStatus.LOADING -> {
                    binding.logoutButton.isVisible = false
                }
                LoginStatus.FAILURE -> {
                    Toast.makeText(
                        requireActivity(),
                        "An error occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                    val action = AccountFragmentDirections.actionAccountFragmentToLoginFragment()
                    navController.navigate(action)
                }
            }
        }
        binding.logoutButton.setOnClickListener {
            viewModel.logoutUser()
            navController.popBackStack(R.id.loginFragment, false)
            //navController.navigate(R.id.loginFragment)//change to login fragment
        }

        binding.answeredQuestionsButton.setOnClickListener {
            val action = AccountFragmentDirections.actionAccountFragmentToAccountQuestions()
            findNavController().navigate(action)
        }

    }

    override fun onDestroy() {

        super.onDestroy()

        if (_binding != null) {
            Log.i(TAG, "onDestroy() called ,${binding}")
            _binding!!.bookmarkList.adapter = null
            _binding = null
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navController = findNavController()
        val currentBackStackEntry = navController.currentBackStackEntry!!
        println(currentBackStackEntry)
        val savedStateHandle = currentBackStackEntry.savedStateHandle
        savedStateHandle.getLiveData<Boolean>(LoginFragment.LOGIN_SUCCESSFUL)
            .observe(currentBackStackEntry, { success ->
                if (!success) {
                    val startDestination = navController.graph.startDestination
                    val navOptions = NavOptions.Builder().setPopUpTo(startDestination, true)
                        .build()
                    navController.navigate(startDestination, null, navOptions)
                }
            })
    }

    override fun onStart() {
        super.onStart()
        toolbarTitleViewModel.changeTitle("Profile .")
    }

    fun retrieveQuestions() {
        questionViewModel.getSubmittedAnswer()
        questionViewModel.points.observe(viewLifecycleOwner) { currentPoints ->
            val pointText = "${currentPoints} Points Scored"
            binding.points.text = pointText
        }
        questionViewModel.submittedAnswers.observe(viewLifecycleOwner) {
            binding.apply {
                answeredQuestionsButton.isVisible = it != emptyList<AnsweredQuestion>()
                answerDisclaimerText.isVisible = it != emptyList<AnsweredQuestion>()

                val questionsAnsweredText = "${it.size} Questions Answered"
                questionsAnswered.text = questionsAnsweredText


            }
        }
    }

    companion object {
        private const val TAG = "AccountFragment"
    }
}