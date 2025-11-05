package org.kotlined.cc.api.v1

import org.junit.Test
import org.kotlined.cc.api.v1.models.*
import kotlin.test.assertContains
import kotlin.test.assertEquals

class SerializationTest {
    private val request = TicketCreateRequest(
        requestType = "create",
        ticket = TicketCreateObject(
            title = "Where is my mind?",
            description = "Wheeeresmymind",
            priority = TicketPriority.MEDIUM
        )
    )
    /*
    private val request = TicketGetRequest(
        requestType = "get",
        ticket = TicketGetObject(
            id = "12345"
        )
    )
     */

    @Test
    fun `request is serialized correctly`() {
        val jsonString = apiV1Mapper.writeValueAsString(request)
        print(jsonString)
        assertContains(jsonString, """"title":"Where is my mind?"""")
        assertContains(jsonString, """"description":"Wheeeresmymind"""")
        assertContains(jsonString, """"priority":"medium"""")
        assertContains(jsonString, """"requestType":"create"""")
    }

    @Test
    fun `request is deserialized correctly`() {
        val jsonString = apiV1Mapper.writeValueAsString(request)
        val deserialized = apiV1Mapper.readValue(jsonString, IRequest::class.java)
        assertEquals(request, deserialized)
    }
}