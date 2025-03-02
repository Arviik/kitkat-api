package com.example.kitkat.api.services

import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.kitkat.api.models.dao.UserDAO
import com.example.kitkat.api.models.dataclass.UserDTO
import com.example.kitkat.api.models.dataclass.UserWithoutPasswordDTO
import com.example.kitkat.api.models.tables.Followers
import com.example.kitkat.api.models.tables.UserFollowers
import com.example.kitkat.api.models.tables.Users
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class UserService() {

    init {
        transaction() {
            SchemaUtils.create(Users)
        }
    }

    suspend fun create(userDTO: UserDTO): Int = dbQuery {
        Users.insertAndGetId {
            it[name] = userDTO.name ?: "Unknown"
            it[email] = userDTO.email
            it[passwordHash] = hashPassword(userDTO.password)
            it[profilePictureUrl] = userDTO.profilePictureUrl
            it[bio] = userDTO.bio
            it[followersCount] = userDTO.followersCount ?: 0
            it[followingCount] = userDTO.followingCount ?: 0
        }.value
    }

    suspend fun read(id: Int): UserDTO? = dbQuery {
        Users.selectAll().where { Users.id eq id }
            .map {
                UserDTO(
                    id = it[Users.id].value,
                    name = it[Users.name],
                    email = it[Users.email],
                    password = it[Users.passwordHash],
                    profilePictureUrl = it[Users.profilePictureUrl],
                    bio = it[Users.bio],
                    followersCount = it[Users.followersCount],
                    followingCount = it[Users.followingCount]
                )
            }
            .singleOrNull()
    }

    suspend fun update(id: Int, userDTO: UserDTO): Boolean = dbQuery {
        Users.update({ Users.id eq id }) {
            it[name] = userDTO.name ?: "Unknown"
            it[email] = userDTO.email
            it[passwordHash] = hashPassword(userDTO.password)
            it[profilePictureUrl] = userDTO.profilePictureUrl
            it[bio] = userDTO.bio
            it[followersCount] = userDTO.followersCount ?: 0
            it[followingCount] = userDTO.followingCount ?: 0
        } > 0
    }

    suspend fun delete(id: Int): Boolean = dbQuery {
        Users.deleteWhere { Users.id eq id } > 0
    }

    suspend fun followUser(userId: Int, followerId: Int): Boolean = dbQuery {
        val user = UserDAO.findById(userId)
        val follower = UserDAO.findById(followerId)

        if (user != null && follower != null && userId != followerId) {
            // Ajouter la relation dans la table de liaison (many-to-many)
            Followers.insert {
                it[Followers.followed] = user.id
                it[Followers.follower] = follower.id
            }

            // Incrémenter les compteurs
            Users.update({ Users.id eq userId }) {
                with(SqlExpressionBuilder) {
                    it[followersCount] = Users.followersCount + 1
                }
            }
            true
        } else {
            false
        }
    }

    suspend fun listFollowers(userId: Int): List<UserWithoutPasswordDTO> {
        val user = UserDAO.findById(userId) ?: throw IllegalArgumentException("User not found")
        return user.followers.map {
            UserWithoutPasswordDTO(
                id = it.id.value,
                name = it.name,
                email = it.email,
                bio = it.bio,
                profilePictureUrl = it.profilePictureUrl,
                followersCount = it.followersCount,
                followingCount = it.followingCount
            )
        }
    }

    suspend fun addFollower(userId: Int, followerId: Int): Boolean = dbQuery {
        val userExists = Users.selectAll().where { Users.id eq userId }.count() > 0
        val followerExists = Users.selectAll().where { Users.id eq followerId }.count() > 0

        if (userExists && followerExists && userId != followerId) {
            Users.update({ Users.id eq userId }) {
                with(SqlExpressionBuilder) {
                    it[followersCount] = Users.followersCount + 1
                }
            }
            Users.update({ Users.id eq userId }) {
                with(SqlExpressionBuilder) {
                    it[followingCount] = Users.followingCount + 1
                }
            }
            true
        } else {
            false
        }
    }

    suspend fun removeFollower(userId: Int, followerId: Int): Boolean = dbQuery {
        val userExists = Users.selectAll().where { Users.id eq userId }.count() > 0
        val followerExists = Users.selectAll().where { Users.id eq followerId }.count() > 0

        if (userExists && followerExists && userId != followerId) {
            Users.update({ Users.id eq userId }) {
                it[followersCount] = Users.followersCount + 1
            }
            Users.update({ Users.id eq followerId }) {
                it[followingCount] = Users.followingCount + 1
            }
            true
        } else {
            false
        }
    }

    suspend fun getFollowing(userId: Int): List<UserWithoutPasswordDTO> = dbQuery {
        // Trouve tous les followers que l'utilisateur suit
        UserFollowers
            .selectAll().where { UserFollowers.user eq userId }
            .mapNotNull { row ->
                val followingId = row[UserFollowers.follower]
                UserDAO.findById(followingId)?.let { user ->
                    UserWithoutPasswordDTO(
                        id = user.id.value,
                        name = user.name,
                        email = user.email,
                        profilePictureUrl = user.profilePictureUrl,
                        bio = user.bio,
                        followersCount = user.followersCount,
                        followingCount = user.followingCount
                    )
                }
            }
    }

    suspend fun authenticate(email: String, password: String): UserDAO? = dbQuery {
        UserDAO.find { Users.email eq email }
            .firstOrNull()
            ?.takeIf { verifyPassword(password, it.passwordHash) }
    }

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    private fun hashPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    private fun verifyPassword(password: String, passwordHash: String): Boolean {
        return BCrypt.verifyer().verify(password.toCharArray(), passwordHash).verified
    }
}
