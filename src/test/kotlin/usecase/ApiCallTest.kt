package usecase

import Resource
import extension.ifFailure
import extension.ifSuccess
import extension.mapError
import extension.toResource
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals

/**
 * @author Irineu A. Silva
 * Testing use case in API call
 */
@ExtendWith(MockKExtension::class)
class ApiCallTest {

    @MockK
    lateinit var service: Service

    @MockK(relaxUnitFun = true)
    lateinit var logger: Logger

    @Test
    fun `api call using kotlin catching`() {

        // given

        every { service.get() } returns
                Response(Data) andThenThrows
                Throwable("api error")

        val repository = Repository(service, logger)

        // then

        assertEquals(
            Resource.Result.Success(Data),
            repository.get()
        )

        verify { logger.send("success: $Data") }

        assertEquals(
            Resource.Result.Failure("api error"),
            repository.get()
        )

        verify { logger.send("error: api error") }
    }

    class Repository(
        private val service: Service,
        private val logger: Logger
    ) {
        fun get(): Resource.Result<Data, String> {
            val result = runCatching {
                service.get().data
            }.toResource()

            return result.ifSuccess {
                logger.send("success: $it")
            }.mapError {
                it.message ?: ""
            }.ifFailure {
                logger.send("error: $it")
            }
        }
    }

    interface Logger {
        fun send(log: String)
    }

    interface Service {
        fun get(): Response
    }

    data class Response(
        val data: Data
    )

    data object Data
}