package com.example.crm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView


class AdminHistoryAdapter(var data : List<AdminHistoryData>) : RecyclerView.Adapter<AdminHistoryViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.admin_history_item_view,parent,false)
        return AdminHistoryViewHolder(view)

    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: AdminHistoryViewHolder, position: Int) {
        val item = data[position]
        holder.name.text= item.name
        holder.phone.text= item.phone
        holder.address.text= item.address +" , "+ item.state +" , "+ item.city +" , "+ item.pincode
        holder.date.text= item.date    }


}

class AdminHistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
{
    val name: TextView =itemView.findViewById(R.id.textView15)
    val phone: TextView =itemView.findViewById(R.id.textView16)
    val address: TextView =itemView.findViewById(R.id.textView17)
    val date: TextView =itemView.findViewById(R.id.textView18)
//val idOfActivity:TextView = itemView.findViewById(R.id.activityId)

}


