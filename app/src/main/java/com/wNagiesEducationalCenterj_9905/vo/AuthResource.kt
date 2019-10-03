package com.wNagiesEducationalCenterj_9905.vo

enum class AuthStatus { LOADING, AUTHENTICATED, ERROR, LOG_OUT }
data class AuthResource<out T>(val status: AuthStatus, val data: T?, val message: String?) {
    companion object{
        fun <T> loading(data: T?): AuthResource<T> {
            return AuthResource(AuthStatus.LOADING, data, null)
        }

        fun <T> authenticated(data: T?): AuthResource<T> {
            return AuthResource(AuthStatus.AUTHENTICATED, data, null)
        }

        fun <T> error(message: String?,data:T?): AuthResource<T> {
            return AuthResource(AuthStatus.ERROR, data, message)
        }

        fun <T> logout(data:T?): AuthResource<T> {
            return AuthResource(AuthStatus.LOG_OUT, data, null)
        }
    }

}