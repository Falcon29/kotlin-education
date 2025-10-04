package org.kotlined.cc.app.ktor
//
//import io.ktor.client.plugins.websocket.*
//import io.ktor.serialization.kotlinx.*
//import io.ktor.server.testing.*
//import kotlinx.coroutines.withTimeout
//import kotlin.test.Test
//import kotlin.test.assertEquals
//import kotlin.test.assertIs
//
//class V2WebsocketStubTest {
//
//    @Test
//    fun createStub() {
//        val request = AdCreateRequest(
//            ad = AdCreateObject(
//                title = "Болт",
//                description = "КРУТЕЙШИЙ",
//                adType = DealSide.DEMAND,
//                visibility = AdVisibility.PUBLIC,
//            ),
//            debug = AdDebug(
//                mode = AdRequestDebugMode.STUB,
//                stub = AdRequestDebugStubs.SUCCESS
//            )
//        )
//
//        testMethod<IResponse>(request) {
//            assertEquals(ResponseResult.SUCCESS, it.result)
//        }
//    }
//
//    @Test
//    fun readStub() {
//        val request = AdReadRequest(
//            ad = AdReadObject("666"),
//            debug = AdDebug(
//                mode = AdRequestDebugMode.STUB,
//                stub = AdRequestDebugStubs.SUCCESS
//            )
//        )
//
//        testMethod<IResponse>(request) {
//            assertEquals(ResponseResult.SUCCESS, it.result)
//        }
//    }
//
//    @Test
//    fun updateStub() {
//        val request = AdUpdateRequest(
//            ad = AdUpdateObject(
//                id = "666",
//                title = "Болт",
//                description = "КРУТЕЙШИЙ",
//                adType = DealSide.DEMAND,
//                visibility = AdVisibility.PUBLIC,
//            ),
//            debug = AdDebug(
//                mode = AdRequestDebugMode.STUB,
//                stub = AdRequestDebugStubs.SUCCESS
//            )
//        )
//
//        testMethod<IResponse>(request) {
//            assertEquals(ResponseResult.SUCCESS, it.result)
//        }
//    }
//
//    @Test
//    fun deleteStub() {
//        val request = AdDeleteRequest(
//            ad = AdDeleteObject(
//                id = "666",
//            ),
//            debug = AdDebug(
//                mode = AdRequestDebugMode.STUB,
//                stub = AdRequestDebugStubs.SUCCESS
//            )
//        )
//
//        testMethod<IResponse>(request) {
//            assertEquals(ResponseResult.SUCCESS, it.result)
//        }
//    }
//
//    @Test
//    fun searchStub() {
//        val request = AdSearchRequest(
//            adFilter = AdSearchFilter(),
//            debug = AdDebug(
//                mode = AdRequestDebugMode.STUB,
//                stub = AdRequestDebugStubs.SUCCESS
//            )
//        )
//
//        testMethod<IResponse>(request) {
//            assertEquals(ResponseResult.SUCCESS, it.result)
//        }
//    }
//
//    @Test
//    fun offersStub() {
//        val request = AdOffersRequest(
//            ad = AdReadObject(
//                id = "666",
//            ),
//            debug = AdDebug(
//                mode = AdRequestDebugMode.STUB,
//                stub = AdRequestDebugStubs.SUCCESS
//            )
//        )
//
//        testMethod<IResponse>(request) {
//            assertEquals(ResponseResult.SUCCESS, it.result)
//        }
//    }
//
//    private inline fun <reified T> testMethod(
//        request: IRequest,
//        crossinline assertBlock: (T) -> Unit
//    ) = testApplication {
//        application { module(MkplAppSettings(corSettings = MkplCorSettings())) }
//        val client = createClient {
//            install(WebSockets) {
//                contentConverter = KotlinxWebsocketSerializationConverter(apiV2Mapper)
//            }
//        }
//
//        client.webSocket("/v2/ws") {
//            withTimeout(3000) {
//                val response = receiveDeserialized<IResponse>() as T
//                assertIs<AdInitResponse>(response)
//            }
//            sendSerialized(request)
//            withTimeout(3000) {
//                val response = receiveDeserialized<IResponse>() as T
//                assertBlock(response)
//            }
//        }
//    }
//}
