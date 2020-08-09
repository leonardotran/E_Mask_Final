package com.vietnamese.maskregion.ui.dashboard

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.vietnamese.maskregion.MainActivity
import com.vietnamese.maskregion.R
import com.vietnamese.maskregion.databinding.ActivityLoginBinding
import com.vietnamese.maskregion.databinding.FragmentProfileBinding
import com.vietnamese.maskregion.model.Profile
import com.vietnamese.maskregion.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {
    private val TAG = "Profile"
    private lateinit var binding: FragmentProfileBinding

    val db = Firebase.firestore
    var profileExists = false
    lateinit var profile: Profile


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        binding.signOutButton.setOnClickListener {
            AuthUI.getInstance()
                .signOut(this.requireContext())
                .addOnCompleteListener {
                    goLogin()
                }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = Firebase.auth.currentUser
        binding.textDisplayName.text = user?.displayName.toString()
        binding.textDescription.text = ""
        binding.textContacted.text = ""
        binding.textStatus.text = ""
        binding.textOccupation.text = ""
        ArrayAdapter.createFromResource(
            requireContext(), R.array.covid_status, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.statusSpinner.adapter = adapter
        }
        user?.uid?.let {
            val userRef = user.uid.let { db.collection("profile").document(it) }
            userRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    profileExists = true
                    Log.d(TAG, "Current data: ${snapshot.data}")
                    snapshot.toObject(Profile::class.java)?.let {
                        profile = it
                        loadProfile(profile)
                    }
                } else {
                    Log.d(TAG, "Current data: null")
                }
            }

            binding.buttonEdit.setOnClickListener {
                binding.layoutEdit.visibility = View.VISIBLE
                binding.buttonEdit.visibility = View.GONE
            }
            binding.buttonCancel.setOnClickListener {
                binding.layoutEdit.visibility = View.GONE
                binding.buttonEdit.visibility = View.VISIBLE
            }
            binding.buttonUpdate.setOnClickListener {
                binding.layoutEdit.visibility = View.GONE
                binding.buttonEdit.visibility = View.VISIBLE
                val position = binding.statusSpinner.selectedItemPosition
                if (profileExists) {
                    if (position != profile.status) {
                        if (position == 1) {
                            profile.contacted_ids.forEach { contactId ->
                                val userTemp = db.collection("profile").document(contactId)
                                userTemp.update("warning", 1)
                            }
                        }
                        userRef.update("status", position)
                    }
                }
            }

        }
    }

    fun loadProfile(profile: Profile) {
        if (activity == null) {
            return
        }
        profile.photoUrl?.let {
            Glide.with(requireContext()).load(profile.photoUrl)
                .apply(RequestOptions.centerInsideTransform())
                .into(binding.profileImage)
        }
        binding.textDisplayName.text = profile.displayName.toString()
        binding.textDescription.text = profile.description.toString()
        binding.textContacted.text = profile.contacted_ids.size.toString()
        binding.textStatus.text = parseStaus(profile.status)
        binding.textOccupation.text = profile.occupation.toString()
        if(profile.warning == 1){
            binding.warningLayout.visibility = View.VISIBLE
            binding.textWarning.text = getString(R.string.may_be_infected)
        }
        else{
            binding.warningLayout.visibility = View.GONE
        }

        var contactedText = ""
        profile.contacted_ids?.forEach { id ->
            contactedText = contactedText + id + "\n"
            Log.d("contacted", "" + id)
        }
        binding.contactedIds.setText(contactedText)
    }



    fun parseStaus(status: Int?): String {
        when (status) {
            0 -> {
                binding.statusSpinner.setSelection(0)
                binding.textStatus.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorNegative
                    )
                )
                return "Negative"
            }
            1 -> {
                binding.statusSpinner.setSelection(1)
                binding.textStatus.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPositive
                    )
                )
                return "Positive"
            }
            2 -> {
                binding.statusSpinner.setSelection(0)
                binding.textStatus.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorMayBeInfected
                    )
                )
                return "May be Infected"
            }
            else -> return ""
        }
    }

    fun goLogin() {
        activity?.let {
            val intent = Intent(it, LoginActivity::class.java)
            it.startActivity(intent)
            it.finish()
        }
    }
}