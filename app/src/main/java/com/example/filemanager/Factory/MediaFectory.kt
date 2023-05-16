package com.example.filemanager.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filemanager.Repository.FileRepository
import com.example.filemanager.ViewModel.ImagesViewModel

class MediaFectory(val fileRepository: FileRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ImagesViewModel(fileRepository) as T
    }
}