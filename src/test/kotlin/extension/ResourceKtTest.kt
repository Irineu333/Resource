@file:Suppress("IMPLICIT_NOTHING_TYPE_ARGUMENT_IN_RETURN_POSITION")

package extension

import Resource
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * @author Irineu A. Silva
 * Testing [Resource] extensions
 */
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

        // given

        val resultToMap = listOf(
            Resource.Result.Success(listOf("one")),
            Resource.Result.Failure("error"),
        )

        // then

        val resultMapped = resultToMap.map { result ->
            result.mapSuccess { data ->
                data + "two"
            }
        }

        // when

        resultMapped.forEach { resource ->
            when (resource) {
                is Resource.Result.Success -> {
                    assertEquals(listOf("one", "two"), resource.data)
                }

                is Resource.Result.Failure -> {
                    assertEquals("error", resource.error)
                }
            }
        }
    }

    @Test
    fun resource_mapSuccess() {

        // given

        val resourceToMap = listOf(
            Resource.Result.Success(listOf("one")),
            Resource.Result.Failure("error"),
            Resource.Loading
        )

        // when

        val resourceMapped = resourceToMap.map { resource ->
            resource.mapSuccess { data ->
                data + "two"
            }
        }

        // then

        resourceMapped.forEach { resource ->
            when (resource) {
                is Resource.Result.Success -> {
                    assertEquals(listOf("one", "two"), resource.data)
                }

                is Resource.Result.Failure -> {
                    assertEquals("error", resource.error)
                }

                Resource.Loading -> {
                    assertEquals(Resource.Loading, resource)
                }
            }
        }
    }

    @Test
    fun result_mapError() {

        // given

        val resultToMap = listOf(
            Resource.Result.Success("data"),
            Resource.Result.Failure(listOf("one"))
        )

        // when

        val resultMapped = resultToMap.map { result ->
            result.mapError { error ->
                error + "two"
            }
        }

        // then

        resultMapped.forEach { result ->
            when (result) {
                is Resource.Result.Success -> {
                    assertEquals("data", result.data)
                }

                is Resource.Result.Failure -> {
                    assertEquals(listOf("one", "two"), result.error)
                }
            }
        }
    }

    @Test
    fun resource_mapError() {

        // given

        val resourceToMap = listOf(
            Resource.Result.Success("data"),
            Resource.Result.Failure(listOf("one")),
            Resource.Loading
        )

        // when

        val resourceMapped = resourceToMap.map {
            it.mapError { error ->
                error + "two"
            }
        }

        // then

        resourceMapped.forEach { resource ->
            when (resource) {
                is Resource.Result.Success -> {
                    assertEquals("data", resource.data)
                }

                is Resource.Result.Failure -> {
                    assertEquals(listOf("one", "two"), resource.error)
                }

                Resource.Loading -> {
                    assertEquals(Resource.Loading, resource)
                }
            }
        }
    }

    @Test
    fun resource_ifSuccess() {

        val doCall: (Data) -> Unit = spyk()
        val notToCall: (Data) -> Unit = spyk()

        Resource.Result.Success(Data).asResource().ifSuccess(doCall)
        Resource.Result.Failure(Error).asResource().ifSuccess(notToCall)
        Resource.Loading.ifSuccess(notToCall)

        verify(atMost = 1) { doCall(Data) }
        verify(inverse = true) { notToCall(any()) }
    }

    @Test
    fun result_ifSuccess() {

        val doCall: (Data) -> Unit = spyk()
        val notToCall: (Data) -> Unit = spyk()

        Resource.Result.Success(Data).ifSuccess(doCall)
        Resource.Result.Failure(Error).ifSuccess(notToCall)

        verify(atMost = 1) { doCall(Data) }
        verify(inverse = true) { notToCall(any()) }
    }

    @Test
    fun resource_ifFailure() {

        val doCall: (Error) -> Unit = spyk()
        val notToCall: (Error) -> Unit = spyk()

        Resource.Result.Success(Data).asResource().ifFailure(notToCall)
        Resource.Result.Failure(Error).asResource().ifFailure(doCall)
        Resource.Loading.ifFailure(notToCall)

        verify(atMost = 1) { doCall(Error) }
        verify(inverse = true) { notToCall(any()) }
    }

    @Test
    fun result_ifFailure() {

        val doCall: (Error) -> Unit = spyk()
        val notToCall: (Error) -> Unit = spyk()

        Resource.Result.Success(Data).ifFailure(notToCall)
        Resource.Result.Failure(Error).ifFailure(doCall)

        verify(atMost = 1) { doCall(Error) }
        verify(inverse = true) { notToCall(any()) }
    }

    @Test
    fun resource_ifLoading() {

        val doCall: () -> Unit = spyk()
        val notToCall: () -> Unit = spyk()

        Resource.Result.Success(Data).asResource().ifLoading(notToCall)
        Resource.Result.Failure(Error).asResource().ifLoading(notToCall)
        Resource.Loading.ifLoading(doCall)

        verify(atMost = 1) { doCall() }
        verify(inverse = true) { notToCall() }
    }

    @Test
    fun getOrElse() {

        val doCall: () -> Unit = spyk()
        val notToCall: () -> Unit = spyk()

        Resource.Result.Success(Data).getOrElse(notToCall)
        Resource.Result.Failure(Error).getOrElse(doCall)
        Resource.Loading.getOrElse(doCall)

        verify(atMost = 2) { doCall() }
        verify(inverse = true) { notToCall() }
    }

    @Test
    fun getOrNull() {
        assertEquals(Data, Resource.Result.Success(Data).getOrNull())
        assertNull(Resource.Result.Failure(Error).getOrNull())
        assertNull(Resource.Loading.getOrNull())
    }

    @Test
    fun getOrThrow() {
        assertEquals(Data, Resource.Result.Success(Data).getOrThrow())
        assertThrows<Throwable> { Resource.Result.Failure(Unit).getOrThrow() }
        assertThrows<Throwable> { Resource.Loading.getOrThrow() }
    }

    @Test
    fun toResult() {
        assertEquals(
            Result.success(Data),
            Resource.Result.Success(Data).toResult()
        )

        val exception = Throwable("Test Error")

        assertEquals(
            Result.failure<Unit>(exception),
            Resource.Result.Failure(exception).toResult()
        )
    }

    data object Data
    data object Error
}