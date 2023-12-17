package com.example.uas_film

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uas_film.databinding.FragmentRegisterrBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterrFragment : Fragment() {

    private lateinit var binding: FragmentRegisterrBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        firestore = FirebaseFirestore.getInstance()

        with(binding) {
            regisBtn.setOnClickListener {
                val email = email.text.toString().trim()
                val username = username.text.toString().trim()
                val phone = phone.text.toString().trim()
                val password = pass.text.toString()

                if (email.isNotEmpty() && username.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty()) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Save user data to Firestore
                                val newAccount = Account(email, username, password, "user")
                                saveUserDataToFirestore(newAccount)

                                // Save login status to SharedPreferences
                                saveLoginStatus(true)

                                // Navigate to HomeActivity or AdminActivity based on userType
                                navigateToHomeOrAdmin("user")
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Registration failed: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e("RegisterrFragment", "Error creating user", task.exception)
                            }
                        }
                } else {
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveUserDataToFirestore(account: Account) {
        firestore.collection("accounts")
            .add(account)
            .addOnSuccessListener { documentReference ->
                Log.d("RegisterrFragment", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("RegisterrFragment", "Error adding document to Firestore", e)
                Toast.makeText(
                    requireContext(),
                    "Error adding document to Firestore: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun saveLoginStatus(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }

    private fun navigateToHomeOrAdmin(userType: String) {
        val intent = if (userType == "admin") {
            Intent(requireContext(), HomeAdminActivity::class.java)
        } else {
            Intent(requireContext(), Navigation::class.java)
        }

        startActivity(intent)
        requireActivity().finish()
    }
}
