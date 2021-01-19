package com.example.lumos.screens.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.lumos.R
import com.example.lumos.databinding.FragmentRegistrationFirstBinding


class RegistrationFragmentFirst : Fragment() {
    private lateinit var binding: FragmentRegistrationFirstBinding

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

        return binding.root
    }

}