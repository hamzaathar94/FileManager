package com.example.filemanager.Fragment

import android.annotation.SuppressLint
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filemanager.Adapter.VideoAdapter
import com.example.filemanager.DB.Images
import com.example.filemanager.DB.Music
import com.example.filemanager.DB.Pdf
import com.example.filemanager.DB.Video
import com.example.filemanager.Factory.MediaFectory
import com.example.filemanager.Interface.onItemClick
import com.example.filemanager.Interface.onVideoClick
import com.example.filemanager.R
import com.example.filemanager.Repository.FileRepository
import com.example.filemanager.ViewModel.ImagesViewModel
import com.example.filemanager.databinding.FragmentVideoBinding
import com.example.filemanager.databinding.RenameFieldBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File


class VideoFragment : Fragment(),onVideoClick{
    private var binding:FragmentVideoBinding?=null
    private var videoAdapter:VideoAdapter?=null
    private var recyclerView: RecyclerView?=null
    private var imagesViewModel: ImagesViewModel? = null
    private var imagesUri: String? = null
    private var imagesTitle: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentVideoBinding.inflate(LayoutInflater.from(requireContext()),container,false)
        recyclerView=binding?.recyclerview
        val fileRepository=FileRepository(requireContext())
        imagesViewModel=ViewModelProvider(this,MediaFectory(fileRepository)).get(ImagesViewModel::class.java)
        imagesViewModel?.getVideos()
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        imagesViewModel?.getVideoLiveData()?.observe(requireActivity(), Observer {
            recyclerView?.adapter=VideoAdapter(requireContext(),it,this)
        })

        try {

        }
        catch (e:java.lang.Exception){
            Toast.makeText(requireContext(), "Error"+e.message, Toast.LENGTH_SHORT).show()
        }

        return binding?.root
    }


    override fun onVideoItemClick(video: Video) {
        val playerFragment = ExoPlayerFragment.newInstance(video.url)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.framelayout,playerFragment)
            ?.addToBackStack(null)
            ?.commit()

/*
        val builder = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.show_imgdailogue, null)
        builder.setView(view)
        builder.show()
        val title = view.findViewById<TextView>(R.id.imgTxtView)
        val img = view.findViewById<ImageView>(R.id.ViewimageView)
        val btnPopMore = view.findViewById<ImageView>(R.id.btnMore)
        title.text = videos.title
        imagesUri= videos.url
        imagesTitle = videos.title
        Glide.with(requireContext()).load(videos.url).thumbnail(0.1f).centerCrop().into(img)

        btnPopMore.setOnClickListener {
            popUpMenu(btnPopMore)
        }*/
    }

    override fun onVideoItemLongClick(video: Video) {
        val builder= AlertDialog.Builder(requireContext())
        val view= LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialogue,null)
        val rename=view.findViewById<Button>(R.id.renameBtn)
        val delete=view.findViewById<Button>(R.id.deleteBtn)
        val share=view.findViewById<Button>(R.id.shareBtn)
        val info=view.findViewById<Button>(R.id.infoBtn)
        builder.setView(view)
        val alert= builder.create()
        alert.show()
        try {
            try {
                rename.setOnClickListener {
                    val customDialog =
                        LayoutInflater.from(context).inflate(R.layout.rename_field, null)
                    val bindingRF = RenameFieldBinding.bind(customDialog)
                    val dialog = MaterialAlertDialogBuilder(requireContext()).setView(customDialog)
                        .setCancelable(false)
                        .setPositiveButton("Rename") { self, _ ->
                            self.dismiss()

                            val currenFile = File(video.url)
                            val newName = bindingRF.renameField.text
                            if (newName != null && currenFile.exists() && newName.toString()
                                    .isNotEmpty()
                            ) {

//                                val newFile = File(
//                                    "",
//                                    newName.toString() + "." + null
//                                )
                                val newFile = File(
                                    currenFile.parentFile,
                                    newName.toString() + "." + currenFile.extension
                                )
                                Log.d("IsFileRename", "popUpMenu: $newName")

                                if (currenFile.renameTo(newFile)) {
                                    Toast.makeText(context, newName.toString(), Toast.LENGTH_SHORT)
                                        .show()
                                    MediaScannerConnection.scanFile(
                                        context,
                                        arrayOf(newFile.toString()),
                                        arrayOf("video/*"),
                                        null
                                    )
                                    video.title = newFile.name
                                    video.url = newFile.path
//                                    notifyItemChanged()
                                   // binding?.recyclerview?.adapter?.notifyDataSetChanged()

                                } else {
                                    Toast.makeText(context, "Error Occurred", Toast.LENGTH_SHORT)
                                        .show()
                                }

                            } else {
                                Toast.makeText(context, "Access Denied", Toast.LENGTH_SHORT).show()
                            }

                        }
                        .setNegativeButton("Cancel") { self, _ ->
                            self.dismiss()
                        }
                        .create()
                    dialog.show()
                    bindingRF.renameField.text = SpannableStringBuilder(video.title)



                    true
                }
                    //Toast.makeText(requireContext(),"I'm clicked",Toast.LENGTH_LONG).show()

            }catch (e:Exception){
                Toast.makeText(requireContext(),"Field Empty!",Toast.LENGTH_SHORT).show()
            }
            try {
                delete.setOnClickListener {
                    val filePath = video.url // Replace with the actual file path'
                    val isDeleted = deleteFileFromStorage(filePath)
//                    val isDeleted = deleteFileFromStorage(Environment.getExternalStorageDirectory().toString()+File.separator+ filePath)
                    if (isDeleted) {
                        // File deleted successfully
                        Toast.makeText(requireContext(), "${video.title} is Deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        // Failed to delete file or file does not exist
                        Toast.makeText(requireContext(), "Error Occurred", Toast.LENGTH_SHORT).show()
                    }

                    true
                    //Toast.makeText(requireContext(), "I'm clicked", Toast.LENGTH_SHORT).show()
                }
            }catch (e:Exception){
                Toast.makeText(requireContext(),"Error!"+e.message,Toast.LENGTH_SHORT).show()
            }
            try {
                share.setOnClickListener {
                    Toast.makeText(requireContext(), "I'm clicked", Toast.LENGTH_SHORT).show()
                }
            }catch (e:Exception){
                Toast.makeText(requireContext(), "Error!"+e.message, Toast.LENGTH_SHORT).show()
            }
            try {
                info.setOnClickListener {
                    Toast.makeText(requireContext(), "I'm clicked", Toast.LENGTH_SHORT).show()
                }
            }catch (e:Exception){
                Toast.makeText(requireContext(), "Error!"+e.message, Toast.LENGTH_SHORT).show()
            }
        }
        catch (e:Exception){
            Toast.makeText(requireContext(), "Error!"+e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteFileFromStorage(filePath: String): Boolean {
        val file = File(filePath)
        if (file.exists()) { // Check if file exists
            return file.delete() // Delete the file
        }
        return false // Return false if file does not exist or deletion fails

    }


}