package com.example.cryptoflow.mainui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cryptoflow.R
import com.example.cryptoflow.auth.SignIn


class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Handler().postDelayed({
            if (onBoardingFinished()) {
                val intent = Intent(context, ListActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(context, LaunchActivity::class.java)
                startActivity(intent)
            }
        }, 3000)
        val view = inflater.inflate(R.layout.fragment_splash, container, false)
        return view
    }

    private fun onBoardingFinished(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

}