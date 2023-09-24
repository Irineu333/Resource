package usecase

import Resource
import extension.getOrElse
import extension.ifFailure
import extension.ifLoading
import extension.ifSuccess
import kotlin.test.Test
import kotlin.test.fail

/**
 * @author Irineu A. Silva
 * Testing cases where the inline modifier is useful to avoid regressions.
 */
class InlineTest {

    @Test
    fun `isSuccess, direct return when type is SUCCESS`() {

        val resource = getResourceType(ResourceType.SUCCESS)

        resource.ifSuccess { return }

        fail("did not return")
    }

    @Test
    fun `ifFailure, direct return when type is FAILURE`() {

        val resource = getResourceType(ResourceType.FAILURE)

        resource.ifFailure { return }

        fail("did not return")
    }

    @Test
    fun `ifLoading, direct return when type is LOADING`() {

        val resource = getResourceType(ResourceType.LOADING)

        resource.ifLoading { return }

        fail("did not return")
    }

    @Test
    fun `getOrElse, direct return when type is LOADING`() {

        val resource = getResourceType(ResourceType.LOADING)

        resource.getOrElse { return }

        fail("did not return")
    }

    @Test
    fun `getOrElse, direct return when type is FAILURE`() {

        val resource = getResourceType(ResourceType.FAILURE)

        resource.getOrElse { return }

        fail("did not return")
    }

    enum class ResourceType {
        SUCCESS,
        FAILURE,
        LOADING
    }

    private fun getResourceType(type: ResourceType) = when (type) {
        ResourceType.SUCCESS -> Resource.Result.Success(Unit)
        ResourceType.FAILURE -> Resource.Result.Failure(Unit)
        ResourceType.LOADING -> Resource.Loading
    }
}