package com.example.uas_film.Fragment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.example.uas_film.Activity.HomeAdminActivity
import com.example.uas_film.Activity.MainActivity
import com.example.uas_film.Activity.Navigation
import com.example.uas_film.Model.Account
import com.example.uas_film.R
import com.example.uas_film.databinding.FragmentRegisterrBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterrFragment : Fragment() {

    private lateinit var binding: FragmentRegisterrBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var firestore: FirebaseFirestore

    private val channelId = "TEST_NOTIFICATION"
    private val notifId = 90

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
                                val newAccount = Account(email, username, password, phone,"user")
                                saveUserDataToFirestore(newAccount)

                                // Save login status to SharedPreferences
                                saveLoginStatus(true)

                                // Create and show the notification
                                showNotification()

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

    private fun showNotification() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notifImage = BitmapFactory.decodeResource(resources,
            R.drawable.sign_up_success)

        val builder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setContentTitle("Sign Up")
            .setContentText("Success")
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(notifImage)
            )
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)


        val notifManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notifManager.notify(notifId, builder.build())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notifChannel = NotificationChannel(
                channelId,
                "Notification App",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            with(notifManager) {
                createNotificationChannel(notifChannel)
                notify(notifId, builder.build())
            }
        } else {
            notifManager.notify(notifId, builder.build())
        }
    }

    private fun saveUserDataToFirestore(akun: Account) {
        val userId = firebaseAuth.currentUser?.uid
        userId?.let { uid ->
            firestore.collection("account")
                .document(uid)
                .set(akun) // Menggunakan set() untuk menggantikan add() agar dapat menggunakan UID sebagai ID dokumen
                .addOnSuccessListener {
                    Log.d("RegisterFragment", "DocumentSnapshot added with ID: $uid")
                }
                .addOnFailureListener { e ->
                    Log.e("RegisterFragment", "Error adding document to Firestore", e)
                    Toast.makeText(
                        requireContext(),
                        "Error adding document to Firestore: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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