package com.example.crm

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MyActivityFragment : Fragment() {

    lateinit var binding: FragmentMyActivityBinding
    lateinit var fromDate:String
    lateinit var toDate:String
    lateinit var from_flag:String
    lateinit var to_flag:String


    //lateinit var act_list:MutableList<MyActivitiesData>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_activity, container, false)


        from_flag = "false"
        to_flag = "false"
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


        val calender1 = Calendar.getInstance()
        val datePicker1 = DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
            calender1.set(Calendar.YEAR,year)
            calender1.set(Calendar.MONTH,month)
            calender1.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateFromDateLable(calender1)
        }

        val calender2 = Calendar.getInstance()
        val datePicker2 = DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
            calender2.set(Calendar.YEAR,year)
            calender2.set(Calendar.MONTH,month)
            calender2.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            updateToDateLable(calender2)
        }


        binding.button6.setOnClickListener {
            DatePickerDialog(requireActivity(),datePicker1,calender1.get(Calendar.YEAR),calender1.get(Calendar.MONTH),calender1.get(Calendar.DAY_OF_MONTH))
                .show()
            from_flag = "true"
        }
        binding.button7.setOnClickListener {
            DatePickerDialog(requireActivity(),datePicker2,calender2.get(Calendar.YEAR),calender2.get(Calendar.MONTH),calender2.get(Calendar.DAY_OF_MONTH))
                .show()
            to_flag = "true"
        }


        binding.button8.setOnClickListener {
            if(from_flag=="true" && to_flag=="true")
            {
                if (SharedPrefManager.getInstance(requireActivity()).user.role=="Executive")
                {
                    sortActivitiesForExecutive()
                    from_flag="false"
                    to_flag="false"
                    binding.button6.text = "From :"
                    binding.button7.text = "To :"
                }
                else if (SharedPrefManager.getInstance(requireActivity()).user.role=="Admin")
                {
                    sortActivitiesForAdmin()
                    from_flag="false"
                    to_flag="false"
                    binding.button6.text = "From :"
                    binding.button7.text = "To :"
                }

            }
            else
            {
                Toast.makeText(requireContext(),"please select both from & to date",Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root

    }

    private fun sortActivitiesForAdmin() {
        val act_lists = mutableListOf<NotificationReminderData>()
        val from = binding.button6.text
        val to = binding.button7.text
        val calender: Calendar = Calendar.getInstance()
        val stringRequest = object : StringRequest(
            Request.Method.POST,  URLs.URL_GET_ADMIN_DATE_RANGE_DATA,
            Response.Listener { response ->

                try {
                    //converting response to json object
                    val obj = JSONObject(response)
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        val array = obj.getJSONArray("activity")
//                        binding.textView40.text = array.length().toString()

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



                        for (i in (array.length()-1) downTo 0 ){
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

                            act_lists.add(banners)
                            val adapter = UpcomingActivitiesAdapter(act_lists)
                            binding.activitiesRcv.adapter=adapter

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
                    binding.textView13.visibility=View.VISIBLE
                    binding.lottieAnimationView.visibility=View.VISIBLE
                    binding.progressBar1.visibility=View.GONE

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
                val params = java.util.HashMap<String, String>()
                params["user_id"] =
                    SharedPrefManager.getInstance(requireActivity().applicationContext).user.id.toString()
                params["from"] = from.toString()
                params["to"] = to.toString()
                return params

            }
        }

        VolleySingleton.getInstance(requireActivity().applicationContext)
            .addToRequestQueue(stringRequest)
    }

    private fun sortActivitiesForExecutive() {
        val act_lists = mutableListOf<NotificationReminderData>()
        val from = binding.button6.text
        val to = binding.button7.text
        val calender: Calendar = Calendar.getInstance()
        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_GET_EXECUTIVE_DATE_RANGE_DATA,
            Response.Listener { response ->

                try {
                    //converting response to json object
                    val obj = JSONObject(response)
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        val array = obj.getJSONArray("activity")
//                        binding.textView40.text = array.length().toString()

                        if (array.length()>0)
                        {
                            binding.textView13.visibility=View.INVISIBLE
                            binding.lottieAnimationView.visibility=View.INVISIBLE
                            binding.progressBar1.visibility=View.INVISIBLE

                        }




                        for (i in (array.length()-1) downTo 0 ){
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

                            act_lists.add(banners)
                            val adapter = UpcomingActivitiesAdapter(act_lists)
                            binding.activitiesRcv.adapter=adapter

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
                    binding.textView13.visibility=View.VISIBLE
                    binding.lottieAnimationView.visibility=View.VISIBLE
                    binding.progressBar1.visibility=View.GONE
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
                val params = java.util.HashMap<String, String>()
                params["assign_to"] =
                    SharedPrefManager.getInstance(requireActivity().applicationContext).user.firstName + " " + SharedPrefManager.getInstance(
                        requireActivity().applicationContext
                    ).user.lastName
                params["from"] = from.toString()
                params["to"] = to.toString()
                return params

            }
        }

        VolleySingleton.getInstance(requireActivity().applicationContext)
            .addToRequestQueue(stringRequest)


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
                            binding.progressBar1.visibility=View.GONE

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
                                objectArtist.optString("assignTo"),
                                objectArtist.optString("status")

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
                                objectArtist.optString("assignTo"),
                                objectArtist.optString("status")
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

    private fun updateFromDateLable(calener: Calendar) {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
// Here we can use calendar selected date
        binding.button6.setText((sdf.format(calener.time)))
        fromDate=(sdf.format(calener.time))

        Log.i("oooooooooooooooooo",fromDate)


    }

    private fun updateToDateLable(calener: Calendar) {
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
// Here we can use calendar selected date
        binding.button7.setText((sdf.format(calener.time)))
        toDate=(sdf.format(calener.time))

        Log.i("oooooooooooooooooo",toDate)


    }




}






