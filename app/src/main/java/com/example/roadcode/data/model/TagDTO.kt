package com.example.roadcode.data.model

object TagDTO {
    data class TagsResponse(
        val tags: List<String>  // 태그 목록
    )
}