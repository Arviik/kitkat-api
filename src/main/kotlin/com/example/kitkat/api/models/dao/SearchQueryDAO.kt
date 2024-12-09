package com.example.kitkat.api.models.dao

import com.example.kitkat.api.models.tables.SearchQueryTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class SearchQueryDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SearchQueryDAO>(SearchQueryTable)

    var user by UserDAO.Companion referencedOn SearchQueryTable.user
    var query by SearchQueryTable.query
    var createdAt by SearchQueryTable.createdAt
}
