package com.example.crm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView


class UpcomingActivitiesAdapter(var data : List<NotificationReminderData>) : RecyclerView.Adapter<UpcomingActivitiesViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingActivitiesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.my_activities_item_view,parent,false)
        return UpcomingActivitiesViewHolder(view)

    }

    override fun onBindViewHolder(holder: UpcomingActivitiesViewHolder, position: Int) {
        val item = data[position]
        holder.name.text= item.name
        holder.phone.text= item.phone
        holder.address.text= item.address +" , "+ item.state +" , "+ item.city +" , "+ item.pincode
        holder.date.text= item.date
//        holder.idOfActivity.text = item.act_id




    }

    override fun getItemCount() = data.size




}

class UpcomingActivitiesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
{
    val name: TextView =itemView.findViewById(R.id.textView15)
    val phone: TextView =itemView.findViewById(R.id.textView16)
    val address: TextView =itemView.findViewById(R.id.textView17)
    val date: TextView =itemView.findViewById(R.id.textView18)
    val edit: ImageView = itemView.findViewById(R.id.imageView6)
//val idOfActivity:TextView = itemView.findViewById(R.id.activityId)

}


