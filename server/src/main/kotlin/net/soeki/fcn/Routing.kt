package net.soeki.fcn

import CharacterData
import ComboDetailData
import GameVersionData
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
                val character = call.receive<CharacterData>()
                createOrUpdateCharacter(character)
            }
            delete("/delete-character/{id}") {
                // url
                call.parameters["id"]?.let {
                    deleteCharacter(it.toInt())
                }
            }
            get("/versions") {
                call.respond(getAllVersion())
            }
            post("/update-version") {
                // body
                val version = call.receive<GameVersionData>()
                createOrUpdateVersion(version)
            }
            delete("/delete-version/{id}") {
                // url
                call.parameters["id"]?.let {
                    deleteVersion(it.toInt())
                }
            }
        }
        route("/note") {
            get("/list/{character}/{version}") {
                // url
                val character = call.parameters["character"]
                val version = call.parameters["version"]
                getComboList(character?.toInt(), version?.toInt()).let {
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
            post("/update-combo") {
                // body
                val comboData = call.receive<ComboDetailData>()
                createOrUpdateComboDetail(comboData)
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
                    deleteComboDetail(it.toInt())
                }
            }
            delete("/delete-combo-version/{id}") {
                // url comboId
                call.parameters["id"]?.let {
                    deleteComboVersion(it.toInt())
                }
            }
        }
    }
}
