package com.example.kitkat.api.models.dao

import com.example.kitkat.api.models.tables.Sounds
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class SoundDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SoundDAO>(Sounds)

    var title by Sounds.title
    var author by UserDAO.Companion referencedOn Sounds.author
    var duration by Sounds.duration
    var url by Sounds.url
    var usageCount by Sounds.usageCount
}
