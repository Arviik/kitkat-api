package com.example.kitkat.api.models.repository

interface Repository<T> {
    suspend fun all(): List<T>
    suspend fun byId(id: Int): T?
    suspend fun add(model: T)
    suspend fun update(id: Int, model: T): Boolean
    suspend fun remove(id: Int): Boolean
}
