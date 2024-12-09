package com.example.kitkat.api.models.dao

import com.example.kitkat.api.models.tables.CommentTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CommentDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CommentDAO>(CommentTable)

    var author by UserDAO referencedOn CommentTable.author
    var video by VideoDAO referencedOn CommentTable.video
    var text by CommentTable.text
    var createdAt by CommentTable.createdAt
    var likesCount by CommentTable.likesCount
}
