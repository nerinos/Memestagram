package com.example.snplc.ui.main.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.snplc.R
import com.example.snplc.data.entities.ProfileUpdate
import com.example.snplc.other.EventObserver
import com.example.snplc.ui.main.viewmodels.SettingsViewModel
import com.example.snplc.ui.slideUpViews
import com.example.snplc.ui.snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment() : Fragment(R.layout.fragment_settings) {

    @Inject
    lateinit var glide: RequestManager

    private val viewModel: SettingsViewModel by viewModels()

    private var curImageUri: Uri? = null

    private lateinit var cropContent: ActivityResultLauncher<Any?>

    private val cropActivityResultContract = object: ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                .setAspectRatio(1, 1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .getIntent(requireContext())
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uriContent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cropContent = registerForActivityResult(cropActivityResultContract) { uri ->
            uri?.let {
                viewModel.setCurImageUri(it)
                btnUpdateProfile.isEnabled = true
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        val uid = FirebaseAuth.getInstance().uid!!
        viewModel.getUser(uid)
        btnUpdateProfile.isEnabled = false
        etUsername.addTextChangedListener {
            btnUpdateProfile.isEnabled = true
        }
        etDescription.addTextChangedListener {
            btnUpdateProfile.isEnabled = true
        }

        ivProfileImage.setOnClickListener {
            cropContent.launch(null)
        }
        btnUpdateProfile.setOnClickListener {
            val username = etUsername.text.toString()
            val description = etDescription.text.toString()
            val profileUpdate = ProfileUpdate(uid, username, description, curImageUri)
            viewModel.updateProfile(profileUpdate)
        }

        slideUpViews(
            requireContext(),
            ivProfileImage,
            etUsername,
            etDescription,
            btnUpdateProfile
        )
    }

    private fun subscribeToObservers() {
        viewModel.getUserStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                settingsProgressBar.isVisible = false
                snackbar(it)
            },
            onLoading = { settingsProgressBar.isVisible = true}
        ) { user ->
            settingsProgressBar.isVisible = false
            glide.load(user.profilePictureUrl).into(ivProfileImage)
            etUsername.setText(user.username)
            etDescription.setText(user.description)
            btnUpdateProfile.isEnabled = false

        })

        viewModel.currentImageUri.observe(viewLifecycleOwner) { uri ->
            curImageUri = uri
            glide.load(uri).into(ivProfileImage)
        }
        viewModel.updateProfileStatus.observe(viewLifecycleOwner, EventObserver(
            onError = {
                settingsProgressBar.isVisible = false
                snackbar(it)
                btnUpdateProfile.isEnabled = true
            }, onLoading = {
                settingsProgressBar.isVisible = true
                btnUpdateProfile.isEnabled = false
            }
        ) {
            settingsProgressBar.isVisible = false
            btnUpdateProfile.isEnabled = false
            snackbar(requireContext().getString(R.string.profile_updated))

        })
    }

}