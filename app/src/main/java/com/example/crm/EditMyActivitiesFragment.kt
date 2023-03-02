package com.example.crm

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.crm.databinding.FragmentEditMyActivitiesBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class EditMyActivitiesFragment : Fragment() {
    lateinit var binding: FragmentEditMyActivitiesBinding
    lateinit var reminderDate: String
    lateinit var adapter : MyActivitiesImageAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_edit_my_activities,
            container,
            false
        )





        binding.floatingActionButton4.setOnClickListener {
            val args = EditMyActivitiesFragmentArgs.fromBundle(requireArguments())
            it.findNavController().navigate(
                EditMyActivitiesFragmentDirections.actionEditMyActivitiesFragmentToHistoryFragment(
                    args.oldData.act_id.toString()
                )
            )
        }


        // status drop down
        val status = resources.getStringArray(R.array.status)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_layout, status)


        // list of all users in drop down menu
        getUsers()


        val calender = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
            calender.set(Calendar.YEAR, year)
            calender.set(Calendar.MONTH, month)
            calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(calender)
        }


        binding.reminder.setOnClickListener {
            DatePickerDialog(
                requireActivity(),
                datePicker,
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH)
            )
                .show()
        }


        val args = EditMyActivitiesFragmentArgs.fromBundle(requireArguments())
        binding.name.setText(args.oldData.act_name)
        binding.email.setText(args.oldData.act_email)
        binding.company.setText(args.oldData.act_assign_to)
        binding.editTextTextPersonName10.setText(args.oldData.act_company)
        binding.phone.setText(args.oldData.act_phone)
        binding.address.setText(args.oldData.act_address)
        binding.state.setText(args.oldData.act_state)
        binding.city.setText(args.oldData.act_city)
        binding.pincode.setText(args.oldData.act_pincode)
        binding.reminder.setText(args.oldData.act_date)
        binding.activitiesid.setText(args.oldData.act_id)
        binding.editTextTextPersonName11.setText(args.oldData.act_status)


        //   88888888888888888888888888888888888888888888888888


        binding.button.setOnClickListener {
            editActivity()
            findNavController().navigate(R.id.dashBoard)

        }


        // Here don't use binding.autocompletetv.adapter = adapter
        //it will give u error , instead use setAdapter()
        binding.editTextTextPersonName11.setOnClickListener {
            binding.editTextTextPersonName11.setAdapter(arrayAdapter)
        }


        //  binding.editTextTextPersonName11.setText("in progress")


        return binding.root

    }

    private fun editActivity() {


        val actid = binding.activitiesid.text!!.trim()
        val name = binding.name.text!!.trim()
        val email = binding.email.text!!.trim()
        val company = binding.company.text!!.trim()
        val phone = binding.phone.text!!.trim()
        val address = binding.address.text!!.trim()
        val state = binding.state.text!!.trim()
        val city = binding.city.text!!.trim()
        val pincode = binding.pincode.text!!.trim()
        val reminderDate = binding.reminder.text!!.trim()
        val assignTo = binding.editTextTextPersonName10.text.trim()
        val id = SharedPrefManager.getInstance(requireContext().applicationContext).user.id
        val status = binding.editTextTextPersonName11.text.trim()!!





        if (TextUtils.isEmpty(name)) {
            binding.name.error = "Please enter your name"
            binding.name.requestFocus()
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

        val stringRequest = object : StringRequest(
            Request.Method.POST, URLs.URL_EDIT_ACTIVITIES,
            Response.Listener { response ->

                try {
                    //converting response to json object
                    val obj = JSONObject(response)
                    //if no error in response
                    if (!obj.getBoolean("error")) {
                        Toast.makeText(
                            requireActivity().applicationContext,
                            obj.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()

                        //getting the user from the response
                        val userJson = obj.getJSONObject("user")

                        //creating a new user object
                        val user = Activity(
                            userJson.getInt("id"),
                            userJson.getString("name"),
                            userJson.getString("email"),
                            userJson.getString("company"),
                            userJson.getString("phone"),
                            userJson.getString("address"),
                            userJson.getString("state"),
                            userJson.getString("city"),
                            userJson.getString("pincode"),
                            userJson.getString("reminderDate"),
                            userJson.getString("assignTo")


                        )


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
                params["act_id"] = actid.toString()
                params["name"] = name.toString()
                params["email"] = email.toString()
                params["company"] = company.toString()
                params["phone"] = phone.toString()
                params["address"] = address.toString()
                params["state"] = state.toString()
                params["city"] = city.toString()
                params["pincode"] = pincode.toString()
                params["reminderDate"] = reminderDate.toString()
                params["assignTo"] = assignTo.toString()
                params["id"] = id.toString()
                params["status"] = status.toString()





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
        reminderDate = (sdf.format(calener.time))

        Log.i("oooooooooooooooooo", reminderDate)
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
//                            val adapter = AssignToAdapter(act_list)
//                            binding.editTextTextPersonName10.adapter = adapter
                        }
                    } else {
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




}




