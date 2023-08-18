package extension

import Resource
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

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
    }

    @Test
    fun ifFailure() {
    }

    @Test
    fun ifLoading() {
    }

    @Test
    fun getOrElse() {
    }

    @Test
    fun testGetOrElse() {
    }

    @Test
    fun getOrNull() {
    }

    @Test
    fun getOrThrow() {
    }

    @Test
    fun toResult() {
    }
}