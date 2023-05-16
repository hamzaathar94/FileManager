package com.example.filemanager.Adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.media.MediaScannerConnection
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filemanager.DB.Music
import com.example.filemanager.DB.Video
import com.example.filemanager.Interface.onItemClick
import com.example.filemanager.Interface.onVideoClick
import com.example.filemanager.R
import com.example.filemanager.databinding.ItemMusicBinding
import com.example.filemanager.databinding.ItemVideoBinding
import com.google.android.material.color.MaterialColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class VideoAdapter(val context: Context, var musicList: List<Video>,
                   val onVideoClick: onVideoClick) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val   musicList = musicList[position]
       holder.binding.audiotitle.text=musicList.title
        Glide.with(context).load(musicList.url.toString()).thumbnail(0.1f).centerCrop().into(holder.binding.imageView3)
        holder.itemView.setOnClickListener {
            onVideoClick.onVideoItemClick(musicList)
        }
        holder.itemView.setOnLongClickListener {
            onVideoClick.onVideoItemLongClick(musicList)

            true
        }
        holder.binding.fav.setOnClickListener {
            try {
                //for devices less than android 11
                val file = File(musicList.url)
                val builder = MaterialAlertDialogBuilder(context)
                builder.setTitle("Delete Video?")
                    .setMessage(musicList.title)
                    .setPositiveButton("Yes"){ self, _ ->
                        if(file.exists() && file.delete()){
                            MediaScannerConnection.scanFile(context, arrayOf(file.path), null, null)
                          //  updateDeleteUI(position = position)
                        }
                        self.dismiss()
                    }
                    .setNegativeButton("No"){self, _ -> self.dismiss() }
                val delDialog = builder.create()
                delDialog.show()
                delDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(
                    MaterialColors.getColor(context, R.attr.themeColor, Color.RED)
                )
                delDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(
                    MaterialColors.getColor(context, R.attr.themeColor, Color.RED)
                )
            }catch (e:Exception){
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                //Toast.makeText(context, "Error"+e.message, Toast.LENGTH_SHORT).show()
            }
        }

    }


    override fun getItemCount(): Int = musicList?.size!!

    inner class VideoViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root)
    {


    }
}