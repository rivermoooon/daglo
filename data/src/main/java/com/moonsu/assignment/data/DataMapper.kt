package com.moonsu.assignment.data

interface DataMapper<Domain> {
    fun toDomain(): Domain
}

fun <E : DataMapper<D>, D> List<E>.toDomainList(): List<D> = map { it.toDomain() }
