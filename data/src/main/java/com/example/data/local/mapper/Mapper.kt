package com.example.data.local.mapper

interface Mapper<E, D> {
    fun mapFromEntity(e: E): D
    fun mapToEntity(d: D): E
}