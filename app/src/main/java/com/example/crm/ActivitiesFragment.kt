package com.example.crm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.crm.databinding.FragmentActivitiesBinding

// T
class ActivitiesFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
val binding : FragmentActivitiesBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_activities,container,false)





        return binding.root

    }


}