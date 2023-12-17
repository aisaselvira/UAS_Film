package com.example.uas_film

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.uas_film.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Set click listener for btnLogout
        binding.btnLogout.setOnClickListener {
            logout()
        }

        // Load and display user data
        loadUserData()
    }

    private fun loadUserData() {
        val userId = firebaseAuth.currentUser?.uid
        userId?.let { uid ->
            firestore.collection("accounts")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    val account = document.toObject(Account::class.java)
                    account?.let {
                        // Set the values to corresponding TextViews
                        binding.usernameProfile.text = "Username: ${it.username}"
                        binding.emailProfile.text = "Email: ${it.email}"
                        binding.phoneProfile.text = "Phone: ${it.phone}"
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error loading user data: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("ProfileFragment", "Error loading user data", e)
                }
        }
    }

    private fun logout() {
        // Clear user session, update SharedPreferences, etc.
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()

        // Show a toast message
        Toast.makeText(requireContext(), "Logout successful", Toast.LENGTH_SHORT).show()

        // Redirect to the login screen or perform other actions as needed
        // For example, you can use the following code:
        val intent = Intent(requireContext(), LoginRegisterActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // binding is automatically set to null, so no need to explicitly set it to null
    }
}