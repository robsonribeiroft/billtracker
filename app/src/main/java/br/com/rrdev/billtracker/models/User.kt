package br.com.rrdev.billtracker.models

import android.net.Uri

data class User(var id: String?,
                var name: String?,
                var email: String?,
                var photoUrl: Uri?)