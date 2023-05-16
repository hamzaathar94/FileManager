package com.example.filemanager.Interface

import com.example.filemanager.DB.Video

interface onVideoClick {
    fun onVideoItemClick(video: Video)
    fun onVideoItemLongClick(video: Video)
}