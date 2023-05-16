package com.example.filemanager.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.filemanager.DB.Pdf
import com.example.filemanager.Interface.onItemClick
import com.example.filemanager.Interface.onPdfClick
import com.example.filemanager.databinding.ItemMusicBinding

class PdfAdapter(val context: Context, var pdf:List<Pdf>, val onPdfClick: onPdfClick):RecyclerView.Adapter<PdfAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfAdapter.MyViewHolder {
        val binding=ItemMusicBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PdfAdapter.MyViewHolder, position: Int) {
        val data=pdf[position]
        holder.binding.audiotitle.text=data.title

        holder.itemView.setOnClickListener {
            onPdfClick.onPdfItemClick(data)
        }
        holder.itemView.setOnLongClickListener {
            onPdfClick.onPdfItemLongClick(data)
            true
        }
    }

    override fun getItemCount(): Int {
        return pdf.size
    }

    inner class MyViewHolder(var binding: ItemMusicBinding):RecyclerView.ViewHolder(binding.root){

    }
}