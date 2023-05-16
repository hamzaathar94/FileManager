package com.example.filemanager.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filemanager.DB.Music
import com.example.filemanager.Interface.onAudioClick
import com.example.filemanager.Interface.onItemClick
import com.example.filemanager.databinding.ItemMusicBinding

class MusicAdapter(val context: Context, var musicList: List<Music>,
    private var onAudioClick: onAudioClick) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val binding = ItemMusicBinding.inflate(LayoutInflater.from(context), parent, false)
        return MusicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val   musicList = musicList[position]
       holder.binding.audiotitle.text=musicList.title

     holder.itemView.setOnClickListener {
         onAudioClick.onAudioItemClick(musicList)
     }
        holder.itemView.setOnLongClickListener {
            onAudioClick.onAudioItemLongClick(musicList)
            true
        }

    }

    override fun getItemCount(): Int = musicList?.size!!

    inner class MusicViewHolder(val binding: ItemMusicBinding) : RecyclerView.ViewHolder(binding.root)
    {


    }
}