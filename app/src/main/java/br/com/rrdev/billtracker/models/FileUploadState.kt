package br.com.rrdev.billtracker.models

data class FileUploadState(val isSuccessFull: Boolean = false,
                           val url: String? = null,
                           val error: String?= null) {
}