package com.example.crm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder




class AssignToAdapter(val data : List<AssignToData>) : RecyclerView.Adapter<AssignToViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignToViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.assign_to_dropdown_layout,parent,false)
        return AssignToViewHolder(view)

    }

    override fun onBindViewHolder(holder: AssignToViewHolder, position: Int) {
        val item = data[position]
        holder.name.text = item.firstName + " " + item.lastName

    }

    override fun getItemCount() = data.size


}

class AssignToViewHolder(itemView: View):ViewHolder(itemView)
{
val name:TextView = itemView.findViewById(R.id.assignTo)
}
