import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldNotContain
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import net.soeki.fcn.module
import java.io.File
import kotlin.test.assertEquals

class NoteTest : FunSpec({
    test("get combo list filtered by character or version") {
        testApplication {
            application {
                module()
            }

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }

            val characterResponse = client.get("/setting/characters")
            val oldestCharacterId = characterResponse.body<List<GameCharacterData>>().minOf { it.id }
            val versionResponse = client.get("/setting/versions")
            val oldestVersionId = versionResponse.body<List<GameVersionData>>().minOf { it.id }

            val allListResponse = client.get("/note/list/${0}/${0}")
            val filteredByCharacterResponse = client.get("/note/list/${oldestCharacterId}/${0}")
            val filteredByVersionResponse = client.get("/note/list/${0}/${oldestVersionId}")

            println(allListResponse.body<List<ComboListInfo>>())
            println(oldestCharacterId)
            println(filteredByCharacterResponse.body<String>())
            println(oldestVersionId)
            println(filteredByVersionResponse.body<String>())
            allListResponse shouldHaveStatus HttpStatusCode.OK
            filteredByCharacterResponse shouldHaveStatus HttpStatusCode.OK
            filteredByVersionResponse shouldHaveStatus HttpStatusCode.OK
        }
    }

    test("get combo detail by id") {
        testApplication {
            application {
                module()
            }

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            val allListResponse = client.get("/note/list/${0}/${0}")
            val oldestId = allListResponse.body<List<ComboListInfo>>().minOf { it.id }
            val detailResponse = client.get("/note/detail/${oldestId}")

            println(detailResponse.body<ComboDetailData>())
            detailResponse shouldHaveStatus HttpStatusCode.OK
        }
    }

    test("get versions by combo id") {
        testApplication {
            application {
                module()
            }

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            val allListResponse = client.get("/note/list/${0}/${0}")
            val oldestId = allListResponse.body<List<ComboListInfo>>().minOf { it.id }
            val versionListResponse = client.get("/note/version-list/${oldestId}")

            println(versionListResponse.body<List<ComboVersionName>>())
            versionListResponse shouldHaveStatus HttpStatusCode.OK
        }
    }

    test("get video by id") {
        testApplication {
            application {
                module()
            }

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            val allListResponse = client.get("/note/list/${0}/${0}")
            val oldestId = allListResponse.body<List<ComboListInfo>>().minOf { it.id }
            val videoListResponse = client.get("/note/video-id-list/${oldestId}")
            val videoId = videoListResponse.body<List<Int>>().first()
            val response = client.get("/note/video/${videoId}")
            val rangeResponse = client.get("/note/video/${videoId}") {
                headers {
                    append("Range", "bytes=0-100000")
                }
            }

            val file = File("./src/test/resources/download.mp4")
            file.writeBytes(response.body<ByteArray>())

            println(videoListResponse.body<List<Int>>())
            videoListResponse shouldHaveStatus HttpStatusCode.OK
            response shouldHaveStatus HttpStatusCode.OK
            rangeResponse shouldHaveStatus HttpStatusCode.PartialContent
        }
    }

    test("post create combo") {
        testApplication {
            application {
                module()
            }

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            val charactersResponse = client.get("/setting/characters")
            val oldCharacter = charactersResponse.body<List<GameCharacterData>>().minOf { it.id }
            val response = client.post("/note/update-combo") {
                contentType(ContentType.Application.Json)
                setBody(
                    ComboWithVideo(
                        ComboDetailData(0, oldCharacter, "recipe", 1000, "situation", "memo"),
                        listOf(
                            File("./src/test/resources/sample.mp4").readBytes(),
                            File("./src/test/resources/sample2.mp4").readBytes()
                        )
                    )
                )
            }
            val allListResponse = client.get("/note/list/${0}/${0}")
            val oldestId = allListResponse.body<List<ComboListInfo>>().minOf { it.id }
            val detailResponse = client.get("/note/detail/${oldestId}")
            val updateResponse = client.post("/note/update-combo") {
                contentType(ContentType.Application.Json)
                setBody(
                    ComboWithVideo(
                        ComboDetailData(oldestId, oldCharacter, "rewrite", 2000, "rewrite", "rewrite"),
                        emptyList()
                    )
                )
            }

            println(detailResponse)
            response shouldHaveStatus HttpStatusCode.OK
            updateResponse shouldHaveStatus HttpStatusCode.OK
        }
    }

    test("post upload video") {
        testApplication {
            application {
                module()
            }

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            val allListResponse = client.get("/note/list/${0}/${0}")
            val oldestId = allListResponse.body<List<ComboListInfo>>().minOf { it.id }
            val beforeVideoListResponse = client.get("/note/video-list/${oldestId}")
            val response = client.post("/note/upload-combo-video") {
                contentType(ContentType.Application.Json)
                setBody(ComboVideoData(oldestId, File("./src/test/resources/sample.mp4").readBytes()))
            }
            val afterVideoListResponse = client.get("/note/video-list/${oldestId}")

            response shouldHaveStatus HttpStatusCode.OK
            assertEquals(
                beforeVideoListResponse.body<List<ByteArray>>().size + 1,
                afterVideoListResponse.body<List<ByteArray>>().size
            )
        }
    }

    test("post create combo version mapping") {
        testApplication {
            application {
                module()
            }

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            val versionResponse = client.get("/setting/versions")
            val version = versionResponse.body<List<GameVersionData>>().minOf { it.id }
            val allListResponse = client.get("/note/list/${0}/${0}")
            val combo = allListResponse.body<List<ComboListInfo>>().minOf { it.id }
            val beforeVersionsResponse = client.get("/note/version-list/${combo}")
            val response = client.post("/note/create-combo-version/${combo}/${version}")
            val afterVersionsResponse = client.get("/note/version-list/${combo}")

            println(afterVersionsResponse.body<List<ComboVersionName>>())
            response shouldHaveStatus HttpStatusCode.OK
            assertEquals(
                beforeVersionsResponse.body<List<ComboVersionName>>() + 1,
                afterVersionsResponse.body<List<ComboVersionName>>()
            )
        }
    }

    test("delete combo by id") {
        testApplication {
            application {
                module()
            }

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            val beforeListResponse = client.get("/note/list/${0}/${0}")
            val newCombo = beforeListResponse.body<List<ComboListInfo>>().maxOf { it.id }
            val response = client.delete("/note/delete-combo/${newCombo}")
            val afterListResponse = client.get("/note/list/${0}/${0}")

            println(newCombo)
            response shouldHaveStatus HttpStatusCode.OK
            afterListResponse.body<List<ComboListInfo>>().map { it.id } shouldNotContain newCombo
        }
    }

    test("delete video by id") {
        testApplication {
            application {
                module()
            }

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            val allListResponse = client.get("/note/list/${0}/${0}")
            val oldestId = allListResponse.body<List<ComboListInfo>>().minOf { it.id }
            val videoListResponse = client.get("/note/video-id-list/${oldestId}")
            val videoId = videoListResponse.body<List<Int>>().max()
            val response = client.delete("/note/delete-combo-video/${videoId}")
            val afterListResponse = client.get("/note/video-id-list/${oldestId}")

            println(videoId)
            response shouldHaveStatus HttpStatusCode.OK
            afterListResponse.body<List<Int>>() shouldNotContain videoId
        }
    }

    test("delete version mapping by id") {
        testApplication {
            application {
                module()
            }

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            val comboListResponse = client.get("/note/list/${0}/${0}")
            val oldCombo = comboListResponse.body<List<ComboListInfo>>().minOf { it.id }
            val versionListResponse = client.get("/note/version-list/${oldCombo}")
            val newVersionMapping = versionListResponse.body<List<ComboVersionName>>().maxOf { it.id }
            val response = client.delete("/note/delete-combo-version/${newVersionMapping}")
            val afterListResponse = client.get("/note/version-list/${oldCombo}")

            println(versionListResponse.body<String>())
            println(afterListResponse.body<String>())
            response shouldHaveStatus HttpStatusCode.OK
            afterListResponse.body<List<ComboVersionName>>().map { it.id } shouldNotContain newVersionMapping
        }
    }
})
