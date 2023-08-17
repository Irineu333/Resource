import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ResourceTest {

    @Test
    fun successResult_shouldHoldData() {
        val success = Resource.Result.Success("Test Data")
        assertEquals("Test Data", success.data)
    }

    @Test
    fun failureResult_shouldHoldError() {
        val failure = Resource.Result.Failure("Test Error")
        assertEquals("Test Error", failure.error)
    }

    @Test
    fun loadingResource_shouldBeLoading() {
        assertEquals(Resource.Loading, Resource.Loading)
    }
}