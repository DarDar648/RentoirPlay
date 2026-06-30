package com.app.tiketin.v1

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.tiketin.v1.databinding.FragmentProfileBinding
import com.app.tiketin.v1.model.UserProfileData
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: UserSessionManager
    private lateinit var profileManager: UserProfileManager
    private var currentUsername: String? = null
    private var selectedImageUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            val localPath = copyImageToInternalStorage(uri)
            if (localPath != null) {
                selectedImageUri = Uri.fromFile(File(localPath))
                binding.ivProfilePhoto.setImageURI(selectedImageUri)
                binding.ivProfilePhoto.setPadding(0, 0, 0, 0)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = UserSessionManager(requireContext())
        profileManager = UserProfileManager(requireContext())
        currentUsername = sessionManager.getUsername()

        setupToolbar()
        loadProfileData()
        setupSaveButton()

        binding.ivProfilePhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    private fun setupToolbar() {
        val activity = activity as? AppCompatActivity
        if (activity != null) {
            activity.setSupportActionBar(binding.toolbar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    private fun copyImageToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
            val fileName = "profile_${currentUsername ?: "user"}_${System.currentTimeMillis()}.jpg"
            val file = File(requireContext().filesDir, fileName)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            Log.e("ProfileFragment", "Error copying image: ${e.message}")
            null
        }
    }

    private fun loadProfileData() {
        val username = currentUsername ?: return
        binding.tvUsername.text = "@$username"
        val profile = profileManager.getProfile(username)

        binding.etNamaLengkap.setText(profile.namaLengkap)
        if (profile.umur > 0) binding.etUmur.setText(profile.umur.toString())
        binding.etNomorKtp.setText(profile.nomorKtp)
        binding.etNomorTelepon.setText(profile.nomorTelepon)
        binding.etEmail.setText(profile.email)

        if (!profile.profileImageUri.isNullOrEmpty()) {
            try {
                val uri = if (profile.profileImageUri.startsWith("/")) Uri.fromFile(File(profile.profileImageUri)) else Uri.parse(profile.profileImageUri)
                binding.ivProfilePhoto.setImageURI(uri)
                binding.ivProfilePhoto.setPadding(0, 0, 0, 0)
                selectedImageUri = uri
            } catch (e: Exception) {
                binding.ivProfilePhoto.setImageResource(R.drawable.ic_default_profile)
                binding.ivProfilePhoto.setPadding(16, 16, 16, 16)
            }
        }
    }

    private fun setupSaveButton() {
        binding.btnSaveProfile.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        val username = currentUsername ?: return
        val namaLengkap = binding.etNamaLengkap.text.toString().trim()
        val umurText = binding.etUmur.text.toString().trim()
        val nomorKtp = binding.etNomorKtp.text.toString().trim()
        val nomorTelepon = binding.etNomorTelepon.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()

        if (namaLengkap.isEmpty()) { binding.etNamaLengkap.error = "Nama lengkap harus diisi"; return }
        val umur = umurText.toIntOrNull()
        if (umur == null || umur <= 0) { binding.etUmur.error = "Umur tidak valid"; return }

        val imagePathToSave = if (selectedImageUri?.scheme == "file") selectedImageUri?.path else selectedImageUri?.toString()
        val profile = UserProfileData(namaLengkap, umur, nomorKtp, nomorTelepon, email, imagePathToSave)
        profileManager.saveProfile(username, profile)
        Toast.makeText(requireContext(), "Profil berhasil disimpan!", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}