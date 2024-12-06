package com.example.kitkat.api.models.dao

import com.example.kitkat.api.models.tables.Videos
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class VideoDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<VideoDAO>(Videos)

    var title by Videos.title
    var duration by Videos.duration
    var author by UserDAO referencedOn Videos.author
    var videoUrl by Videos.videoUrl
    var viewCount by Videos.viewCount
    var likeCount by Videos.likeCount
    var commentCount by Videos.commentCount
    var createdAt by Videos.createdAt
    var isPublic by Videos.isPublic
    var sound by SoundDAO referencedOn Videos.sound
}
