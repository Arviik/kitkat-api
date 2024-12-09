package com.example.kitkat.api.models.dao

import com.example.kitkat.api.models.tables.SoundTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class SoundDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SoundDAO>(SoundTable)

    var title by SoundTable.title
    var author by UserDAO.Companion referencedOn SoundTable.author
    var duration by SoundTable.duration
    var url by SoundTable.url
    var usageCount by SoundTable.usageCount
}
