package com.moonsu.assignment.core.common

sealed class AppError : Throwable() {

    /**
     * 네트워크 연결 에러
     * - 인터넷 연결 끊김
     * - DNS 실패
     * - 네트워크 권한 없음
     */
    data class NetworkError(
        override val message: String = "네트워크 연결을 확인해주세요",
        override val cause: Throwable? = null,
    ) : AppError()

    /**
     * HTTP 서버 에러 (4xx, 5xx)
     * - 400 Bad Request
     * - 401 Unauthorized
     * - 404 Not Found
     * - 500 Internal Server Error
     */
    data class ServerError(
        val code: Int,
        override val message: String = "서버 오류가 발생했습니다",
        override val cause: Throwable? = null,
    ) : AppError()

    /**
     * 요청 타임아웃
     * - 연결 타임아웃
     * - 읽기 타임아웃
     * - 쓰기 타임아웃
     */
    data class TimeoutError(
        override val message: String = "요청 시간이 초과되었습니다",
        override val cause: Throwable? = null,
    ) : AppError()

    /**
     * 데이터 파싱 에러
     * - JSON 파싱 실패
     * - 데이터 형식 불일치
     */
    data class ParseError(
        override val message: String = "데이터 처리 중 오류가 발생했습니다",
        override val cause: Throwable? = null,
    ) : AppError()

    /**
     * 데이터를 찾을 수 없음 (404)
     * - 존재하지 않는 리소스
     */
    data class NotFoundError(
        val resourceType: String = "데이터",
        override val message: String = "${resourceType}를 찾을 수 없습니다",
        override val cause: Throwable? = null,
    ) : AppError()

    /**
     * 인증/인가 에러
     * - 로그인 필요
     * - 토큰 만료
     * - 권한 없음
     */
    data class AuthError(
        override val message: String = "인증이 필요합니다",
        override val cause: Throwable? = null,
    ) : AppError()

    /**
     * 알 수 없는 에러
     * - 예상하지 못한 에러
     */
    data class UnknownError(
        override val message: String = "알 수 없는 오류가 발생했습니다",
        override val cause: Throwable? = null,
    ) : AppError()

    /**
     * 사용자 친화적 메시지
     */
    fun getUserMessage(): String {
        return when (this) {
            is NetworkError ->
                "인터넷 연결을 확인하고\n다시 시도해주세요"

            is ServerError -> {
                if (code in 500..599) {
                    "서버에 일시적인 문제가 발생했습니다\n잠시 후 다시 시도해주세요"
                } else {
                    "요청을 처리할 수 없습니다\n잠시 후 다시 시도해주세요"
                }
            }

            is TimeoutError ->
                "요청 시간이 초과되었습니다\n네트워크 상태를 확인해주세요"

            is NotFoundError ->
                "$resourceType 를 찾을 수 없습니다"

            is AuthError ->
                "로그인이 필요한 서비스입니다"

            is ParseError ->
                "데이터를 불러오는 중 문제가 발생했습니다"

            is UnknownError ->
                "오류가 발생했습니다\n잠시 후 다시 시도해주세요"
        }
    }
}
