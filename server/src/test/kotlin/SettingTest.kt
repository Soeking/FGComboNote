import io.kotest.core.spec.style.FunSpec
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldNotBe
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import net.soeki.fcn.module

class SettingTest : FunSpec({
    test("check root") {
        testApplication {
            application {
                module()
            }

            val response = client.get("/")
            response shouldHaveStatus HttpStatusCode.OK
        }
    }

    test("get versions") {
        testApplication {
            application {
                module()
            }

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            val response = client.get("/setting/versions")
            response shouldHaveStatus HttpStatusCode.OK

            val data = response.body<List<GameVersionData>>()
            data shouldNotBe null
        }
    }

    test("get characters") {
        testApplication {
            application {
                module()
            }

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            val response = client.get("/setting/characters")
            response shouldHaveStatus HttpStatusCode.OK

            val data = response.body<List<GameCharacterData>>()
            data shouldNotBe null
        }
    }

    test("post create character") {
        testApplication {
            application {
                module()
            }

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            val createResponse = client.post("/setting/update-character") {
                contentType(ContentType.Application.Json)
                setBody(GameCharacterData(0, "test", 0))
            }
            val failResponse = client.post("/setting/update-character") {
                contentType(ContentType.Application.Json)
                setBody(Pair(0, "test"))
            }
            val charactersResponse = client.get("/setting/characters")
            val oldData = charactersResponse.body<List<GameCharacterData>>().first()
            val updateResponse = client.post("/setting/update-character") {
                contentType(ContentType.Application.Json)
                setBody(GameCharacterData(oldData.id, "rewrite", 1))
            }

            println(oldData)
            createResponse shouldHaveStatus HttpStatusCode.OK
            failResponse shouldHaveStatus HttpStatusCode.BadRequest
            updateResponse shouldHaveStatus HttpStatusCode.OK
        }
    }

    test("post create version") {
        testApplication {
            application {
                module()
            }

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            val createResponse = client.post("/setting/update-version") {
                contentType(ContentType.Application.Json)
                setBody(GameVersionData(0, "0.0.1"))
            }
            val failResponse = client.post("/setting/update-version") {
                contentType(ContentType.Application.Json)
                setBody(Pair(0, "test"))
            }
            val versionsResponse = client.get("/setting/versions")
            val oldData = versionsResponse.body<List<GameVersionData>>().first()
            val updateResponse = client.post("/setting/update-version") {
                contentType(ContentType.Application.Json)
                setBody(GameVersionData(oldData.id, "0.0.2"))
            }

            println(oldData)
            createResponse shouldHaveStatus HttpStatusCode.OK
            failResponse shouldHaveStatus HttpStatusCode.BadRequest
            updateResponse shouldHaveStatus HttpStatusCode.OK
        }
    }

    test("delete character") {
        testApplication {
            application {
                module()
            }

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            val beforeResponse = client.get("/setting/characters")
            val newData = beforeResponse.body<List<GameCharacterData>>().maxBy { it.id }
//            val response = client.delete("/setting/delete-character/${oldData.id}")
            val afterResponse = client.get("/setting/characters")

//            response shouldHaveStatus HttpStatusCode.OK
            afterResponse.body<List<GameCharacterData>>() shouldNotContain newData
        }
    }

    test("delete version") {
        testApplication {
            application {
                module()
            }

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            val beforeResponse = client.get("/setting/versions")
            val newData = beforeResponse.body<List<GameVersionData>>().maxBy { it.id }
//            val response = client.delete("/setting/delete-version/${oldData.id}")
            val afterResponse = client.get("/setting/versions")

//            response shouldHaveStatus HttpStatusCode.OK
            afterResponse.body<List<GameVersionData>>() shouldNotContain newData
        }
    }
})
