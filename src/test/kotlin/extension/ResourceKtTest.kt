package extension

import Resource
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class ResourceKtTest {

    @Test
    fun isLoading() {
        val resource = Resource.Loading

        assertTrue(resource.isLoading)
        assertFalse(resource.isSuccess)
        assertFalse(resource.isError)
    }

    @Test
    fun isSuccess() {
        val resource = Resource.Result.Success(Unit)

        assertFalse(resource.isLoading)
        assertTrue(resource.isSuccess)
        assertFalse(resource.isError)
    }

    @Test
    fun isError() {
        val resource = Resource.Result.Failure(Unit)

        assertFalse(resource.isLoading)
        assertFalse(resource.isSuccess)
        assertTrue(resource.isError)
    }

    @Test
    fun result_mapSuccess() {
        listOf(
            Pair(Resource.Result.Success(listOf("one")), listOf("one", "two")),
            Pair(Resource.Result.Failure("error"), "error"),
        ).map {
            it.copy(
                first = it.first.mapSuccess { data ->
                    data + "two"
                }
            )
        }.forEach { (resource, expected) ->
            when (resource) {
                is Resource.Result.Success -> {
                    assertEquals(expected, resource.data)
                }

                is Resource.Result.Failure -> {
                    assertEquals(expected, resource.error)
                }
            }
        }
    }

    @Test
    fun resource_mapSuccess() {
        listOf(
            Pair(Resource.Result.Success(listOf("one")), listOf("one", "two")),
            Pair(Resource.Result.Failure("error"), "error"),
            Pair(Resource.Loading, "loading"),
        ).map {
            it.copy(
                first = it.first.mapSuccess { data ->
                    data + "two"
                }
            )
        }.forEach { (resource, expected) ->
            when (resource) {
                is Resource.Result.Success -> {
                    assertEquals(expected, resource.data)
                }

                is Resource.Result.Failure -> {
                    assertEquals(expected, resource.error)
                }

                Resource.Loading -> {
                    assertEquals(expected, "loading")
                }
            }
        }
    }

    @Test
    fun result_mapError() {
        listOf(
            Pair(Resource.Result.Success("data"), "data"),
            Pair(Resource.Result.Failure(listOf("one")), listOf("one", "two")),
        ).map {
            it.copy(
                first = it.first.mapError { data ->
                    data + "two"
                }
            )
        }.forEach { (resource, expected) ->
            when (resource) {
                is Resource.Result.Success -> {
                    assertEquals(expected, resource.data)
                }

                is Resource.Result.Failure -> {
                    assertEquals(expected, resource.error)
                }
            }
        }
    }

    @Test
    fun resource_mapError() {
        listOf(
            Pair(Resource.Result.Success("data"), "data"),
            Pair(Resource.Result.Failure(listOf("one")), listOf("one", "two")),
            Pair(Resource.Loading, "loading"),
        ).map {
            it.copy(
                first = it.first.mapError { data ->
                    data + "two"
                }
            )
        }.forEach { (resource, expected) ->
            when (resource) {
                is Resource.Result.Success -> {
                    assertEquals(expected, resource.data)
                }

                is Resource.Result.Failure -> {
                    assertEquals(expected, resource.error)
                }

                Resource.Loading -> {
                    assertEquals(expected, "loading")
                }
            }
        }
    }

    @Test
    fun ifSuccess() {

        var onSuccessCalled = false

        Resource.Result.Success("test data").ifSuccess {
            assertEquals("test data", it)
            onSuccessCalled = true
        }

        assertTrue(onSuccessCalled)

        listOf(
            Resource.Result.Failure(Unit),
            Resource.Loading
        ).forEach { resource ->
            resource.ifSuccess {
                fail("${resource::class.java.simpleName} don't must call onSuccess")
            }
        }
    }

    @Test
    fun ifFailure() {
        var onFailureCalled = false

        Resource.Result.Failure("test error").ifFailure {
            assertEquals("test error", it)
            onFailureCalled = true
        }

        assertTrue(onFailureCalled)

        listOf(
            Resource.Result.Success(Unit),
            Resource.Loading
        ).forEach { resource ->
            resource.ifFailure {
                fail("${resource::class.java.simpleName} don't must call onFailure")
            }
        }
    }

    @Test
    fun ifLoading() {
        var onLoadingCalled = false

        Resource.Loading.ifLoading {
            onLoadingCalled = true
        }

        assertTrue(onLoadingCalled)

        listOf(
            Resource.Result.Success(Unit),
            Resource.Result.Failure(Unit)
        ).forEach { resource ->
            resource.ifLoading {
                fail("${resource::class.java.simpleName} don't must call onLoading")
            }
        }
    }

    @Test
    fun getOrElse() {

        assertEquals(
            "test data",
            Resource.Result.Success("test data").getOrElse {
                fail("getOrElse don't must call onElse when success")
            },
            "getOrElse must return data when success",
        )

        var failureOnElse = false

        Resource.Result.Failure(Unit).getOrElse {
            failureOnElse = true
        }

        var loadingOnElse = false

        Resource.Loading.getOrElse {
            loadingOnElse = true
        }

        assertTrue(failureOnElse)
        assertTrue(loadingOnElse)
    }

    @Test
    fun getOrNull() {
        listOf(
            Pair(Resource.Result.Success("test data"), "test data"),
            Pair(Resource.Result.Failure("test error"), null),
            Pair(Resource.Loading, null),
        ).forEach { (resource, expected) ->
            assertEquals(expected, resource.getOrNull())
        }
    }

    @Test
    fun getOrThrow() {
        assertEquals(
            "test data",
            Resource.Result.Success("test data").getOrThrow(),
            "getOrThrow must return data when success"
        )

        assertThrows<Throwable> {
            Resource.Result.Failure(Unit).getOrThrow()
        }

        assertThrows<Throwable> {
            Resource.Loading.getOrThrow()
        }
    }

    @Test
    fun toResult() {
        assertEquals(
            Result.success("test data"),
            Resource.Result.Success("test data").toResult()
        )

        val exception = Throwable("Test Error")

        assertEquals(
            Result.failure<Unit>(exception),
            Resource.Result.Failure(exception).toResult()
        )
    }
}