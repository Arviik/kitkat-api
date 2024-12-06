package com.example.kitkat.api.models.tables

import org.jetbrains.exposed.dao.id.CompositeIdTable

object Followers : CompositeIdTable() {
    val follower = reference("follower", Users)
    val followed = reference("followed", Users)

    init {
        addIdColumn(follower)
        addIdColumn(followed)
    }

    override val primaryKey = PrimaryKey(follower, followed)
}
