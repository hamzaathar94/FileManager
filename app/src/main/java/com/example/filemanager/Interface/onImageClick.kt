package com.example.filemanager.Interface

import com.example.filemanager.DB.Images

interface onImageClick {
    fun onImageItemClick(images: Images)
    fun onImageItemLongClick(images: Images)
}