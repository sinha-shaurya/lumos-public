package com.example.lumos.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.lumos.R
import com.example.lumos.databinding.FragmentThemeBottomSheetBinding
import com.example.lumos.datastore
import com.example.lumos.repository.UserPreferencesRepository
import com.example.lumos.utils.viewmodelfactory.UserPreferencesViewModelFactory
import com.example.lumos.viewmodel.UserPreferencesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ThemeBottomSheetFragment : BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<UserPreferencesViewModel> {
        UserPreferencesViewModelFactory(
            userPreferencesRepository = UserPreferencesRepository(
                requireContext().datastore
            )
        )
    }

    private var _binding: FragmentThemeBottomSheetBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_theme_bottom_sheet,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //check radio buttons appropriately

        val themeValue = viewModel.userPreferences.value

        when (themeValue) {
            AppCompatDelegate.MODE_NIGHT_NO -> binding.lightButton.isChecked = true
            AppCompatDelegate.MODE_NIGHT_YES -> binding.nightButton.isChecked = true
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> binding.followSystem.isChecked = true
            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> binding.batterySaverButton.isChecked = true
        }

        /*
        Set onChecked listeners for radio buttons
         */

        binding.lightButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.changeTheme(AppCompatDelegate.MODE_NIGHT_NO)
        }
        binding.nightButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.changeTheme(AppCompatDelegate.MODE_NIGHT_YES)
        }

        binding.followSystem.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.changeTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        binding.batterySaverButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.changeTheme(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
        }
        /*
        * Only show the follow system button on SDK Level 29 and above
        * Show disclaimer text on devices lower than SDK Level 29
         */
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            binding.followSystem.isVisible = true
        } else {
            binding.themeDisclaimerText.isVisible = true
            binding.batterySaverButton.isVisible = true
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): ThemeBottomSheetFragment {
            val fragment = ThemeBottomSheetFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}