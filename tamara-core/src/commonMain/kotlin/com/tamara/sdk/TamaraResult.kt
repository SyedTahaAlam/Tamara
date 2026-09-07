package com.tamara.sdk

sealed interface TamaraResult<out T> {
    data class Success<T>(val value: T) : TamaraResult<T>
    data class Failure(val error: TamaraError) : TamaraResult<Nothing>
}

inline fun <T, R> TamaraResult<T>.map(transform: (T) -> R): TamaraResult<R> = when (this) {
    is TamaraResult.Success -> TamaraResult.Success(transform(value))
    is TamaraResult.Failure -> this
}

inline fun <T> TamaraResult<T>.onSuccess(block: (T) -> Unit): TamaraResult<T> {
    if (this is TamaraResult.Success) block(value)
    return this
}

inline fun <T> TamaraResult<T>.onFailure(block: (TamaraError) -> Unit): TamaraResult<T> {
    if (this is TamaraResult.Failure) block(error)
    return this
}
