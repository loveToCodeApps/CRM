package com.example.crm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.crm.databinding.FragmentAddedUsersBinding
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AddedUsersFragment : Fragment() {

  lateinit var binding:FragmentAddedUsersBinding
    lateinit var adapter : AddedUsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
   binding = DataBindingUtil.inflate(inflater,R.layout.fragment_added_users,container,false)

        // Get All the users
        val acts_list = mutableListOf<AddedUsersData>()
        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_GET_ADDED_USERS,
            Response.Listener { response ->

                try {
                    //converting response to json object
                    val obj = JSONObject(response)
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        val array = obj.getJSONArray("activity")

                        if(array.length()<=0)
                        {
                            binding.textView13.visibility=View.VISIBLE
                            binding.textView13.text="You don't have any Users added!!"
                            binding.lottieAnimationView.visibility=View.VISIBLE
                        }
                        else
                        {
                            binding.textView13.visibility=View.INVISIBLE
                            binding.lottieAnimationView.visibility=View.INVISIBLE
                            binding.progressBar1.visibility=View.GONE

                        }


                        for (i in (array.length()-1) downTo 0 )
                        {

                            val objectArtist = array.getJSONObject(i)

                            val banners = AddedUsersData(
                                objectArtist.optString("fname"),
                                objectArtist.optString("lname"),
                                objectArtist.optString("email"),
                                objectArtist.optString("phone"),
                                objectArtist.optString("role")
                            )
                            acts_list.add(banners)
                            adapter = AddedUsersAdapter(acts_list)
                            binding.addedUsersRcv.adapter=adapter
                        }
                    } else {
                        Toast.makeText(requireActivity().applicationContext, obj.getString("message"), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    binding.textView13.visibility=View.VISIBLE
                    binding.lottieAnimationView.visibility=View.VISIBLE
                    binding.progressBar1.visibility=View.GONE


                }
            },
            Response.ErrorListener { error -> Toast.makeText(requireActivity().applicationContext, error.message, Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["added_by"] = SharedPrefManager.getInstance(requireActivity().applicationContext).user.firstName + " " + SharedPrefManager.getInstance(requireActivity().applicationContext).user.lastName

                return params

            }
        }

        VolleySingleton.getInstance(requireActivity().applicationContext).addToRequestQueue(stringRequest)

        return  binding.root
    }


}