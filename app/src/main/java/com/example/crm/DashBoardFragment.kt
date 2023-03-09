package com.example.crm


import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.appsearch.SetSchemaRequest.READ_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.SharedElementCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.crm.databinding.FragmentDashBoardBinding
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class DashBoardFragment : Fragment() {

    lateinit var binding: FragmentDashBoardBinding

    //upload video variables--------------------
    private val SELECT_VIDEO = 3
    private var selectedPath: String? = null
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1

    private var images:ArrayList<String>?=null
    private val PICK_IMAGES_CODE = 0
    lateinit var selectedPicture: String
    var count = 0


    //------------------------------------------


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dash_board, container, false)
        if (checkConnection(requireContext())) {
            binding.animationView2.visibility = View.GONE
            binding.c1.visibility = View.VISIBLE
            binding.c2.visibility = View.VISIBLE
            binding.c5.visibility = View.VISIBLE
            binding.c10.visibility = View.VISIBLE
            binding.c11.visibility = View.VISIBLE
            binding.view.visibility = View.VISIBLE
            binding.textView21.visibility = View.GONE
        } else {
            Toast.makeText(requireContext(), "Bad Connection", Toast.LENGTH_SHORT).show()
            binding.animationView2.visibility = View.VISIBLE
            binding.c1.visibility = View.GONE
            binding.c2.visibility = View.GONE
            binding.c5.visibility = View.GONE
            binding.c10.visibility = View.GONE
            binding.c11.visibility = View.GONE
            binding.view.visibility = View.GONE
            binding.textView21.visibility = View.VISIBLE
        }



        getActiviesNotificationData()


        binding.floatingActionButton42.setOnClickListener {
            it.findNavController().navigate(DashBoardFragmentDirections.actionDashBoardToActivities())
        }




        if (SharedPrefManager.getInstance(requireActivity().applicationContext).isLoggedIn) {
            binding.textView9.text =
                SharedPrefManager.getInstance(requireActivity().applicationContext).user.firstName

            if (SharedPrefManager.getInstance(requireActivity().applicationContext).user.role == "Admin") {

                getAdminActivitiesCount()
                getAdminUpcomingActivitiesCount()
                getAdminActivitiesStatusCounts()

            } else if (SharedPrefManager.getInstance(requireActivity().applicationContext).user.role == "Executive") {
                getExecutiveActivitiesCount()
                getExecutiveUpcomingActivitiesCount()
                getExecutiveActivitiesStatusCounts()

            }
        }



        binding.cl2.setOnClickListener {
            findNavController().navigate(DashBoardFragmentDirections.actionDashBoardToMyActivityFragment())
        }
        binding.c2.setOnClickListener {
            findNavController().navigate(DashBoardFragmentDirections.actionDashBoardToUpcomingActivitiesFragment())

        }
        binding.c5.setOnClickListener {
            findNavController().navigate(DashBoardFragmentDirections.actionDashBoardToInprogressFragment())
        }

        binding.c10.setOnClickListener {
            findNavController().navigate(DashBoardFragmentDirections.actionDashBoardToCompletedFragment())

        }
        binding.c11.setOnClickListener {
            findNavController().navigate(DashBoardFragmentDirections.actionDashBoardToCancelledFragment())

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
                                objectArtist.optString("pincode"),
                                objectArtist.optString("status"),
                                objectArtist.optString("email"),
                                objectArtist.optString("company"),
                                objectArtist.optString("assign_to")


                            )
//                            calender.set(Calendar.HOUR_OF_DAY,9)
//                            calender.set(Calendar.MINUTE,0)
//                            calender.set(Calendar.SECOND,0)
//
//                            val timer : Timer = Timer()
//                            timer.schedule(addNotification(banners.name.toString(),banners.date.toString(),i),
//                            calender.time, TimeUnit.MILLISECONDS.convert(1,TimeUnit.DAYS))


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


    private fun getExecutiveUpcomingActivitiesCount() {
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

                        binding.textView40.text = array.length().toString()

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
        var first = " "
        var last = " "
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
                                objectArtist.optString("pincode"),
                                objectArtist.optString("status"),
                                objectArtist.optString("email"),
                                objectArtist.optString("company"),
                                objectArtist.optString("assign_to")

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
//                        Toast.makeText(
//                            requireActivity().applicationContext,
//                            obj.getString("message"),
//                            Toast.LENGTH_SHORT
//                        ).show()
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
                if (SharedPrefManager.getInstance(requireActivity().applicationContext).isLoggedIn) {
                    first =
                        SharedPrefManager.getInstance(requireActivity().applicationContext).user.firstName
                    last =
                        SharedPrefManager.getInstance(requireActivity().applicationContext).user.lastName
                }

                params["assign_to"] =
                    first + " " + last

                return params


            }
        }

        VolleySingleton.getInstance(requireActivity().applicationContext)
            .addToRequestQueue(stringRequest)


    }

//    private fun addNotification(msg1: String, msg2: String, notifyId: Int) {
//        val builder: NotificationCompat.Builder = NotificationCompat.Builder(requireContext())
//            .setSmallIcon(R.drawable.tps_logo) //set icon for notification
//            .setContentTitle(msg1) //set title of notification
//            .setContentText(msg2)//this is notification message
//            .setAutoCancel(true) // makes auto cancel of notification
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification
//

//        Intent notificationIntent = new Intent(this, NotificationView.class);
//        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        //notification message will get at NotificationView
//        notificationIntent.putExtra("message", "This is a notification message");

//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);

    // Add as notification
//        val manager: NotificationManager =
//            (requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)!!
////            (getSystemService(Context.NOTIFICATION_SERVICE)) as NotificationManager
//        manager.notify(notifyId, builder.build());
//    }


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


                        if (count=="0")
                        {

                        }
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

    fun checkConnection(context: Context): Boolean {
        val connMgr =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connMgr != null) {
            val activeNetworkInfo = connMgr.activeNetworkInfo
            if (activeNetworkInfo != null) { // connected to the internet
                // connected to the mobile provider's data plan
                return if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    true
                } else activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE
            }
        }
        return false
    }


    // video uploading functions
    private fun chooseVideo() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_VIDEO) {
                println("SELECT_VIDEO")
                val selectedImageUri: Uri? = data!!.data
                selectedPath = getPath(selectedImageUri!!)
               // binding.textView22.setText(selectedPath)
            }
        }

    }


    //---------------------------------------------------------------------
    @SuppressLint("Range")
    open fun getPath(uri: Uri): String {
        var cursor: Cursor? =
            requireActivity().getContentResolver().query(uri, null, null, null, null)
        cursor!!.moveToFirst()
        var document_id = cursor.getString(0)
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
        cursor.close()
        cursor = requireActivity().getContentResolver().query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            null, MediaStore.Images.Media._ID + " = ? ", arrayOf(document_id), null
        )
        cursor!!.moveToFirst()
        val path = cursor!!.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
        cursor.close()
        return path
    }


    fun requestRead() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        } else {
           chooseVideo()
        }
    }


}

