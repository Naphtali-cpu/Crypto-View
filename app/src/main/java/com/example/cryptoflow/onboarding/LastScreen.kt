package com.example.cryptoflow.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cryptoflow.R
import com.example.cryptoflow.auth.SignIn
import com.example.cryptoflow.auth.SignUp
import kotlinx.android.synthetic.main.fragment_last_screen.view.*


class LastScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_last_screen, container, false)

        view.getStartedBtn.setOnClickListener {
            val intent = Intent(context, SignIn::class.java)
            startActivity(intent)
            onBoardingFinished()
        }

        return view
    }

    private fun onBoardingFinished() {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }

}