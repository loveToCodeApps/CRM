package com.example.crm

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.example.crm.databinding.FragmentMyActivityBinding
import org.json.JSONException
import org.json.JSONObject

class MyActivityFragment : Fragment() {

    lateinit var binding: FragmentMyActivityBinding

    //lateinit var act_list:MutableList<MyActivitiesData>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_activity, container, false)

//    act_list.add(MyActivitiesData("meeting with manager","9877894570","Green park hights","22-01-2022"))
//        act_list.add(MyActivitiesData("submitting documents","8877894570","Green park hights","17-08-2022"))
//        act_list.add(MyActivitiesData("education course launching with our collegues ","7507794570","Green park hights","28-11-2022"))


//        binding.activitiesRcv.adapter = MyActivitiesAdapter(act_list)

        if(SharedPrefManager.getInstance(requireActivity().applicationContext).user.role == "Executive")
        {
            getExecutiveActivities()
        }
        else if(SharedPrefManager.getInstance(requireActivity().applicationContext).user.role == "Admin")

        {
            getAdminActivities()
        }



        return binding.root

    }

    private fun getAdminActivities() {
        val act_list = mutableListOf<MyActivitiesData>()
        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_GET_ADMIN_ACTIVITIES,
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
                            binding.lottieAnimationView.visibility=View.VISIBLE
                        }
                        else
                        {
                            binding.textView13.visibility=View.INVISIBLE
                            binding.lottieAnimationView.visibility=View.INVISIBLE

                        }


                        for (i in (array.length()-1) downTo 0 )
                         {
                            val objectArtist = array.getJSONObject(i)
                            val banners = MyActivitiesData(
                                objectArtist.optString("id"),
                                objectArtist.optString("name"),
                                objectArtist.optString("phone"),
                                objectArtist.optString("address"),
                                objectArtist.optString("state"),
                                objectArtist.optString("city"),
                                objectArtist.optString("pincode"),
                                objectArtist.optString("date"),
                                objectArtist.optString("email"),
                                objectArtist.optString("company"),
                                objectArtist.optString("assignTo")

                            )
                            act_list.add(banners)
                            val adapter = MyActivitiesAdapter(act_list)
                            binding.activitiesRcv.adapter=adapter
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
                params["id"] = SharedPrefManager.getInstance(requireActivity().applicationContext).user.id.toString().trim()
                params["assign_to"] = SharedPrefManager.getInstance(requireActivity().applicationContext).user.firstName+" "+SharedPrefManager.getInstance(requireActivity().applicationContext).user.lastName

                return params

            }
        }

        VolleySingleton.getInstance(requireActivity().applicationContext).addToRequestQueue(stringRequest)



    }



    private fun getExecutiveActivities() {
        val act_list = mutableListOf<MyActivitiesData>()
        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_GET_EXECUTIVE_ACTIVITIES,
            Response.Listener { response ->

                try {
                    //converting response to json object
                    val obj = JSONObject(response)
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        val array = obj.getJSONArray("activity")



                         if (array.length()>0)
                        {
                            binding.textView13.visibility=View.INVISIBLE
                            binding.lottieAnimationView.visibility=View.INVISIBLE
                            binding.progressBar1.visibility=View.INVISIBLE

                        }





                        for (i in (array.length()-1) downTo 0) {
                            val objectArtist = array.getJSONObject(i)
                            val banners = MyActivitiesData(
                                objectArtist.optString("id"),
                                objectArtist.optString("name"),
                                objectArtist.optString("phone"),
                                objectArtist.optString("address"),
                                objectArtist.optString("state"),
                                objectArtist.optString("city"),
                                objectArtist.optString("pincode"),
                                objectArtist.optString("date"),
                                objectArtist.optString("email"),
                                objectArtist.optString("company"),
                                objectArtist.optString("assignTo")

                            )
                            act_list.add(banners)
                            val adapter = MyActivitiesAdapter(act_list)
                            binding.activitiesRcv.adapter=adapter
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
            Response.ErrorListener { error -> Toast.makeText(requireActivity().applicationContext, error.message, Toast.LENGTH_SHORT).show() }
        ) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["id"] = SharedPrefManager.getInstance(requireActivity().applicationContext).user.id.toString().trim()
                params["assign_to"] = SharedPrefManager.getInstance(requireActivity().applicationContext).user.firstName+" "+SharedPrefManager.getInstance(requireActivity().applicationContext).user.lastName

                return params

            }
        }

        VolleySingleton.getInstance(requireActivity().applicationContext).addToRequestQueue(stringRequest)



    }


}






