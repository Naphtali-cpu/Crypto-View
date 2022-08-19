package com.example.cryptoflow.sessions

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.cryptoflow.auth.SignIn

class LoginPref {
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var con: Context
    var PRIVATEMODE : Int = 0

    constructor(con: Context) {
        this.con = con
        pref = con.getSharedPreferences(PREF_NAME, PRIVATEMODE)
        editor = pref.edit()
    }

    companion object {
        val PREF_NAME = "Login Preference"
        val IS_LOGIN = "isLoggedini"
        val KEY_EMAIL = "email"
        val PASSWORD = "password"
    }

    fun createLoginSession(email: String, password: String) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(KEY_EMAIL, email)
        editor.putString(PASSWORD, password)
        editor.commit()
    }

    fun checkLogin() {
        if (!this.isLoggedIn()) {
            var i: Intent = Intent(con, SignIn::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            con.startActivity(i)
        }
    }



    fun getUserDetails(): HashMap<String, String> {
        var user: Map<String, String> = HashMap<String, String>()
        (user as HashMap).put(KEY_EMAIL, pref.getString(KEY_EMAIL, null)!!)
        user.put(PASSWORD, pref.getString(PASSWORD, null)!!)
        return user
    }

    fun logoutUser() {
        editor.clear()
        editor.commit()
        var i : Intent = Intent(con, SignIn::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }
}