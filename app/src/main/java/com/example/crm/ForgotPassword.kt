package com.example.crm

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.crm.databinding.ActivityForgotPasswordBinding
import org.json.JSONException
import org.json.JSONObject

class ForgotPassword : AppCompatActivity() {
    lateinit var binding: ActivityForgotPasswordBinding
     var password:String=" "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        supportActionBar?.hide()

        binding.login.setOnClickListener {
            matchDetails()
        }
        binding.login2.setOnClickListener {
            sendPassword()
            Toast.makeText(this,"Password sent to your email successfully",Toast.LENGTH_SHORT).show()
        }

        binding.regis.setOnClickListener {
            finish()
            startActivity(Intent(this, Login::class.java))
        }


    }

    private fun sendPassword() {

        //if everything is fine
        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_SEND_PASSWORD,
            Response.Listener { response ->

                try {
                    //converting response to json object
                    val obj = JSONObject(response)

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(
                            applicationContext,
                            obj.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                        //getting the user from the response
                        val userJson = obj.getJSONObject("user")

                        //creating a new user object


                        //storing the user in shared preferences

//                        finish()
//                        startActivity(Intent(applicationContext, MainActivity::class.java))
                    } else {
                        Toast.makeText(
                            applicationContext,
                            obj.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()

                params["password"] = password
                params["tos"] = binding.other.text.toString()
                return params
            }
        }

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }

    private fun matchDetails() {
        val email = binding.other.text.toString()
        val phone = binding.email.text.toString()

//            if (TextUtils.isEmpty(email)) {
//                binding.email.error = "Please enter your email id"
//                binding.email.requestFocus()
//            }
        if (TextUtils.isEmpty(phone)) {
            binding.email.error = "Phone can't be empty"
            binding.email.requestFocus()
        }

        if (TextUtils.isEmpty(email)) {
            binding.other.error = "Email can't be empty"
            binding.other.requestFocus()
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.other.error = "Enter a valid email"
            binding.other.requestFocus()
        }




        //if everything is fine
        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_FORGOT_PASSWORD,
            Response.Listener { response ->

                try {
                    //converting response to json object
                    val obj = JSONObject(response)

                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(
                            applicationContext,
                            obj.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()

                        if (obj.getString("message") == "Details Matched , Congratulations!!")
                        {
                            binding.login2.visibility = View.VISIBLE
                           Toast.makeText(this,"$password",Toast.LENGTH_SHORT).show()
                        }



                        //getting the user from the response
                        val userJson = obj.getJSONObject("user")


                        password = userJson.optString("password")

                        //creating a new user object


                        //storing the user in shared preferences

//                        finish()
//                        startActivity(Intent(applicationContext, MainActivity::class.java))
                    } else {
                        Toast.makeText(
                            applicationContext,
                            obj.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()

                params["email"] = email
                params["phone"] = phone
                return params
            }
        }

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }
}

