package com.example.filemanager.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filemanager.DB.Images
import com.example.filemanager.DB.Music
import com.example.filemanager.DB.Pdf
import com.example.filemanager.DB.Video
import com.example.filemanager.Repository.FileRepository

class ImagesViewModel(private val fileRepository: FileRepository):ViewModel() {
    //******* Audio**************
    val audio: MutableLiveData<List<Music>> = fileRepository.audioLiveData
    val imagesLiveData: MutableLiveData<List<Images>> = fileRepository.imagesLiveData

    fun getAudio() {
        fileRepository.getAudio()
    }

    fun getAudioLiveData(): LiveData<List<Music>> {
        fileRepository.getAudioLiveData()
        return audio
    }

//******* Videos**************

    val video: MutableLiveData<List<Video>> = fileRepository.videoLiveData

    fun getVideos() {
        fileRepository.getVideos()
    }

    fun getVideoLiveData(): LiveData<List<Video>> {
        fileRepository.getVideosLiveData()
        return video
    }

    // val data: MutableLiveData<List<Image>> = videoRepository.imagesLiveData

    fun getImages() {
        fileRepository.getImages()
    }

    fun getImagesLiveData(): LiveData<List<Images>> {

        return imagesLiveData
    }
    //*********PDF****************
    val pdf:MutableLiveData<List<Pdf>> = fileRepository.pdfFilesLiveData

    fun getPdfFiles(){
        fileRepository.getPdfFiles()

    }
    fun getPdfFilesLiveData():LiveData<List<Pdf>>{
        fileRepository.getPdfFilesLiveData()
        return pdf
    }
}