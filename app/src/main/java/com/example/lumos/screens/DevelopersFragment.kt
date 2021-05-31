package com.example.lumos.screens

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
import com.example.lumos.databinding.FragmentDevelopersBinding
import com.example.lumos.utils.developers.Developer
import com.example.lumos.utils.developers.DeveloperListAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DevelopersFragment : BottomSheetDialogFragment(), DeveloperListAdapter.onLinkClickListener {

    private var _binding: FragmentDevelopersBinding? = null
    private val binding get() = _binding!!

    val adapter = DeveloperListAdapter(this)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)

        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let {
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        return dialog

    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_developers, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.developerList.adapter = adapter
        binding.developerList.setHasFixedSize(true)
        getDeveloperInfo()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun getDeveloperInfo() {
        //5 developers

        val developer1 = convertToDeveloperObject(resources.getStringArray(R.array.Developer1))
        val developer2 = convertToDeveloperObject(resources.getStringArray(R.array.Developer2))
        val developer3 = convertToDeveloperObject(resources.getStringArray(R.array.Developer3))
        val developer4 = convertToDeveloperObject(resources.getStringArray(R.array.Developer4))
        val developer5 = convertToDeveloperObject(resources.getStringArray(R.array.Developer5))

        val developerList = listOf(developer1, developer2, developer3, developer4, developer5)
        adapter.submitList(developerList)
    }

    private fun convertToDeveloperObject(list: Array<String>): Developer {
        val name = list[0]
        val role = list[1]
        val gitHubLink = list[2]
        val linkedInLink = list[3]
        val instagramLink = list[4]
        val personalWebsiteLink = list[5]
        val twitterLink = list[6]
        val emailLink = list[7]
        println(list.asList().toString())
        return Developer(
            name,
            role,
            gitHubLink,
            linkedInLink,
            instagramLink,
            personalWebsiteLink,
            twitterLink,
            emailLink
        )
    }


    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?): DevelopersFragment {
            val fragment = DevelopersFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onLinkClick(link: String, linkType: String) {
        val intent = if (link.equals("email", ignoreCase = true)) {
                Intent(Intent.ACTION_SEND).apply {
                    type = "*/*"
                    putExtra(Intent.EXTRA_EMAIL, link)
                }
            } else {
                val uri = Uri.parse(link)
                Intent(Intent.ACTION_VIEW, uri)
            }
        try{
            startActivity(intent)
        }
        catch (e:Exception){}

    }
}