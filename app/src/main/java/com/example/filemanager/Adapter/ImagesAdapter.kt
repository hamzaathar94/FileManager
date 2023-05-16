package com.example.filemanager.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filemanager.DB.Images
import com.example.filemanager.Interface.onImageClick
import com.example.filemanager.Interface.onItemClick
import com.example.filemanager.databinding.ItemVideoBinding

class ImagesAdapter(val context:Context,val image:List<Images>,val onImageClick: onImageClick):RecyclerView.Adapter<ImagesAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesAdapter.MyViewHolder {
        val binding=ItemVideoBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImagesAdapter.MyViewHolder, position: Int) {
        val data=image[position]

        holder.binding.audiotitle.text= data.title
        Glide.with(context).load(data.url.toString()).thumbnail(0.1f).centerCrop().into(holder.binding.imageView3)

        holder.itemView.setOnClickListener {
            onImageClick.onImageItemClick(data)
        }
        holder.itemView.setOnLongClickListener {
            onImageClick.onImageItemLongClick(data)
            true
        }
    }

    override fun getItemCount(): Int {
        return image.size
    }

    inner class MyViewHolder(var binding: ItemVideoBinding):RecyclerView.ViewHolder(binding.root){

    }
}