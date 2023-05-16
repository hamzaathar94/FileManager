package com.example.filemanager.Interface

import com.example.filemanager.DB.Music

interface onAudioClick {
    fun onAudioItemClick(music: Music)
    fun onAudioItemLongClick(music: Music)
}