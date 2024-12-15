package com.example.kitkat.api.models.tables

import org.jetbrains.exposed.sql.Table

object UserFollowers : Table() {
    val user = reference("user_id", Users) // Utilisateur qui suit
    val follower = reference("follower_id", Users) // Utilisateur qui est suivi

    override val primaryKey = PrimaryKey(user, follower) // Empêche les doublons
}
