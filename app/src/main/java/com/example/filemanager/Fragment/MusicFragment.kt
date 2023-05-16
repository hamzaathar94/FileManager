package com.example.filemanager.Fragment

import android.annotation.SuppressLint
import android.media.MediaPlayer
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
import com.example.filemanager.Adapter.MusicAdapter
import com.example.filemanager.DB.Images
import com.example.filemanager.DB.Music
import com.example.filemanager.DB.Pdf
import com.example.filemanager.DB.Video
import com.example.filemanager.Factory.MediaFectory
import com.example.filemanager.Interface.onAudioClick
import com.example.filemanager.Interface.onItemClick
import com.example.filemanager.R
import com.example.filemanager.Repository.FileRepository
import com.example.filemanager.ViewModel.ImagesViewModel
import com.example.filemanager.databinding.FragmentMusicBinding
import com.example.filemanager.databinding.RenameFieldBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.IOException

class MusicFragment : Fragment() ,onAudioClick{
    private var binding:FragmentMusicBinding?=null
    private var recyclerView:RecyclerView?=null
    private  var musicList: ArrayList<Music>?=null
    private  var mediaPlayer: MediaPlayer?=null
    private var musicAdapter:MusicAdapter?=null
    private var imagesViewModel:ImagesViewModel?=null
    private var imagesUri: String? = null
    private var imagesTitle: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentMusicBinding.inflate(LayoutInflater.from(requireContext()),container,false)

        recyclerView=binding?.recyclerview
        val fileRepository=FileRepository(requireContext())
        imagesViewModel= ViewModelProvider(this,MediaFectory(fileRepository)).get(ImagesViewModel::class.java)
        imagesViewModel?.getAudio()
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        imagesViewModel?.getAudioLiveData()?.observe(requireActivity(), Observer {
            recyclerView?.adapter=MusicAdapter(requireContext(),it,this)
        })


        mediaPlayer = MediaPlayer()
        try {

        }
        catch (e:java.lang.Exception){
            Toast.makeText(requireContext(), "Error"+e.message, Toast.LENGTH_SHORT).show()
        }

        return binding?.root
    }



    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }







    override fun onAudioItemClick(music: Music) {
        try {
//            mediaPlayer?.reset()
//            mediaPlayer?.setDataSource(music.url)
//            mediaPlayer?.prepare()
//            mediaPlayer?.start()

            val playerFragment = ExoPlayerFragment.newInstance(music.url)
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.framelayout,playerFragment)
                ?.addToBackStack(null)
                ?.commit()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onAudioItemLongClick(music: Music) {
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

                            val currenFile = File(music.url)
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
                                    music.title = newFile.name
                                    music.url = newFile.path
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
                    bindingRF.renameField.text = SpannableStringBuilder(music.title)



                    true
                }
                //Toast.makeText(requireContext(),"I'm clicked",Toast.LENGTH_LONG).show()

            }catch (e:Exception){
                Toast.makeText(requireContext(),"Field Empty!",Toast.LENGTH_SHORT).show()
            }
            try {
                delete.setOnClickListener {
                    val filePath = music.url // Replace with the actual file path'
                    val isDeleted = deleteFileFromStorage(filePath)
//                    val isDeleted = deleteFileFromStorage(Environment.getExternalStorageDirectory().toString()+File.separator+ filePath)
                    if (isDeleted) {
                        // File deleted successfully
                        Toast.makeText(requireContext(), "${music.title} is Deleted", Toast.LENGTH_SHORT).show()
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