package com.moonsu.assignment.data

import android.util.Log
import com.moonsu.assignment.data.DataErrorMapper.toAppError
import com.moonsu.assignment.domain.DataResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Flow를 DataResource로 감싸는 유틸리티 함수
 *
 * 자동으로 에러를 AppError로 변환하고 로깅합니다.
 *
 * @param context 에러 로깅용 컨텍스트
 * @param fetch 데이터를 가져오는 suspend 함수
 * @return DataResource로 감싸진 Flow
 */
inline fun <T> flowDataResource(
    context: String = "",
    crossinline fetch: suspend () -> T,
): Flow<DataResource<T>> = flow {
    emit(DataResource.Loading)
    try {
        val result = fetch()
        emit(DataResource.Success(result))
    } catch (e: Exception) {
        val appError = e.toAppError()
        Log.e("DataResource", "[$context] ${appError.message}", appError.cause)
        emit(DataResource.Error(appError))
    }
}
