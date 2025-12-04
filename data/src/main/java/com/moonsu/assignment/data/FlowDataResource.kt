package com.moonsu.assignment.data

import android.util.Log
import com.moonsu.assignment.domain.DataResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Flow를 DataResource로 감싸는 유틸리티 함수
 * Loading → Success/Error 순서로 emit
 */
inline fun <T> flowDataResource(
    crossinline fetch: suspend () -> T,
): Flow<DataResource<T>> = flow {
    emit(DataResource.Loading)
    try {
        val result = fetch()
        emit(DataResource.Success(result))
    } catch (e: Exception) {
        Log.e("FlowDataResource", "Error fetching data", e)
        emit(DataResource.Error(e))
    }
}
