package com.example.crm

import android.content.Context
import android.content.Intent
import com.example.crm.Login

class SharedPrefManager private constructor(context: Context) {

    //this method will checker whether user is already logged in or not
    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = ctx?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences?.getString(KEY_EMAIL, null) != null
        }

    //this method will give the logged in user
    val user: User
        get() {
            val sharedPreferences = ctx?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return User(
                sharedPreferences!!.getInt(KEY_ID, -1),
                sharedPreferences.getString(KEY_FIRST_NAME, null)!!,
                sharedPreferences.getString(KEY_LAST_NAME, null)!!,
                sharedPreferences.getString(KEY_EMAIL, null)!!,
                sharedPreferences.getString(KEY_PHONE, null)!!,
                sharedPreferences.getString(KEY_ADDRESS, null)!!,
                sharedPreferences.getString(KEY_STATE, null)!!,
                sharedPreferences.getString(KEY_CITY, null)!!,
                sharedPreferences.getString(KEY_PIN_CODE, null)!!,
                sharedPreferences.getString(KEY_ROLE, null)!!

            )
        }

    init {
        ctx = context
    }

    //this method will store the user data in shared preferences
    fun userLogin(user: User) {
        val sharedPreferences = ctx?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putInt(KEY_ID, user.id)
        editor?.putString(KEY_FIRST_NAME, user.firstName)
        editor?.putString(KEY_LAST_NAME, user.lastName)
        editor?.putString(KEY_EMAIL, user.email)
        editor?.putString(KEY_PHONE, user.phone)
        editor?.putString(KEY_ADDRESS, user.address)
        editor?.putString(KEY_STATE, user.state)
        editor?.putString(KEY_CITY, user.city)
        editor?.putString(KEY_PIN_CODE, user.pincode)
        editor?.putString(KEY_ROLE, user.role)

        editor?.apply()
    }

    //this method will logout the user
    fun logout() {
        val sharedPreferences = ctx?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.clear()
        editor?.apply()
        val intent = Intent(ctx,Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ctx?.startActivity(intent)
    }

    companion object {

        private val SHARED_PREF_NAME = "volleyregisterlogin"
        private val KEY_EMAIL = "keyemail"
        private val KEY_FIRST_NAME = "keyfirstname"
        private val KEY_LAST_NAME = "keylastname"
        private val KEY_PHONE = "keyphone"
        private val KEY_ADDRESS = "keyaddress"
        private val KEY_STATE = "keystate"
        private val KEY_CITY = "keycity"
        private val KEY_PIN_CODE= "keypincode"
        private val KEY_ROLE= "keyrole"
        private val KEY_ID = "keyid"
        private var mInstance: SharedPrefManager? = null
        private var ctx: Context? = null
        @Synchronized
        fun getInstance(context: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(context)
            }
            return mInstance as SharedPrefManager
        }
    }
}




