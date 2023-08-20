sealed class Resource<out T, out E> {

    sealed class Result<out T, out E> : Resource<T, E>() {

        data class Success<T>(val data: T) : Result<T, Nothing>()

        data class Failure<E>(val error: E) : Result<Nothing, E>()
    }

    data object Loading : Resource<Nothing, Nothing>()
}