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
import com.example.crm.databinding.FragmentHistoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONException
import org.json.JSONObject

class HistoryFragment : BottomSheetDialogFragment() {

lateinit var binding:FragmentHistoryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
 binding = DataBindingUtil.inflate(inflater,R.layout.fragment_history,container,false)

if (SharedPrefManager.getInstance(requireActivity().applicationContext).user.role=="Admin")
{
    getAdminHistory()
}
else if (SharedPrefManager.getInstance(requireActivity().applicationContext).user.role=="Executive")
        {
   getExecutiveHistory()
        }



        return binding.root

    }

    private fun getExecutiveHistory() {
        val act_list = mutableListOf<ExecutiveHistoryData>()
       val args = HistoryFragmentArgs.fromBundle(requireArguments())
       val activity_id= args.activityId

        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_GET_EXECUTIVE_HISTORY,
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
                            binding.progressBar1.visibility=View.GONE

                        }


                        for (i in (array.length()-1) downTo 0 )
                        {
                            val objectArtist = array.getJSONObject(i)
                            val banners = ExecutiveHistoryData(
                                objectArtist.optString("id"),
                                objectArtist.optString("name"),
                                objectArtist.optString("date"),
                                objectArtist.optString("email"),
                                objectArtist.optString("phone"),
                                objectArtist.optString("address"),
                                objectArtist.optString("state"),
                                objectArtist.optString("city"),
                                objectArtist.optString("pincode")


                            )
                            act_list.add(banners)
                            val adapter = ExecutiveHistoryAdapter(act_list)
                            binding.historyRcv.adapter=adapter
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
                params["act_id"]= activity_id

                return params

            }
        }

        VolleySingleton.getInstance(requireActivity().applicationContext).addToRequestQueue(stringRequest)

    }


    private fun getAdminHistory() {
        val act_list = mutableListOf<AdminHistoryData>()
        val args = HistoryFragmentArgs.fromBundle(requireArguments())
        val activity_id= args.activityId
        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_GET_ADMIN_HISTORY,
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
                            binding.progressBar1.visibility=View.GONE

                        }


                        for (i in (array.length()-1) downTo 0 )
                        {
                            val objectArtist = array.getJSONObject(i)
                            val banners = AdminHistoryData(
                                objectArtist.optString("id"),
                                objectArtist.optString("name"),
                                objectArtist.optString("date"),
                                objectArtist.optString("email"),
                                objectArtist.optString("phone"),
                                objectArtist.optString("address"),
                                objectArtist.optString("state"),
                                objectArtist.optString("city"),
                                objectArtist.optString("pincode")

                            )
                            act_list.add(banners)
                            val adapter = AdminHistoryAdapter(act_list)
                            binding.historyRcv.adapter=adapter
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
                params["act_id"]= activity_id

                return params

            }
        }

        VolleySingleton.getInstance(requireActivity().applicationContext).addToRequestQueue(stringRequest)

    }


}


