package com.example.crm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class MyActivitiesAdapter(var data : List<MyActivitiesData>) : Adapter<MyActivitiesViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyActivitiesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.my_activities_item_view,parent,false)
        return MyActivitiesViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyActivitiesViewHolder, position: Int) {
val item = data[position]
        holder.name.text= item.act_name
        holder.phone.text= item.act_phone
        holder.address.text= item.act_address +" , "+ item.act_state +" , "+ item.act_city +" , "+ item.act_pincode
        holder.date.text= item.act_date
        holder.status.text = item.act_status.toString()
//        holder.idOfActivity.text = item.act_id

        if(holder.status.text=="cancelled" && SharedPrefManager.getInstance(holder.edit.context).user.role=="Executive" || holder.status.text=="completed" && SharedPrefManager.getInstance(holder.edit.context).user.role=="Executive")
        {
            holder.edit.visibility=View.GONE
        }

        holder.edit.setOnClickListener {
            it.findNavController().navigate(MyActivityFragmentDirections.actionMyActivityFragmentToEditMyActivitiesFragment(item))
        }

        holder.cardviews.setOnClickListener {
            it.findNavController().navigate(MyActivityFragmentDirections.actionMyActivityFragmentToActivityDetailsFragment(item.act_name.toString(),item.act_phone.toString(),item.act_address.toString() +" , "+ item.act_state.toString() +" , "+ item.act_city.toString() +" , "+ item.act_pincode.toString(),item.act_date.toString(),item.act_id.toString()))
        }


    }

    override fun getItemCount() = data.size




}

class MyActivitiesViewHolder(itemView: View):ViewHolder(itemView)
{
val name:TextView=itemView.findViewById(R.id.textView15)
val phone:TextView=itemView.findViewById(R.id.textView16)
val address:TextView=itemView.findViewById(R.id.textView17)
val date:TextView=itemView.findViewById(R.id.textView18)
val edit: ImageView = itemView.findViewById(R.id.imageView6)
val status:TextView = itemView.findViewById(R.id.status)
val cardviews:CardView = itemView.findViewById(R.id.card1)

//val idOfActivity:TextView = itemView.findViewById(R.id.activityId)

}


