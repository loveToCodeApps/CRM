package com.example.crm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.crm.databinding.FragmentDashBoardBinding

class DashBoardFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
var binding : FragmentDashBoardBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_dash_board,container,false)

    binding.button3.setOnClickListener {
        SharedPrefManager.getInstance(requireActivity().applicationContext).logout()
        requireActivity().finish()
    }


    return binding.root}


}