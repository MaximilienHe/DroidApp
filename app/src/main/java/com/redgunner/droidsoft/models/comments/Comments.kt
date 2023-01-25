package com.redgunner.droidsoft.models.comments

data class Comments(
    val id: Int,
    val post: Int,
    val author_name: String,
    val date: String,
    val content: Content
)

data class Content(
    val rendered: String
)