package com.example.kitkat.api.models.tables

import org.jetbrains.exposed.dao.id.CompositeIdTable

object FollowerTable : CompositeIdTable() {
    val follower = reference("follower", UserTable)
    val followed = reference("followed", UserTable)

    init {
        addIdColumn(follower)
        addIdColumn(followed)
    }

    override val primaryKey = PrimaryKey(follower, followed)
}
