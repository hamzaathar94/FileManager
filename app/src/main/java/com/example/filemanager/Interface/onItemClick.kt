package com.example.filemanager.Interface


import com.example.filemanager.DB.Images
import com.example.filemanager.DB.Music
import com.example.filemanager.DB.Pdf
import com.example.filemanager.DB.Video

interface onItemClick {
    fun onClick(images: Images)
    fun onAudioClick(music: Music)
    fun onVideoClick(videos: Video)
    fun onPdfClick(pdf: Pdf)
}