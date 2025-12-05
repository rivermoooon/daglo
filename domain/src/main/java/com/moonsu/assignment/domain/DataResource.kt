package com.moonsu.assignment.domain

import com.moonsu.assignment.core.common.AppError

sealed interface DataResource<out T> {
    data object Loading : DataResource<Nothing>
    data class Success<T>(val data: T) : DataResource<T>
    data class Error(val error: AppError) : DataResource<Nothing>
}
