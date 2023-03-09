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

class AddedUsersAdapter(var data : List<AddedUsersData>) : Adapter<AddedUsersViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddedUsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.added_users_item_view,parent,false)
        return AddedUsersViewHolder(view)

    }

    override fun onBindViewHolder(holder: AddedUsersViewHolder, position: Int) {

        val item = data[position]
        holder.name.text = item.fname + " " + item.lname
        holder.phone.text = item.phone
        holder.email.text = item.email
        holder.role.text = item.role

    }

    override fun getItemCount() = data.size




}

class AddedUsersViewHolder(itemView: View):ViewHolder(itemView)
{
    val name:TextView=itemView.findViewById(R.id.textView15)
    val phone:TextView=itemView.findViewById(R.id.textView16)
    val email:TextView=itemView.findViewById(R.id.textView17)
    val role:TextView=itemView.findViewById(R.id.textView18)
}


