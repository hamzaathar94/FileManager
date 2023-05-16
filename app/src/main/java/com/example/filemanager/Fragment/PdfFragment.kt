package com.example.filemanager.Fragment

import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaScannerConnection
import android.os.Bundle
import android.provider.DocumentsContract
import android.text.SpannableStringBuilder
import android.util.Log
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
import com.example.filemanager.Adapter.PdfAdapter
import com.example.filemanager.DB.Images
import com.example.filemanager.DB.Music
import com.example.filemanager.DB.Pdf
import com.example.filemanager.DB.Video
import com.example.filemanager.Factory.MediaFectory
import com.example.filemanager.Interface.onItemClick
import com.example.filemanager.Interface.onPdfClick
import com.example.filemanager.R
import com.example.filemanager.Repository.FileRepository

import com.example.filemanager.ViewModel.ImagesViewModel
import com.example.filemanager.databinding.FragmentPdfBinding
import com.example.filemanager.databinding.RenameFieldBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File


class PdfFragment : Fragment() ,onPdfClick{
    private var binding:FragmentPdfBinding?=null
    private var recyclerView: RecyclerView?=null
    private var imagesViewModel: ImagesViewModel?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentPdfBinding.inflate(LayoutInflater.from(requireContext()),container,false)
        recyclerView=binding?.recyclerview
        try {
            val fileRepository= FileRepository(requireContext())
            imagesViewModel= ViewModelProvider(this, MediaFectory(fileRepository)).get(ImagesViewModel::class.java)
            imagesViewModel?.getPdfFiles()
            recyclerView?.layoutManager=LinearLayoutManager(requireContext())
            imagesViewModel?.getPdfFilesLiveData()?.observe(requireActivity(), Observer {
                recyclerView?.adapter=PdfAdapter(requireContext(),it,this)
            })
        }
        catch (e:Exception){
            Toast.makeText(requireContext(), "Error!!"+e.message, Toast.LENGTH_SHORT).show()
        }

        return binding?.root
    }

    override fun onPdfItemClick(pdf: Pdf) {
        TODO("Not yet implemented")
    }

    override fun onPdfItemLongClick(pdf: Pdf) {
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

                            val currenFile = File(pdf.url.toString())
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
                                        arrayOf("Downloads/*"),
                                        null
                                    )
                                    pdf.title = newFile.name
                                    pdf.url = newFile.path.toUri()
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
                    bindingRF.renameField.text = SpannableStringBuilder(pdf.title)
                }
                //Toast.makeText(requireContext(),"I'm clicked",Toast.LENGTH_LONG).show()

            }catch (e:Exception){
                Toast.makeText(requireContext(),"Field Empty!",Toast.LENGTH_SHORT).show()
            }
            try {
                delete.setOnClickListener {
                    val filePath = pdf.url // Replace with the actual file path'
                    val isDeleted = deleteFileFromStorage(filePath.toString())
//                    val isDeleted = deleteFileFromStorage(Environment.getExternalStorageDirectory().toString()+File.separator+ filePath)
                    if (isDeleted) {
                        // File deleted successfully
                        Toast.makeText(requireContext(), "${pdf.title} is Deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        // Failed to delete file or file does not exist
                        Toast.makeText(requireContext(), "Error Occurred", Toast.LENGTH_SHORT).show()
                    }

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