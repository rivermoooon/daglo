package com.moonsu.assignment.data

import com.moonsu.assignment.core.common.AppError
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object DataErrorMapper {

    /**
     * Throwable을 AppError로 변환
     *
     * @return AppError 타입으로 변환된 에러
     */
    fun Throwable.toAppError(): AppError {
        // 이미 AppError면 그대로 반환
        if (this is AppError) {
            return this
        }

        return when (this) {
            // 네트워크 연결 에러
            is UnknownHostException -> AppError.NetworkError(
                message = "인터넷 연결을 확인해주세요",
                cause = this,
            )

            is IOException -> AppError.NetworkError(
                message = "네트워크 오류가 발생했습니다",
                cause = this,
            )

            // HTTP 에러
            is HttpException -> {
                val code = this.code()
                when (code) {
                    401 -> AppError.AuthError(
                        message = "로그인이 필요합니다",
                        cause = this,
                    )

                    403 -> AppError.AuthError(
                        message = "접근 권한이 없습니다",
                        cause = this,
                    )

                    404 -> AppError.NotFoundError(
                        resourceType = "요청하신 데이터",
                        message = "요청하신 데이터를 찾을 수 없습니다",
                        cause = this,
                    )

                    in 400..499 -> AppError.ServerError(
                        code = code,
                        message = "잘못된 요청입니다 (오류 코드: $code)",
                        cause = this,
                    )

                    in 500..599 -> AppError.ServerError(
                        code = code,
                        message = "서버에서 오류가 발생했습니다 (오류 코드: $code)",
                        cause = this,
                    )

                    else -> AppError.ServerError(
                        code = code,
                        message = "서버 통신 중 오류가 발생했습니다 (오류 코드: $code)",
                        cause = this,
                    )
                }
            }

            // 타임아웃 에러
            is SocketTimeoutException -> AppError.TimeoutError(
                message = "요청 시간이 초과되었습니다\n네트워크 상태를 확인해주세요",
                cause = this,
            )

            // 그 외 모든 에러
            else -> AppError.UnknownError(
                message = this.message ?: "알 수 없는 오류가 발생했습니다",
                cause = this,
            )
        }
    }
}
