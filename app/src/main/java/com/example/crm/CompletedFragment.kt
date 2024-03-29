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
import com.example.crm.databinding.FragmentCompletedBinding
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CompletedFragment : Fragment() {

    lateinit var binding: FragmentCompletedBinding
    lateinit var adapter: UpcomingActivitiesAdapter
    var context = "R.id.CompletedFragment"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_completed, container, false)
        if (SharedPrefManager.getInstance(requireActivity()).isLoggedIn) {
            if (SharedPrefManager.getInstance(requireActivity()).user.role == "Executive") {
                getExecutiveCompletedActivities()

            } else if (SharedPrefManager.getInstance(requireActivity()).user.role == "Admin") {
                getAdminCompletedActivities()
            }

        }




        return binding.root
    }

    private fun getAdminCompletedActivities() {
        val act_lists = mutableListOf<EditUpcomingActivitiesData>()

        val calender: Calendar = Calendar.getInstance()
        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_ACTIVITIES_ADMIN_COMPLETED,
            Response.Listener { response ->

                try {
                    //converting response to json object
                    val obj = JSONObject(response)
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        val array = obj.getJSONArray("activity")
//                        binding.textView40.text = array.length().toString()

                        if (array.length() <= 0) {
                            binding.textView13.visibility = View.VISIBLE
                            binding.lottieAnimationView.visibility = View.VISIBLE
                        } else {
                            binding.textView13.visibility = View.INVISIBLE
                            binding.lottieAnimationView.visibility = View.INVISIBLE
                            binding.progressBar1.visibility = View.INVISIBLE


                        }



                        for (i in (array.length() - 1) downTo 0) {
                            val objectArtist = array.getJSONObject(i)

                            val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                            val outputFormat: DateFormat = SimpleDateFormat("dd-MMM-yyyy")
                            val inputDateStr = objectArtist.optString("date")
                            val date: Date = inputFormat.parse(inputDateStr)
                            val outputDateStr: String = outputFormat.format(date)

                            val banners = EditUpcomingActivitiesData(
                                objectArtist.optString("id"),
                                objectArtist.optString("name"),
                                outputDateStr,
                                objectArtist.optString("userid"),
                                objectArtist.optString("phone"),
                                objectArtist.optString("address"),
                                objectArtist.optString("state"),
                                objectArtist.optString("city"),
                                objectArtist.optString("pincode"),
                                objectArtist.optString("status"),
                                objectArtist.optString("email"),
                                objectArtist.optString("company"),
                                objectArtist.optString("assign_to")
                            )

                            act_lists.add(banners)
                            adapter = UpcomingActivitiesAdapter(act_lists,context)
                            binding.activitiesRcv.adapter = adapter

//                            calender.set(Calendar.HOUR_OF_DAY,9)
//                            calender.set(Calendar.MINUTE,0)
//                            calender.set(Calendar.SECOND,0)
//
//                            val timer : Timer = Timer()
//                            timer.schedule(addNotification(banners.name.toString(),banners.date.toString(),i),
//                            calender.time,TimeUnit.MILLISECONDS.convert(1,TimeUnit.DAYS))


//                            act_list.add(banners)
//                            val adapter = MyActivitiesAdapter(act_list)
//                            binding.activitiesRcv.adapter=adapter
                        }
                    } else {
                        Toast.makeText(
                            requireActivity().applicationContext,
                            obj.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    binding.textView13.visibility = View.VISIBLE
                    binding.textView13.text = "You don't have any Completed activities yet!!"
                    binding.lottieAnimationView.visibility = View.VISIBLE
                    binding.progressBar1.visibility = View.GONE

                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    requireActivity().applicationContext,
                    error.message,
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["user_id"] =
                    SharedPrefManager.getInstance(requireActivity().applicationContext).user.id.toString()

                return params

            }
        }

        VolleySingleton.getInstance(requireActivity().applicationContext)
            .addToRequestQueue(stringRequest)
    }

    private fun getExecutiveCompletedActivities() {
        val act_lists = mutableListOf<EditUpcomingActivitiesData>()

        val calender: Calendar = Calendar.getInstance()
        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_ACTIVITIES_EXECUTIVE_COMPLETED,
            Response.Listener { response ->

                try {
                    //converting response to json object
                    val obj = JSONObject(response)
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        val array = obj.getJSONArray("activity")
//                        binding.textView40.text = array.length().toString()

                        if (array.length() > 0) {
                            binding.textView13.visibility = View.INVISIBLE
                            binding.lottieAnimationView.visibility = View.INVISIBLE
                            binding.progressBar1.visibility = View.INVISIBLE

                        }




                        for (i in (array.length() - 1) downTo 0) {
                            val objectArtist = array.getJSONObject(i)

                            val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                            val outputFormat: DateFormat = SimpleDateFormat("dd-MMM-yyyy")
                            val inputDateStr = objectArtist.optString("date")
                            val date: Date = inputFormat.parse(inputDateStr)
                            val outputDateStr: String = outputFormat.format(date)

                            val banners = EditUpcomingActivitiesData(
                                objectArtist.optString("id"),
                                objectArtist.optString("name"),
                                outputDateStr,
                                objectArtist.optString("userid"),
                                objectArtist.optString("phone"),
                                objectArtist.optString("address"),
                                objectArtist.optString("state"),
                                objectArtist.optString("city"),
                                objectArtist.optString("pincode"),
                                objectArtist.optString("status"),
                                objectArtist.optString("email"),
                                objectArtist.optString("company"),
                                objectArtist.optString("assign_to")

                            )

                            act_lists.add(banners)
                            adapter = UpcomingActivitiesAdapter(act_lists,context)
                            binding.activitiesRcv.adapter = adapter

//                            calender.set(Calendar.HOUR_OF_DAY,9)
//                            calender.set(Calendar.MINUTE,0)
//                            calender.set(Calendar.SECOND,0)
//
//                            val timer : Timer = Timer()
//                            timer.schedule(addNotification(banners.name.toString(),banners.date.toString(),i),
//                            calender.time,TimeUnit.MILLISECONDS.convert(1,TimeUnit.DAYS))


//                            act_list.add(banners)
//                            val adapter = MyActivitiesAdapter(act_list)
//                            binding.activitiesRcv.adapter=adapter
                        }
                    } else {
                        Toast.makeText(
                            requireActivity().applicationContext,
                            obj.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    binding.textView13.visibility = View.VISIBLE
                    binding.textView13.text = "You don't have any Completed activities yet!!"
                    binding.lottieAnimationView.visibility = View.VISIBLE
                    binding.progressBar1.visibility = View.GONE
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    requireActivity().applicationContext,
                    error.message,
                    Toast.LENGTH_SHORT
                ).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["assign_to"] =
                    SharedPrefManager.getInstance(requireActivity().applicationContext).user.firstName + " " + SharedPrefManager.getInstance(
                        requireActivity().applicationContext
                    ).user.lastName

                return params

            }
        }

        VolleySingleton.getInstance(requireActivity().applicationContext)
            .addToRequestQueue(stringRequest)
    }


}