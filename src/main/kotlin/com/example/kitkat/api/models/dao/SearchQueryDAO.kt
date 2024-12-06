package com.example.kitkat.api.models.dao

import com.example.kitkat.api.models.tables.SearchQueries
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class SearchQueryDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SearchQueryDAO>(SearchQueries)

    var user by UserDAO.Companion referencedOn SearchQueries.user
    var query by SearchQueries.query
    var createdAt by SearchQueries.createdAt
}
