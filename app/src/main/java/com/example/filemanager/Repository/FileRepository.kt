package com.example.filemanager.Repository

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context

import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.filemanager.DB.Images
import com.example.filemanager.DB.Music
import com.example.filemanager.DB.Pdf
import com.example.filemanager.DB.Video

class FileRepository(val context: Context) {

    // audio

    val audioLiveData = MutableLiveData<List<Music>>()

    @SuppressLint("Range")
    fun getAudio() {
        val audio = mutableListOf<Music>()
// Use content resolver to query for music files on device
        val musicCurser = context.contentResolver?.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )
        // Iterate through cursor to get music file information
        if (musicCurser != null && musicCurser.moveToFirst()) {
            do {
                val title =
                    musicCurser.getString(musicCurser.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val path =
                    musicCurser.getString(musicCurser.getColumnIndex(MediaStore.Audio.Media.DATA))
                audio.add(Music(title, path))
            } while (musicCurser.moveToNext())
            musicCurser.close()
            audioLiveData.postValue(audio)
        }
    }

    fun getAudioLiveData(): LiveData<List<Music>> {
        return audioLiveData
    }

    // video

    val videoLiveData = MutableLiveData<List<Video>>()
    fun getVideos() {
        val videos = mutableListOf<Video>()
        val projection = arrayOf(
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATA
        )
        val selection = "${MediaStore.Video.Media.BUCKET_DISPLAY_NAME}=?"
        val selectionArgs = arrayOf("WhatsApp Video")
        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"
        val cursor = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection, selectionArgs, sortOrder
        )
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val title =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
                val image =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                videos.add(Video(title, image))
            } while (cursor.moveToNext())
            cursor.close()
        }
        videoLiveData.postValue(videos)
    }

    fun getVideosLiveData(): LiveData<List<Video>> {
        return videoLiveData
    }

    //      *Images*
    val imagesLiveData = MutableLiveData<List<Images>>()
    fun getImages() {
        val images = mutableListOf<Images>()
        val projection = arrayOf(MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media._ID)
        val selection = "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} = ?"
        val selcetionArgs = arrayOf("Snapchat")
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selcetionArgs,
            sortOrder
        )
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                val image = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                val imageId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,imageId)
                images.add(Images(title,imageUri.toString()))
            } while (cursor.moveToNext())
            cursor.close()
        }
        imagesLiveData.postValue(images)
    }

    fun getImagesLiveData(): LiveData<List<Images>> {
        return imagesLiveData
    }

    // Fetch PDF files from device's download folder
    val pdfFilesLiveData = MutableLiveData<List<Pdf>>()
    fun getPdfFiles() {
        val pdfFilesList = mutableListOf<Pdf>()
        val downloadsFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        // Check if downloads folder exists
        if (downloadsFolder.exists() && downloadsFolder.isDirectory) {
            // Get list of files in downloads folder
            val files = downloadsFolder.listFiles()
            // Iterate through the files and filter PDF files
            files?.forEach { file ->
                if (file.isFile && file.extension.equals("pdf", ignoreCase = true)) {
                    // Add PDF file to list
                    pdfFilesList.add(Pdf(file.name, file.path.toString().toUri()))
                }
            }
            Log.d("pdfFilesChecking", "getPdfFiles: ${files.size} ")
        }
        // Update LiveData with PDF files list
        pdfFilesLiveData.postValue(pdfFilesList)
    }
    fun getPdfFilesLiveData():LiveData<List<Pdf>>{
        return pdfFilesLiveData
    }


}