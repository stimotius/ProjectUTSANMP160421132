package com.ubaya.projectutsanmp160421132.model

data class Pengguna (
    val id: Int?,
    val username: String?,
    val nama_depan: String?,
    val nama_belakang: String?,
    val email: String?,
    val password: String?,
)

data class VideoEditor (
    val id: Int?,
    val nama: String?,
    val pembuat: String?,
    val url_gambar: String?,
    val deskripsi: String?,
    val isi: String?,
)