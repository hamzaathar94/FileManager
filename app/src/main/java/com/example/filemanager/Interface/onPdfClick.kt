package com.example.filemanager.Interface

import com.example.filemanager.DB.Pdf

interface onPdfClick {
    fun onPdfItemClick(pdf: Pdf)
    fun onPdfItemLongClick(pdf: Pdf)
}