package extension

import Resource

// basic

inline val Resource<*, *>.isLoading get() = this == Resource.Loading
inline val Resource<*, *>.isSuccess get() = this is Resource.Result.Success
inline val Resource<*, *>.isError get() = this is Resource.Result.Failure

fun <T, E> Resource.Result<T, E>.asResource() = this as Resource<T, E>

// map

inline fun <T, E, T2> Resource<T, E>.mapSuccess(
    transform: (T) -> T2
) = when (this) {
    is Resource.Result.Failure -> Resource.Result.Failure(error)
    is Resource.Result.Success -> Resource.Result.Success(transform(data))
    Resource.Loading -> Resource.Loading
}

inline fun <T, E, T2> Resource.Result<T, E>.mapSuccess(
    transform: (T) -> T2
) = when (this) {
    is Resource.Result.Failure -> Resource.Result.Failure(error)
    is Resource.Result.Success -> Resource.Result.Success(transform(data))
}

inline fun <T, E, E2> Resource<T, E>.mapError(
    transform: (E) -> E2
) = when (this) {
    is Resource.Result.Failure -> Resource.Result.Failure(transform(error))
    is Resource.Result.Success -> Resource.Result.Success(data)
    Resource.Loading -> Resource.Loading
}

inline fun <T, E, E2> Resource.Result<T, E>.mapError(
    transform: (E) -> E2
) = when (this) {
    is Resource.Result.Failure -> Resource.Result.Failure(transform(error))
    is Resource.Result.Success -> Resource.Result.Success(data)
}

// action

inline fun <T, E> Resource<T, E>.ifSuccess(
    onSuccess: (T) -> Unit,
) = apply {
    if (this is Resource.Result.Success) {
        onSuccess(data)
    }
}

inline fun <T, E> Resource<T, E>.ifFailure(
    onError: (E) -> Unit,
) = apply {
    if (this is Resource.Result.Failure) {
        onError(error)
    }
}

inline fun <T, E> Resource<T, E>.ifLoading(
    onLoading: () -> Unit,
) = apply {
    if (this == Resource.Loading) {
        onLoading()
    }
}

// getter

inline fun <R, T : R, E> Resource.Result<T, E>.getOrElse(
    onElse: (E) -> R,
) = when (this) {
    is Resource.Result.Failure -> onElse(error)
    is Resource.Result.Success -> data
}


inline fun <R, T : R> Resource<T, *>.getOrElse(onElse: () -> R): R {

    ifSuccess { return it }

    return onElse()
}


fun <T> Resource<T, *>.getOrNull() = getOrElse { null }

@Throws(IllegalStateException::class)
fun <T> Resource<T, *>.getOrThrow(
    message: String = "$this isn't success",
) = getOrElse { error(message) }

// parser

fun <T> Resource.Result<T, Throwable>.toResult() = when (this) {
    is Resource.Result.Success -> Result.success(data)
    is Resource.Result.Failure -> Result.failure(error)
}