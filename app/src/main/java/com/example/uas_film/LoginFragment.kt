package com.example.uas_film

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.example.uas_film.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    private val channelId = "TEST_NOTIFICATION"
    private val notifId = 90

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Move the NotificationManager and PendingIntent creation to onViewCreated
        val notifManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            0
        }

        val intent = Intent(requireContext(), MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(requireContext(), 0, intent, flag)

        binding.loginBtn.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.pass.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Save login status to SharedPreferences
                            saveLoginStatus(true)
                            val notifImage = BitmapFactory.decodeResource(resources,
                                R.drawable.sign_in_success)

                            val builder = NotificationCompat.Builder(requireContext(), channelId)
                                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                                .setContentTitle("Sign In")
                                .setContentText("Succes")
                                .setStyle(
                                    NotificationCompat.BigPictureStyle()
                                        .bigPicture(notifImage)
                                )
                                .setAutoCancel(true)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(pendingIntent)
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

                            // Navigate to HomeActivity or AdminActivity based on userType
                            navigateToUserOrAdmin(email)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Login failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun saveLoginStatus(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }

    private fun navigateToUserOrAdmin(email: String) {
        val userType = getUserTypeFromEmail(email)
        val username = getUsernameFromEmail(email)

        val intent = if (userType == "admin") {
            Intent(requireContext(), HomeAdminActivity::class.java)
        } else {
            Intent(requireContext(), Navigation::class.java)
        }

        saveUsername(username)

        startActivity(intent)
        requireActivity().finish()
    }

    private fun saveUsername(username: String) {
        val editor = sharedPreferences.edit()
        editor.putString("USERNAME_KEY", username)
        editor.apply()
    }

    private fun getUsernameFromEmail(email: String): String {
        return email.substringBefore('@')
    }


    private fun getUserTypeFromEmail(email: String): String {
        return if (email.contains("admin")) {
            "admin"
        } else {
            "user"
        }
    }

}