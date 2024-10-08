package net.soeki.fcn

import APIResult
import ComboVideoData
import GameCharacterData
import ComboWithVideo
import GameVersionData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.soeki.fcn.database.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("welcome to combo note")
        }
        route("/setting") {
            get("/characters") {
                call.respond(getAllCharacters())
            }
            post("/update-character") {
                // body
                try {
                    val character = call.receive<GameCharacterData>()
                    createOrUpdateCharacter(character)
                    call.response.status(HttpStatusCode.OK)
                    call.respond(APIResult(HttpStatusCode.OK.value, "success"))
                } catch (e: Exception) {
                    call.response.status(HttpStatusCode.BadRequest)
                    call.respond(APIResult(HttpStatusCode.BadRequest.value, e.message ?: "failed"))
                }
            }
            delete("/delete-character/{id}") {
                // url
                call.parameters["id"]?.let {
                    deleteCharacter(it.toInt())
                    call.response.status(HttpStatusCode.OK)
                    call.respond(APIResult(HttpStatusCode.OK.value, "success"))
                }
            }
            get("/versions") {
                call.respond(getAllVersion())
            }
            post("/update-version") {
                // body
                try {
                    val version = call.receive<GameVersionData>()
                    createOrUpdateVersion(version)
                    call.response.status(HttpStatusCode.OK)
                    call.respond(APIResult(HttpStatusCode.OK.value, "success"))
                } catch (e: Exception) {
                    call.response.status(HttpStatusCode.BadRequest)
                    call.respond(APIResult(HttpStatusCode.BadRequest.value, e.message ?: "failed"))
                }
            }
            delete("/delete-version/{id}") {
                // url
                call.parameters["id"]?.let {
                    deleteVersion(it.toInt())
                    call.response.status(HttpStatusCode.OK)
                    call.respond(APIResult(HttpStatusCode.OK.value, "success"))
                }
            }
        }
        route("/note") {
            get("/list/{character}/{version}") {
                // url
                val character = call.parameters["character"]
                val version = call.parameters["version"]
                getComboList(character?.toInt() ?: 0, version?.toInt() ?: 0).let {
                    call.respond(it)
                }
            }
            get("/detail/{id}") {
                // url comboId
                call.parameters["id"]?.let {
                    call.respond(getComboDetail(it.toInt()))
                }
            }
            get("/version-list/{id}") {
                // url comboId
                call.parameters["id"]?.let {
                    call.respond(getVersionIdsByCombo(it.toInt()))
                }
            }
            get("/video-id-list/{id}") {
                // url comboId
                call.parameters["id"]?.let {
                    call.respond(getComboVideoIds(it.toInt()))
                }
            }
            get("/video/{id}") {
                // url videoId
                // header range
                val range = call.request.headers["Range"]
                call.parameters["id"]?.let {
                    val videoResult = getVideoByRange(it.toInt(), range)
                    if (videoResult.isSuccess) {
                        val video = videoResult.getOrNull()!!
                        if (video.second) {
                            video.third?.run {
                                call.response.headers.append("Content-Range", "bytes ${first}-${second}/${third}")
                            }
                            call.respondBytes(video.first, ContentType.Video.MP4, HttpStatusCode.PartialContent) {}
                        } else {
                            call.response.headers.append("Accept-Ranges", "bytes")
                            call.respondBytes(video.first, ContentType.Video.MP4, HttpStatusCode.OK) {}
                        }
                    } else {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
            }
            post("/update-combo") {
                // body
                try {
                    val comboData = call.receive<ComboWithVideo>()
                    createOrUpdateComboDetail(comboData)
                    call.response.status(HttpStatusCode.OK)
                    call.respond(APIResult(HttpStatusCode.OK.value, "success"))
                } catch (e: Exception) {
                    call.response.status(HttpStatusCode.BadRequest)
                    call.respond(APIResult(HttpStatusCode.BadRequest.value, e.message ?: "failed"))
                }
            }
            post("/upload-combo-video") {
                // body
                try {
                    val comboVideoData = call.receive<ComboVideoData>()
                    createComboVideo(comboVideoData)
                    call.response.status(HttpStatusCode.OK)
                    call.respond(APIResult(HttpStatusCode.OK.value, "success"))
                } catch (e: Exception) {
                    call.response.status(HttpStatusCode.BadRequest)
                    call.respond(APIResult(HttpStatusCode.BadRequest.value, e.message ?: "failed"))
                }
            }
            post("/create-combo-version/{combo}/{version}") {
                // url
                val combo = call.parameters["combo"]
                val version = call.parameters["version"]
                createComboVersionWrapNull(combo?.toInt(), version?.toInt())
            }
            delete("/delete-combo/{id}") {
                // url comboId
                call.parameters["id"]?.let {
                    deleteComboData(it.toInt())
                    call.response.status(HttpStatusCode.OK)
                    call.respond(APIResult(HttpStatusCode.OK.value, "success"))
                }
            }
            delete("/delete-combo-video/{id}") {
                // url id
                call.parameters["id"]?.let {
                    deleteComboVideo(it.toInt())
                    call.response.status(HttpStatusCode.OK)
                    call.respond(APIResult(HttpStatusCode.OK.value, "success"))
                }
            }
            delete("/delete-combo-version/{id}") {
                // url comboId
                call.parameters["id"]?.let {
                    deleteComboVersion(it.toInt())
                    call.response.status(HttpStatusCode.OK)
                    call.respond(APIResult(HttpStatusCode.OK.value, "success"))
                }
            }
        }
    }
}
