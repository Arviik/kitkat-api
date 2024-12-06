package com.example.kitkat.api.models.dao

import com.example.kitkat.api.models.tables.Comments
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CommentDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CommentDAO>(Comments)

    var author by UserDAO referencedOn Comments.author
    var video by VideoDAO referencedOn Comments.video
    var text by Comments.text
    var createdAt by Comments.createdAt
    var likesCount by Comments.likesCount
}
