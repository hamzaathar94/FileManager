package com.example.filemanager.Fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filemanager.Adapter.ImagesAdapter
import com.example.filemanager.DB.Images
import com.example.filemanager.DB.Music
import com.example.filemanager.DB.Pdf
import com.example.filemanager.DB.Video
import com.example.filemanager.Factory.MediaFectory
import com.example.filemanager.Interface.onImageClick
import com.example.filemanager.Interface.onItemClick
import com.example.filemanager.R
import com.example.filemanager.Repository.FileRepository
import com.example.filemanager.ViewModel.ImagesViewModel
import com.example.filemanager.databinding.FragmentImagesBinding

class ImagesFragment : Fragment(),onImageClick {
    private var binding:FragmentImagesBinding?=null
    private var recyclerView: RecyclerView? = null
    private var imagesViewModel: ImagesViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentImagesBinding.inflate(LayoutInflater.from(requireContext()),container,false)
        recyclerView = binding?.recyclerview
        try {
            val fileRepository=FileRepository(requireContext())
            imagesViewModel=ViewModelProvider(this,MediaFectory(fileRepository)).get(ImagesViewModel::class.java)
            imagesViewModel?.getImages()
//            recyclerView?.layoutManager=StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
            recyclerView?.layoutManager= LinearLayoutManager(requireContext())
            imagesViewModel?.getImagesLiveData()?.observe(requireActivity(), Observer {
                recyclerView?.adapter=ImagesAdapter(requireContext(),it,this)
                Toast.makeText(requireContext(), "${it.size}", Toast.LENGTH_SHORT).show()
            })


        }catch (e:Exception){
            Toast.makeText(requireContext(), "Error!!"+e.message, Toast.LENGTH_SHORT).show()
        }

        return binding?.root
    }



    override fun onImageItemClick(images: Images) {
        val imageUri = images.url.toUri()
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        try {
            val chooser = Intent.createChooser(shareIntent, "Share image using")
            startActivity(chooser)
        } catch (e: ActivityNotFoundException) {
            // Handle the exception if no activity is available to handle the intent
        }
    }

    override fun onImageItemLongClick(images: Images) {
        val builder= AlertDialog.Builder(requireContext())
        val view= LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialogue,null)
        builder.setView(view)
        val alert= builder.create()
        alert.show()
        try {


        }
        catch (e:Exception){
            Toast.makeText(requireContext(), "Empty Field!"+e.message, Toast.LENGTH_SHORT).show()
        }
    }


}