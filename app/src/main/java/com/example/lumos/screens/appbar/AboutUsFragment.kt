package com.example.lumos.screens.appbar

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.lumos.R
import com.example.lumos.databinding.FragmentAboutUsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AboutUsFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAboutUsBinding? = null
    private val binding get() = _binding!!


    //override on dialog to start bottom sheet in fully expanded state
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)

        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let {
                val behavior = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about_us, container, false)
        return binding.root
    }

    //destroy instance of data binding object when onDestroy() is called
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.instagramLinkIcon.setOnClickListener {
            val url = resources.getString(R.string.instagram_link)
            openUrl(url)
        }
        binding.facebookLinkIcon.setOnClickListener {
            val url = resources.getString(R.string.facebook_link)
            openUrl(url)
        }
        binding.websiteLinkIcon.setOnClickListener {
            val url = resources.getString(R.string.website_link)
            openUrl(url)
        }

    }

    //utility function to setup full height of the dialog
    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    //function open web links in browser
    private fun openUrl(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    companion object {
        /**
         * Method to return a new instance of [AboutUsFragment]
         */
        @JvmStatic
        fun newInstance(bundle: Bundle): AboutUsFragment {
            val fragment = AboutUsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}