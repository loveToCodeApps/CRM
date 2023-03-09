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
import com.example.crm.databinding.FragmentActivityDetailsBinding
import org.json.JSONException
import org.json.JSONObject

class ActivityDetailsFragment : Fragment() {

    lateinit var  binding : FragmentActivityDetailsBinding
    lateinit var adapter : MyActivitiesImageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_activity_details,container,false)

       val args =  ActivityDetailsFragmentArgs.fromBundle(requireArguments())

            binding.textView24.text = args.title
            binding.textView25.text = args.phone
            binding.textView26.text = args.address
            binding.textView27.text = args.date

        if (SharedPrefManager.getInstance(requireActivity().applicationContext).user.role == "Admin") {
            loadImagesForAdmin()
        }
        else if (SharedPrefManager.getInstance(requireActivity().applicationContext).user.role == "Executive")
        {
            loadImagesForExecutives()
        }
            return binding.root
    }

    private fun loadImagesForExecutives() {
        val args =  ActivityDetailsFragmentArgs.fromBundle(requireArguments())

        val acts_list = mutableListOf<MyActivitiesImageData>()

        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_GET_PICTURES_EXECUTIVES,
            Response.Listener { response ->

                try {
                    //converting response to json object
                    val obj = JSONObject(response)
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        val array = obj.getJSONArray("activity")
                        for (i in (array.length()-1) downTo 0) {
                            val objectArtist = array.getJSONObject(i)
                            val banners = MyActivitiesImageData(
                                objectArtist.optString("picture"),
                                objectArtist.optString("id"),
                                args.title,
                                args.phone,
                                args.address,
                                args.date,
                                args.actid

                            )
                            acts_list.add(banners)
                            adapter = MyActivitiesImageAdapter(acts_list)
                            binding.imagesRcv.adapter=adapter
                        }
                    } else {
                        Toast.makeText(requireActivity().applicationContext, obj.getString("message"), Toast.LENGTH_SHORT).show()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
//                    binding.textView13.visibility=View.VISIBLE
//                    binding.lottieAnimationView.visibility=View.VISIBLE
//                    binding.progressBar1.visibility=View.GONE
//                    binding.button6.visibility = View.GONE
//                    binding.button7.visibility = View.GONE
//                    binding.button8.visibility = View.GONE

                }

            },
            Response.ErrorListener { error -> Toast.makeText(requireActivity().applicationContext, error.message, Toast.LENGTH_SHORT).show() }
        ) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["assign_to"] = SharedPrefManager.getInstance(requireActivity().applicationContext).user.firstName + " " +SharedPrefManager.getInstance(requireActivity().applicationContext).user.lastName
                params["act_id"] = args.actid.toString()


                return params

            }
        }

        VolleySingleton.getInstance(requireActivity().applicationContext).addToRequestQueue(stringRequest)

    }

    private fun loadImagesForAdmin() {
        val args =  ActivityDetailsFragmentArgs.fromBundle(requireArguments())

        val acts_list = mutableListOf<MyActivitiesImageData>()

        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_GET_PICTURES_ADMIN,
            Response.Listener { response ->

                try {
                    //converting response to json object
                    val obj = JSONObject(response)
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        val array = obj.getJSONArray("activity")
                        for (i in (array.length()-1) downTo 0) {
                            val objectArtist = array.getJSONObject(i)
                            val banners = MyActivitiesImageData(
                                objectArtist.optString("picture"),
                                objectArtist.optString("id"),
                                args.title,
                                args.phone,
                                args.address,
                                args.date,
                                args.actid

                            )
                            acts_list.add(banners)
                            adapter = MyActivitiesImageAdapter(acts_list)
                            binding.imagesRcv.adapter=adapter
                        }
                    } else {
                        Toast.makeText(requireActivity().applicationContext, obj.getString("message"), Toast.LENGTH_SHORT).show()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
//                    binding.textView13.visibility=View.VISIBLE
//                    binding.lottieAnimationView.visibility=View.VISIBLE
//                    binding.progressBar1.visibility=View.GONE
//                    binding.button6.visibility = View.GONE
//                    binding.button7.visibility = View.GONE
//                    binding.button8.visibility = View.GONE

                }

            },
            Response.ErrorListener { error -> Toast.makeText(requireActivity().applicationContext, error.message, Toast.LENGTH_SHORT).show() }
        ) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["id"] = SharedPrefManager.getInstance(requireActivity().applicationContext).user.id.toString().trim()
                params["act_id"] = args.actid.toString()

                return params

            }
        }

        VolleySingleton.getInstance(requireActivity().applicationContext).addToRequestQueue(stringRequest)

    }




}