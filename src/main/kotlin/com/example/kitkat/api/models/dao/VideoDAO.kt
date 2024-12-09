package com.example.kitkat.api.models.dao

import com.example.kitkat.api.models.tables.VideoTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class VideoDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<VideoDAO>(VideoTable)

    var title by VideoTable.title
    var duration by VideoTable.duration
    var author by UserDAO referencedOn VideoTable.author
    var videoUrl by VideoTable.videoUrl
    var viewCount by VideoTable.viewCount
    var likeCount by VideoTable.likeCount
    var commentCount by VideoTable.commentCount
    var createdAt by VideoTable.createdAt
    var isPublic by VideoTable.isPublic
    var sound by SoundDAO referencedOn VideoTable.sound
}
