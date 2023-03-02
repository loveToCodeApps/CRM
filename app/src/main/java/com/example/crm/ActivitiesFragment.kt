package com.example.crm

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
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
import com.android.volley.toolbox.Volley
import com.example.crm.databinding.FragmentActivitiesBinding
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// T
class ActivitiesFragment : Fragment() {
lateinit var binding:FragmentActivitiesBinding
lateinit var reminderDate:String
var doneVolleyRequest:Boolean=false
private var images:ArrayList<String>?=null
private val PICK_IMAGES_CODE = 0
lateinit var selectedPicture: String
lateinit var reminderDates:String
var count = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
binding = DataBindingUtil.inflate(inflater,R.layout.fragment_activities,container,false)
getUsers()

        images = ArrayList()
        reminderDates = "00-00-0000"


        val calender = Calendar.getInstance()
val datePicker = DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
    calender.set(Calendar.YEAR,year)
    calender.set(Calendar.MONTH,month)
    calender.set(Calendar.DAY_OF_MONTH,dayOfMonth)
    updateLabel(calender)
}


        binding.reminder.setOnClickListener {
            DatePickerDialog(requireActivity(),datePicker,calender.get(Calendar.YEAR),calender.get(Calendar.MONTH),calender.get(Calendar.DAY_OF_MONTH))
                .show()
        }


        binding.button.setOnClickListener {
            binding.progressBar3.visibility = View.VISIBLE
            addActivity()


        }


        binding.imageView7.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent,"Select Images"),PICK_IMAGES_CODE)
        }

        return binding.root
    }


    private fun getUsers() {
        val act_list = ArrayList<String>()


        val stringRequest = StringRequest(
            Request.Method.GET,
            URLs.URL_GET_USERS,
            { s ->
                try {
                    val obj = JSONObject(s)
                    if (!obj.getBoolean("error")) {
                        val array = obj.getJSONArray("users")

                        if (SharedPrefManager.getInstance(requireActivity().applicationContext).user.role=="Admin") {
                            for (i in 0 until array.length()) {
                                val objectArtist = array.getJSONObject(i)
                                val banners = AssignToData(
                                    objectArtist.optString("fname"),
                                    objectArtist.optString("lname")

                                )

                                act_list.add(banners.firstName + " " + banners.lastName)
                                val arrayAdapter = ArrayAdapter(
                                    requireContext(),
                                    R.layout.assign_to_dropdown_layout,
                                    act_list
                                )
                                binding.editTextTextPersonName10.setAdapter(arrayAdapter)
                            }
                        }
                        else if ((SharedPrefManager.getInstance(requireActivity().applicationContext).user.role=="Executive"))
                        {
                            binding.editTextTextPersonName10.setText(SharedPrefManager.getInstance(requireActivity().applicationContext).user.firstName+" "+SharedPrefManager.getInstance(requireActivity().applicationContext).user.lastName)
                        }
//                            val adapter = AssignToAdapter(act_list)
//                            binding.editTextTextPersonName10.adapter = adapter
                        }
                     else {
                        Toast.makeText(
                            requireContext(),
                            obj.getString("message"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { volleyError ->
                Toast.makeText(
                    requireContext(),
                    volleyError.message,
                    Toast.LENGTH_LONG
                ).show()
            })

        val requestQueue = Volley.newRequestQueue(requireContext())
        requestQueue.add(stringRequest)
    }

    private fun addActivity() {

        val name = binding.name.text!!.trim()
        val email = binding.email.text!!.trim()
        val company = binding.company.text!!.trim()
        val phone = binding.phone.text!!.trim()
        val address = binding.address.text!!.trim()
        val state = binding.state.text!!.trim()
        val city = binding.city.text!!.trim()
        val pincode = binding.pincode.text!!.trim()
        val reminderDate = reminderDates
        val assignTo = binding.editTextTextPersonName10.text.trim()
        val id = SharedPrefManager.getInstance(requireContext().applicationContext).user.id





        if (TextUtils.isEmpty(name)) {
            binding.name.error = "Please enter your name"
            binding.name.requestFocus()
            return
        }
        if (TextUtils.isEmpty(email)) {
            binding.email.error = "Please enter your email"
            binding.email.requestFocus()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.email.error = "Enter a valid email"
            binding.email.requestFocus()
            return
        }

        if (TextUtils.isEmpty(company)) {
            binding.company.error = "Please enter company name"
          binding.company.requestFocus()
            return
        }

        if (TextUtils.isEmpty(assignTo)) {
            binding.tl8.error = "Please enter company name"
            binding.tl8.requestFocus()
            return
        }



        if (TextUtils.isEmpty(phone)) {
            binding.phone.error = "Please enter phone number"
            binding.phone.requestFocus()
            return
        }

        if (TextUtils.getTrimmedLength(phone) < 10) {
            binding.phone.error = "enter 10 digit phone number"
            binding.phone.requestFocus()
            return
        }

        if (TextUtils.isEmpty(address)) {
            binding.address.error = "Please enter your address"
            binding.address.requestFocus()
            return
        }

        if (TextUtils.isEmpty(state)) {
            binding.state.error = "Please enter your state name"
            binding.state.requestFocus()
            return

        }

        if (TextUtils.isEmpty(city)) {
            binding.city.error = "Please enter your city name"
            binding.city.requestFocus()
            return
        }
        if (TextUtils.isEmpty(pincode)) {
            binding.pincode.error = "Please enter your pincode"
            binding.pincode.requestFocus()
            return
        }

        if (TextUtils.isEmpty(reminderDate)) {
            binding.reminder.error = "Please select reminder date"
            binding.reminder.requestFocus()
            return
        }

        if (TextUtils.isEmpty(reminderDate) || reminderDate.length==0 || reminderDate==null) {
            binding.reminder.error = "Please select reminder date"
            binding.reminder.requestFocus()
            return
        }


//        uploadImage

        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_ACTIVITIES,
            Response.Listener { response ->

                try {
                    //converting response to json object
                    val obj = JSONObject(response)
                    //if no error in response
                    if (!obj.getBoolean("error")) {

                      // if there are images call this function
                        if(images!!.size!=0) {
                            uploadImages(obj.getString("last_id"), assignTo.toString())
                        }
                        else
                        {
                            // if no images selected , then go to dashboard directly
                           binding.progressBar3.visibility = View.GONE
                            Toast.makeText(
                                requireActivity().applicationContext,
                              "Activity successfully added",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.dashBoard)
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
                params["name"] = name.toString()
                params["email"] = email.toString()
                params["company"] = company.toString()
                params["phone"] = phone.toString()
                params["address"] = address.toString()
                params["state"] = state.toString()
                params["city"] = city.toString()
                params["pincode"] = pincode.toString()
                params["reminderDate"] = reminderDate
                params["assignTo"] = assignTo.toString()
                params["id"] = id.toString()
             //   params["images"] = img_name.toString()
//                params["videos"] = vid_name.toString()




                return params
            }
        }

        VolleySingleton.getInstance(requireActivity().applicationContext).addToRequestQueue(stringRequest)



    }

    private fun uploadImages(last: String, assign: String) {
        val stringRequest = object : StringRequest(

            Request.Method.POST, "http://192.168.0.107/crm/registrationapi.php?apicall=uploadPictures",
            Response.Listener { response ->

                try {
                    //converting response to json object
                    val obj = JSONObject(response)
                    //if no error in response
                    if (!obj.getBoolean("error")) {
    //----------------- here put code to go to dashboard
                        //----------------- here put code to go to dashboard
                        //----------------- here put code to go to dashboard
                        //----------------- here put code to go to dashboard
                        //----------------- here put code to go to dashboardv
                        //----------------- here put code to go to dashboard
                   binding.progressBar3.visibility = View.GONE
                        Toast.makeText(
                            requireActivity().applicationContext,
                            "Activity added successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.dashBoard)

//
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
                params["count"] = count.toString()
                params["last"] = last
                params["assign"] = assign
                for(i in 0 ..count-1) {
                    var numb = (i+1).toString()
                    params["picture$numb"] = images!![i]
//                      Log.i("888",images!![i])
                    Log.i("888","picture$numb")
                    Log.i("888",images!![i])
                }
                // params["picture"] = selectedPicture
                return params
            }
        }



        VolleySingleton.getInstance(requireActivity().applicationContext)
            .addToRequestQueue(stringRequest)

    }


    private fun updateLabel(calener: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
// Here we can use calendar selected date
        binding.reminder.setText((sdf.format(calener.time)))
        reminderDate=(sdf.format(calener.time))



        val myFormat2 = "yyyy-MM-dd"
        val sdf2 = SimpleDateFormat(myFormat2, Locale.UK)
        reminderDates=(sdf2.format(calener.time))
        Log.i("oooooooooooooooooo",reminderDate)


    }





    //----------------------------------------------------------------------------------------------------------


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==PICK_IMAGES_CODE)
        {
            if (resultCode== Activity.RESULT_OK)
            {
                if (data!!.clipData!=null)
                {
                    // total images selected
                    count = data.clipData!!.itemCount

                    for(i in 0 until count) {
                        val imgUri = data.clipData!!.getItemAt(i).uri

                        val bitmap =
                            MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imgUri)
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val imageBytes = baos.toByteArray()
                        selectedPicture =
                            android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT)
                        val bytesImage: ByteArray =
                            android.util.Base64.decode(selectedPicture, android.util.Base64.DEFAULT)

                        images?.add(selectedPicture)



                    }

                        Toast.makeText(requireContext(),"$count images selected",Toast.LENGTH_SHORT).show()

                }

                else
                {
                    Toast.makeText(requireContext(),"only 1 image selected",Toast.LENGTH_SHORT).show()
                    val imgUri = data.data

                    val bitmap =
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imgUri)
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val imageBytes = baos.toByteArray()
                    selectedPicture =
                        android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT)
                    val bytesImage: ByteArray =
                        android.util.Base64.decode(selectedPicture, android.util.Base64.DEFAULT)

                    images?.add(selectedPicture)


                }

            }
        }

    }




}






