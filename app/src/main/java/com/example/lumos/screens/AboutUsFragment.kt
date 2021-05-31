package com.example.lumos.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.lumos.R
import com.example.lumos.databinding.FragmentAboutUsBinding
import com.example.lumos.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AboutUsFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAboutUsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about_us, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       
        binding.websiteButton.setOnClickListener {
            val url = Constants.BASE_URL
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): AboutUsFragment {
            val fragment = AboutUsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}