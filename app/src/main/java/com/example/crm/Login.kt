package com.example.crm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.crm.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject

class Login : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        supportActionBar?.hide()

        // check if already logged in or not
        if (SharedPrefManager.getInstance(this).isLoggedIn) {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
            return
        }


        binding.regis.setOnClickListener {
            finish()
            startActivity(Intent(this, Registration::class.java))
        }

        binding.regis2.setOnClickListener {
            finish()
            startActivity(Intent(this, ForgotPassword::class.java))
        }



        //login button
        binding.login.setOnClickListener {
            userLogin()
        }



    }

    private fun userLogin() {

            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

//            if (TextUtils.isEmpty(email)) {
//                binding.email.error = "Please enter your email id"
//                binding.email.requestFocus()
//            }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.email.error = "Enter a valid email"
            binding.email.requestFocus()

        }
        if (TextUtils.isEmpty(password)) {
            binding.password.error = "Please enter your password"
            binding.password.requestFocus()
        }

            //if everything is fine
            val stringRequest = object : StringRequest(
                Request.Method.POST, URLs.URL_LOGIN,
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

//                            Snackbar.make(
//                                this.findViewById(android.R.id.content),
//                                obj.getString("message"),
//                                Snackbar.LENGTH_SHORT // How long to display the message.
//                            ).show()
//





                            //getting the user from the response
                            val userJson = obj.getJSONObject("user")

                            //creating a new user object
                            val user = User(
                                userJson.getInt("id"),
                                userJson.getString("firstname"),
                                userJson.getString("lastname"),
                                userJson.getString("email"),
                                userJson.getString("phone"),
                                userJson.getString("address"),
                                userJson.getString("state"),
                                userJson.getString("city"),
                                userJson.getString("pincode"),
                                userJson.getString("role")


                            )

                            //storing the user in shared preferences
                            SharedPrefManager.getInstance(applicationContext).userLogin(user)
                            //starting the MainActivity
                            finish()
                            startActivity(Intent(applicationContext, MainActivity::class.java))
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
                    params["password"] = password
                    return params
                }
            }

            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)

        }




}











