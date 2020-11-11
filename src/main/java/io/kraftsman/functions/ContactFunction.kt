package io.kraftsman.functions

import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.annotation.AuthorizationLevel
import com.microsoft.azure.functions.annotation.FunctionName
import com.microsoft.azure.functions.annotation.HttpTrigger
import io.kraftsman.services.ContactService
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.time.ExperimentalTime

class ContactFunction {

    @ExperimentalTime
    @FunctionName("ContactFunction")
    fun run(
            @HttpTrigger(
                    name = "req",
                    route = "contacts",
                    methods = [HttpMethod.GET],
                    authLevel = AuthorizationLevel.ANONYMOUS
            )
            request: HttpRequestMessage<Optional<String?>>,
            context: ExecutionContext
    ): HttpResponseMessage {

        context.logger.info("Java HTTP trigger processed a request.")

        val query = request.queryParameters["amount"]
        val amount = query?.toIntOrNull() ?: 10

        val contacts = ContactService().generate(amount)
        val jsonString = Json.encodeToString(mapOf("data" to contacts))

        return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(jsonString)
                .build()
    }
}
