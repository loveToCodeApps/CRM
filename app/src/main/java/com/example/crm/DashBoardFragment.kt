package com.example.crm

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.crm.databinding.FragmentDashBoardBinding
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit

class DashBoardFragment : Fragment() {

    lateinit var binding: FragmentDashBoardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dash_board, container, false)


        getActiviesNotificationData()



        if (SharedPrefManager.getInstance(requireActivity().applicationContext).isLoggedIn) {
            binding.textView9.text =
                SharedPrefManager.getInstance(requireActivity().applicationContext).user.firstName

            if (SharedPrefManager.getInstance(requireActivity().applicationContext).user.role == "Admin") {
              getAdminActivitiesCount()
                getAdminUpcomingActivitiesCount()
                getAdminActivitiesStatusCounts()

            } else if (SharedPrefManager.getInstance(requireActivity().applicationContext).user.role == "Executive") {
                getExecutiveActivitiesCount()
                getExecutiveActivitiesStatusCounts()

            }
        }



        binding.cl2.setOnClickListener {
            findNavController().navigate(R.id.myActivityFragment)
        }
        binding.c2.setOnClickListener {
            findNavController().navigate(R.id.upcomingActivitiesFragment)

        }

        return binding.root

    }

    private fun getAdminUpcomingActivitiesCount() {
        val calender: Calendar = Calendar.getInstance()
        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_ADMIN_UPCOMING_ACTIVITIES_COUNT,
            Response.Listener { response ->

                try {
                    //converting response to json object
                    val obj = JSONObject(response)
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        val array = obj.getJSONArray("activity")
                        binding.textView40.text = array.length().toString()



                        for (i in 0 until array.length()) {
                            val objectArtist = array.getJSONObject(i)
                            val banners = NotificationReminderData(
                                objectArtist.optString("id"),
                                objectArtist.optString("name"),
                                objectArtist.optString("date"),
                                objectArtist.optString("userid"),
                                objectArtist.optString("phone"),
                                objectArtist.optString("address"),
                                objectArtist.optString("state"),
                                objectArtist.optString("city"),
                                objectArtist.optString("pincode")

                            )
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

    private fun getAdminActivitiesStatusCounts() {
        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_ADMIN_ACTIVITIES_STATUS_COUNT,
            Response.Listener { response ->

                try {
                    //converting response to json object
                    val obj = JSONObject(response)
                    //if no error in response
                    if (!obj.getBoolean("error")) {

                        val inprogresscount = obj.getString("inprogresscount")
                        val completedcount = obj.getString("completedcount")
                        val cancelledcount = obj.getString("cancelledcount")

                        binding.textView7.text = inprogresscount
                        binding.textView14.text = completedcount
                        binding.textView19.text = cancelledcount


                    } else {
                        Toast.makeText(
                            requireActivity().applicationContext,
                            obj.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
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
                params["id"] =
                    SharedPrefManager.getInstance(requireActivity().applicationContext).user.id.toString()
                return params

            }
        }

        VolleySingleton.getInstance(requireActivity().applicationContext)
            .addToRequestQueue(stringRequest)

    }

    private fun getExecutiveActivitiesStatusCounts() {
        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_EXECUTIVE_ACTIVITIES_STATUS_COUNT,
            Response.Listener { response ->

                try {
                    //converting response to json object
                    val obj = JSONObject(response)
                    //if no error in response
                    if (!obj.getBoolean("error")) {

                        val inprogresscount = obj.getString("inprogresscount")
                        val completedcount = obj.getString("completedcount")
                        val cancelledcount = obj.getString("cancelledcount")

                        binding.textView7.text = inprogresscount
                        binding.textView14.text = completedcount
                        binding.textView19.text = cancelledcount


                    } else {
                        Toast.makeText(
                            requireActivity().applicationContext,
                            obj.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
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
                params["assignTo"] =
                    (SharedPrefManager.getInstance(requireActivity().applicationContext).user.firstName + " " + SharedPrefManager.getInstance(
                        requireActivity().applicationContext
                    ).user.lastName)
                return params

            }
        }

        VolleySingleton.getInstance(requireActivity().applicationContext)
            .addToRequestQueue(stringRequest)
    }

    //--------------------------------------------------------------------------------------------------
    private fun getActiviesNotificationData() {
        val calender: Calendar = Calendar.getInstance()
        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_ACTIVITIES_REMINDER_NOTIFICATION_DATA,
            Response.Listener { response ->

                try {
                    //converting response to json object
                    val obj = JSONObject(response)
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        val array = obj.getJSONArray("activity")
                        binding.textView40.text = array.length().toString()



                        for (i in 0 until array.length()) {
                            val objectArtist = array.getJSONObject(i)
                            val banners = NotificationReminderData(
                                objectArtist.optString("id"),
                                objectArtist.optString("name"),
                                objectArtist.optString("date"),
                                objectArtist.optString("userid"),
                                objectArtist.optString("phone"),
                                objectArtist.optString("address"),
                                objectArtist.optString("state"),
                                objectArtist.optString("city"),
                                objectArtist.optString("pincode")

                            )
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

    private fun addNotification(msg1: String, msg2: String, notifyId: Int) {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(requireContext())
            .setSmallIcon(R.drawable.tps_logo) //set icon for notification
            .setContentTitle(msg1) //set title of notification
            .setContentText(msg2)//this is notification message
            .setAutoCancel(true) // makes auto cancel of notification
            .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification


//        Intent notificationIntent = new Intent(this, NotificationView.class);
//        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        //notification message will get at NotificationView
//        notificationIntent.putExtra("message", "This is a notification message");

//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);

        // Add as notification
        val manager: NotificationManager =
            (requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)!!
//            (getSystemService(Context.NOTIFICATION_SERVICE)) as NotificationManager
        manager.notify(notifyId, builder.build());
    }


    //------------------------------------------------------------------------------------------------
    private fun getExecutiveActivitiesCount() {
        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_EXECUTIVE_ACTIVITIES_COUNT,
            Response.Listener { response ->

                try {
                    //converting response to json object
                    val obj = JSONObject(response)
                    //if no error in response
                    if (!obj.getBoolean("error")) {

                        val count = obj.getString("count")
                        binding.textView5.text = count

                    } else {
                        Toast.makeText(
                            requireActivity().applicationContext,
                            obj.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
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
                    (SharedPrefManager.getInstance(requireActivity().applicationContext).user.firstName + " " + SharedPrefManager.getInstance(
                        requireActivity().applicationContext
                    ).user.lastName)
                return params

            }
        }

        VolleySingleton.getInstance(requireActivity().applicationContext)
            .addToRequestQueue(stringRequest)
    }

    private fun getAdminActivitiesCount() {
        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_ADMIN_ACTIVITIES_COUNT,
            Response.Listener { response ->

                try {
                    //converting response to json object
                    val obj = JSONObject(response)
                    //if no error in response
                    if (!obj.getBoolean("error")) {

                        val count = obj.getString("count")
                        binding.textView5.text = count

                    } else {
                        Toast.makeText(
                            requireActivity().applicationContext,
                            obj.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
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
                params["id"] =
                    SharedPrefManager.getInstance(requireActivity().applicationContext).user.id.toString()
                        .trim()
                return params

            }
        }

        VolleySingleton.getInstance(requireActivity().applicationContext)
            .addToRequestQueue(stringRequest)


    }


}


