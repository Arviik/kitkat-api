package com.example.kitkat.api.services

interface Service<T> {

    fun create(t: T): Int

    fun read(id: Int): T?

    fun update(id: Int, t: T)

    fun delete(id: Int)

    //fun daoToModel(dao: T): T
}
