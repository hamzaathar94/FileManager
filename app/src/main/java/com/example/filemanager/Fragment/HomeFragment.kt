package com.example.filemanager.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.filemanager.R
import com.example.filemanager.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var binding:FragmentHomeBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentHomeBinding.inflate(LayoutInflater.from(requireContext()),container,false)

        binding?.cardmusic?.setOnClickListener {
            findNavController().navigate(R.id.musicFragment)
        }
        binding?.cardvideo?.setOnClickListener {
            findNavController().navigate(R.id.videoFragment)

        }
        binding?.cardimage?.setOnClickListener {
            findNavController().navigate(R.id.imagesFragment)

        }
        binding?.cardpdf?.setOnClickListener {
            findNavController().navigate(R.id.pdfFragment)

        }

        return binding?.root
    }


}