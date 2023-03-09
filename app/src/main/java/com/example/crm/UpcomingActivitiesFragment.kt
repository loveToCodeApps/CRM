package com.example.crm

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.crm.databinding.FragmentUpcomingActivitiesBinding
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class UpcomingActivitiesFragment : Fragment()
{
lateinit var binding:FragmentUpcomingActivitiesBinding
    lateinit var fromDate:String
    lateinit var toDate:String
    lateinit var from_flag:String
    lateinit var to_flag:String
    var searchList =  arrayListOf<EditUpcomingActivitiesData>()
    lateinit var adapter : UpcomingActivitiesAdapter
    var context = "R.id.UpcomingActivitiesFragment"



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_upcoming_activities,
            container,
            false
        )




        if (SharedPrefManager.getInstance(requireActivity()).isLoggedIn)
        {
            if (SharedPrefManager.getInstance(requireActivity()).user.role=="Executive")
            {
                getExecutiveActiviesNotificationData()

            }
            else if (SharedPrefManager.getInstance(requireActivity()).user.role=="Admin")
            {
                getAdminActiviesNotificationData()
            }

        }

        setHasOptionsMenu(true)
        return binding.root

    }







    private fun getAdminActiviesNotificationData() {
        val act_lists = mutableListOf<EditUpcomingActivitiesData>()

        val calender: Calendar = Calendar.getInstance()
        val stringRequest = object : StringRequest(
            Request.Method.POST,  URLs.URL_ADMIN_UPCOMING_ACTIVITIES_COUNT,
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
                            binding.progressBar1.visibility=View.INVISIBLE


                        }



                        for (i in (array.length()-1) downTo 0 ){
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
                            searchList = act_lists as ArrayList<EditUpcomingActivitiesData>
                             adapter = UpcomingActivitiesAdapter(act_lists,context)
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
                val params = HashMap<String, String>()
                params["user_id"] =
                    SharedPrefManager.getInstance(requireActivity().applicationContext).user.id.toString()

                return params

            }
        }

        VolleySingleton.getInstance(requireActivity().applicationContext)
            .addToRequestQueue(stringRequest)
    }


    private fun getExecutiveActiviesNotificationData() {
        val act_lists = mutableListOf<EditUpcomingActivitiesData>()
        var first = " "
        var last = " "

        val calender: Calendar = Calendar.getInstance()
        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_EXECUTIVE_UPCOMING_ACTIVITIES_COUNT,
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
                            searchList = act_lists as ArrayList<EditUpcomingActivitiesData>
                             adapter = UpcomingActivitiesAdapter(act_lists,context)
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
                if (SharedPrefManager.getInstance(requireActivity().applicationContext).isLoggedIn)
                {
                    first=SharedPrefManager.getInstance(requireActivity().applicationContext).user.firstName
                    last=   SharedPrefManager.getInstance(requireActivity().applicationContext).user.lastName
                }
                val params = HashMap<String, String>()
                params["assign_to"] =
                    first+" "+last

                return params

            }
        }

        VolleySingleton.getInstance(requireActivity().applicationContext)
            .addToRequestQueue(stringRequest)


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.reset_upcoming_date_data, menu)
        val item = menu.findItem(R.id.search)
        val searchView = item.actionView as SearchView
        searchView.queryHint="Search  activities"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                activitiesFilter(newText)
                return true
            }
        })


    }

    private fun activitiesFilter(newText: String?) {
            Log.i("@@@@@@@@@@@@@@@@@@@@","$newText")
        var newFilteredList = arrayListOf<EditUpcomingActivitiesData>()

        for (i in searchList)
        {
            if (i.name.contains(newText!!,true) || i.company.contains(newText!!,true) || i.phone.contains(newText!!,true)|| i.address.contains(newText!!,true) || i.date.contains(newText!!,true)|| i.state.contains(newText!!,true)|| i.city.contains(newText!!,true)|| i.pincode.contains(newText!!,true)){

                newFilteredList.add(i)
            }
        }
adapter.filtering(newFilteredList)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}
