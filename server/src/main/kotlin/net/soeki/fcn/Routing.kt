package net.soeki.fcn

import CharacterData
import GameVersionData
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.soeki.fcn.database.deleteCharacter
import net.soeki.fcn.database.deleteVersion
import net.soeki.fcn.database.getAllCharacters
import net.soeki.fcn.database.getAllVersion

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
                val version=call.receive<GameVersionData>()
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
            get("/list") {

            }
            get("/detail/{id}") {

            }
            post("/update-combo") {
                // create or update

            }
        }
    }
}
