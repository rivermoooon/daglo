package com.moonsu.assignment.domain

sealed interface DataResource<out T> {
    data object Loading : DataResource<Nothing>
    data class Success<T>(val data: T) : DataResource<T>
    data class Error(val throwable: Throwable) : DataResource<Nothing>
}
