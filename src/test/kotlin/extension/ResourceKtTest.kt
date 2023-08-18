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
    fun asResource() {
        Resource.Result.Success(Unit).asResource()
        Resource.Result.Failure(Unit).asResource()
    }

    @Test
    fun mapSuccess() {
    }

    @Test
    fun mapError() {
    }

    @Test
    fun testMapSuccess() {
    }

    @Test
    fun testMapError() {
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