package com.example.crm

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.crm.databinding.FragmentAddUserBinding
import org.json.JSONException
import org.json.JSONObject

class AddUserFragment : Fragment() {

lateinit var binding : FragmentAddUserBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
 binding  = DataBindingUtil.inflate(inflater,R.layout.fragment_add_user,container,false)


val roles = resources.getStringArray(R.array.roles)
val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_layout,roles)


 // Here don't use binding.autocompletetv.adapter = adapter
        //it will give u error , instead use setAdapter()
    binding.editTextTextPersonName10.setAdapter(arrayAdapter)


        binding.button2.setOnClickListener {
            addUser()
        }



        return  binding.root



    }

    private fun addUser() {
        val firstname = binding.editTextTextPersonName.text!!.trim()
        val lastname = binding.editTextTextPersonName2.text!!.trim()
        val email = binding.editTextTextPersonName4.text!!.trim()
        val phone = binding.editTextTextPersonName3.text!!.trim()
        val password = binding.editTextTextPersonName5.text!!.trim()
        val address = binding.editTextTextPersonName6.text!!.trim()
        val state = binding.editTextTextPersonName7.text!!.trim()
        val city = binding.editTextTextPersonName8.text!!.trim()
        val pincode = binding.editTextTextPersonName9.text!!.trim()
        val role = binding.editTextTextPersonName10.text!!.trim()


        if (TextUtils.isEmpty(firstname)) {
            binding.editTextTextPersonName.error = "Please enter your first name"
            binding.editTextTextPersonName.requestFocus()
            return
        }


        if (TextUtils.isEmpty(lastname)) {
            binding.editTextTextPersonName2.error = "Please enter your last name"
            binding.editTextTextPersonName2.requestFocus()
            return
        }
        if (TextUtils.isEmpty(email)) {
            binding.editTextTextPersonName4.error = "Please enter your email"
            binding.editTextTextPersonName4.requestFocus()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editTextTextPersonName4.error = "Enter a valid email"
            binding.editTextTextPersonName4.requestFocus()
            return
        }

        if (TextUtils.isEmpty(phone)) {
            binding.editTextTextPersonName3.error = "Please enter phone number"
            binding.editTextTextPersonName3.requestFocus()
            return
        }
        if (TextUtils.isEmpty(password)) {
            binding.editTextTextPersonName5.error = "Password can't be empty"
            binding.editTextTextPersonName5.requestFocus()
            return
        }


        if (TextUtils.isEmpty(role)) {
            binding.editTextTextPersonName10.error = "Please choose role"
            binding.editTextTextPersonName10.requestFocus()
            return
        }
        if (TextUtils.getTrimmedLength(phone) > 10 || TextUtils.getTrimmedLength(phone) < 10) {
            binding.editTextTextPersonName3.error = "enter 10 digit phone number"
            binding.editTextTextPersonName3.requestFocus()
            return
        }

        if (TextUtils.isEmpty(address)) {
            binding.editTextTextPersonName6.error = "Please enter your address"
            binding.editTextTextPersonName6.requestFocus()
            return
        }

        if (TextUtils.isEmpty(state)) {
            binding.editTextTextPersonName7.error = "Please enter your state name"
            binding.editTextTextPersonName7.requestFocus()
            return

        }

        if (TextUtils.isEmpty(city)) {
            binding.editTextTextPersonName8.error = "Please enter your city name"
            binding.editTextTextPersonName8.requestFocus()
            return
        }
        if (TextUtils.isEmpty(pincode)) {
            binding.editTextTextPersonName9.error = "Please enter your pincode"
            binding.editTextTextPersonName9.requestFocus()
            return
        }

        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_ADD_USER,
            Response.Listener { response ->

                try {
                    //converting response to json object
                    val obj = JSONObject(response)
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(requireActivity().applicationContext, obj.getString("message"), Toast.LENGTH_SHORT).show()
//
//                        //getting the user from the response
//                        val userJson = obj.getJSONObject("user")
//
//                        //creating a new user object
//                        val user = NewUser(
//                            userJson.getInt("id"),
//                            userJson.getString("firstname"),
//                            userJson.getString("lastname"),
//                            userJson.getString("email"),
//                            userJson.getString("phone"),
//                            userJson.getString("address"),
//                            userJson.getString("state"),
//                            userJson.getString("city"),
//                            userJson.getString("pincode"),
//                            userJson.getString("role")
//                        )
                        findNavController().navigate(R.id.dashBoard)

                    } else {
                        Toast.makeText(requireActivity().applicationContext, obj.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error -> Toast.makeText(requireActivity().applicationContext, error.message, Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["firstname"] = firstname.toString()
                params["lastname"] = lastname.toString()
                params["email"] = email.toString()
                params["phone"] = phone.toString()
                params["password"] = password.toString()
                params["address"] = address.toString()
                params["state"] = state.toString()
                params["city"] = city.toString()
                params["pincode"] = pincode.toString()
                params["role"] = role.toString()

                return params
            }
        }

        VolleySingleton.getInstance(requireActivity().applicationContext).addToRequestQueue(stringRequest)




    }


}