package com.example.kitkat.api.models.dao.entities

import com.example.kitkat.api.models.dao.tables.SearchQueries
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class SearchQuery(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SearchQuery>(SearchQueries)

    var user by User.Companion referencedOn SearchQueries.user
    var query by SearchQueries.query
    var createdAt by SearchQueries.createdAt
}
