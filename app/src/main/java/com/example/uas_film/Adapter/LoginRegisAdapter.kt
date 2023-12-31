package com.example.uas_film.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.uas_film.Fragment.LoginFragment
import com.example.uas_film.Fragment.RegisterrFragment

class LoginRegisAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LoginFragment()
            1 -> RegisterrFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}