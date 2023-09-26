package extension

import Resource
import org.junit.jupiter.api.Assertions.assertEquals
import util.model.ErrorTest
import kotlin.test.Test

class ResultKtTest {

    @Test
    fun successResult_toResource() {

        val result = Result.success("Test Data").toResource()

        assertEquals(Resource.Result.Success("Test Data"), result)
    }

    @Test
    fun failureResult_toResource() {

        val result = Result.failure<Nothing>(ErrorTest("message")).toResource()

        assertEquals(Resource.Result.Failure(ErrorTest("message")), result)
    }
}