package extension

fun <T> Result<T>.toResource(): Resource.Result<T, Throwable> {
    return Resource.Result.Success(
        getOrElse {
            return Resource.Result.Failure(it)
        }
    )
}