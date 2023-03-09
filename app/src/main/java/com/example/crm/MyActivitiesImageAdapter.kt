package com.example.crm

import android.content.Context
import android.provider.ContactsContract.DeletedContacts
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject


class MyActivitiesImageAdapter(var data : List<MyActivitiesImageData>) : RecyclerView.Adapter< MyActivitiesImageViewHolder>()
{

    var count = data.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  MyActivitiesImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.my_activities_image_item_view,parent,false)
        return  MyActivitiesImageViewHolder(view)

    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder:  MyActivitiesImageViewHolder, position: Int) {
        val item = data[position]

//        Glide.with(holder.image.context)
//            .load("http://192.168.0.107/crm/"+item.url)
//            .into((holder.image))

        Glide.with(holder.image.context)
            .load(item.url)
            .into((holder.image))

        holder.image.setOnClickListener {
            Toast.makeText(it.context,"Pinch image to Zoom-In & Zoom-Out",Toast.LENGTH_SHORT).show()
            val mBuilder: AlertDialog.Builder = AlertDialog.Builder(it.context)
            val mView: View =LayoutInflater.from(it.context).inflate(R.layout.dialog_custom_layout,null)
//            inflate(R.layout.dialog_custom_layout, null)
            val photoView: PhotoView = mView.findViewById(R.id.imageView)
            Picasso.get().load(item.url).into(photoView)
            mBuilder.setView(mView)
            val mDialog: AlertDialog = mBuilder.create()
            mDialog.show()
        }


        holder.delete.setOnClickListener {

            if (count == data.size) {
                val builder = AlertDialog.Builder(it.context)
                //set title for alert dialog
                builder.setTitle("Delete!!")
                //set message for alert dialog
                builder.setMessage("Confirm to delete images?")
                builder.setIcon(R.drawable.tps_logo)

                //performing positive action
                builder.setPositiveButton("Yes") { dialogInterface, which ->
                    // if clicked on yes button we will not show alert on further delete clicks


                    val stringRequest = object : StringRequest(
                        Request.Method.POST, URLs.URL_DELETE_IMAGE,
                        Response.Listener { response ->

                            try {
                                //converting response to json object
                                val obj = JSONObject(response)
                                //if no error in response
                                if (!obj.getBoolean("error")) {
                                    val array = obj.getJSONArray("user")
                                    Toast.makeText(it.context,"Deleted Image successfully",Toast.LENGTH_SHORT).show()



                                } else {
                                    Toast.makeText(it.context , obj.getString("message"), Toast.LENGTH_SHORT).show()
                                }

                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }

                        },
                        Response.ErrorListener { error -> Toast.makeText(it.context, error.message, Toast.LENGTH_SHORT).show() }
                    ) {
                        @Throws(AuthFailureError::class)
                        override fun getParams(): Map<String, String> {
                            val params = HashMap<String, String>()
                            params["id"] = item.imgId

                            return params

                        }
                    }

                    VolleySingleton.getInstance(it.context).addToRequestQueue(stringRequest)
                    it.findNavController().navigate(ActivityDetailsFragmentDirections.actionActivityDetailsFragmentSelf(item.title,item.phone,item.address,item.date,item.actid))


                }
                //performing negative action
                builder.setNegativeButton("No") { dialogInterface, which ->
                    Toast.makeText(it.context.applicationContext, "It's ok", Toast.LENGTH_LONG)
                        .show()
                }
                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
            else
            {
                val stringRequest = object : StringRequest(
                    Request.Method.POST, URLs.URL_DELETE_IMAGE,
                    Response.Listener { response ->

                        try {
                            //converting response to json object
                            val obj = JSONObject(response)
                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                val array = obj.getJSONArray("user")
                                Toast.makeText(it.context,"Deleted Image successfully",Toast.LENGTH_SHORT).show()


                            } else {
                                Toast.makeText(it.context , obj.getString("message"), Toast.LENGTH_SHORT).show()
                            }

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    },
                    Response.ErrorListener { error -> Toast.makeText(it.context, error.message, Toast.LENGTH_SHORT).show() }
                ) {
                    @Throws(AuthFailureError::class)
                    override fun getParams(): Map<String, String> {
                        val params = HashMap<String, String>()
                        params["id"] = item.imgId

                        return params

                    }
                }

                VolleySingleton.getInstance(it.context).addToRequestQueue(stringRequest)
                it.findNavController().navigate(ActivityDetailsFragmentDirections.actionActivityDetailsFragmentSelf(item.title,item.phone,item.address,item.date,item.actid))



            }
        }
        }
       }




class  MyActivitiesImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
{
    val image: ImageView =itemView.findViewById(R.id.imageView11)
    val delete:ImageView = itemView.findViewById(R.id.imageView12)

//val idOfActivity:TextView = itemView.findViewById(R.id.activityId)

}


