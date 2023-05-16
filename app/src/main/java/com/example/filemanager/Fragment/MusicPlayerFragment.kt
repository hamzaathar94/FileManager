package com.example.filemanager.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.filemanager.R
import com.example.filemanager.databinding.FragmentMusicPlayerBinding


class MusicPlayerFragment : Fragment() {
    private var binding:FragmentMusicPlayerBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentMusicPlayerBinding.inflate(LayoutInflater.from(requireContext()),container,false)



        return binding?.root
    }


}