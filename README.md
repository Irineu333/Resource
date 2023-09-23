# Resource [![](https://jitpack.io/v/Irineu333/Resource.svg) ](https://jitpack.io/#Irineu333/Highlight)

Complete solution for handling success, failure, and loading states in Kotlin.

## Loading

Use the sealed class [`Resource`](src%2Fmain%2Fkotlin%2FResource.kt) with loading states.

``` kotlin
ordersRepository.flow.collect { resource ->
    when (resource) {
        is Resource.Result.Failure -> //...
        is Resource.Result.Success -> //...
        Resource.Loading -> //...
    }
}
```

## Result

If the possible states are success and error, the ideal class is `Resource.Result`.

``` kotlin
when (ordersRepository.getOrders()) {
    is Resource.Result.Failure -> //...
    is Resource.Result.Success -> //...
}
```

## Extensions

The library comes with some basi basic [extensions](src%2Fmain%2Fkotlin%2Fextension%2FResource.kt) to handle and
manipulate the states.

## kotlin.Result

Integration with kotlin Result.

``` kotlin
suspend fun getOrders(): Resource.Result<List<Order>, String> {
    val result = runCatching {
        service.getOrders()
    }.toResource()
    
    return result.mapError {
        it.message ?: ""
    }.ifFailure {
        logger.send("error: $it")
    }.ifSuccess {
        logger.send("success: $it")
    }
}
```

## Add to project (Gradle)

Add the jitpack to project in `build.gradle.kts` or `settings.gradle.kts`:

``` kotlin
repositories { 

    maven { url = uri("https://jitpack.io") }
}
```

Add the dependence to module:

``` kotlin
implementation("com.github.NeoUtils:Resource:{version}")
```