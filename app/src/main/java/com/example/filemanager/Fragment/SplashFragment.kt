package com.example.filemanager.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.filemanager.R
import com.example.filemanager.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private var binding:FragmentSplashBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSplashBinding.inflate(LayoutInflater.from(requireContext()),container,false)
        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        //Normal Handler is deprecated , so we have to change the code little bit
        Handler(Looper.myLooper()!!).postDelayed({
           findNavController().navigate(R.id.homeFragment2)
        }, 3000)


        return binding?.root
    }


}