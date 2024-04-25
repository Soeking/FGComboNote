package net.soeki.fcn

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import net.soeki.fcn.query.getAllCharacters
import net.soeki.fcn.query.getAllVersion

fun Application.configureRouting() {
    routing {
        get("/"){
            call.respondText("welcome to combo note")
        }
        route("/setting") {
            get("/characters") {
                call.respond(getAllCharacters())
            }
            post("/update-character") {
                // create or update
            }
            get("/versions") {
                call.respond(getAllVersion())
            }
            post("/update-version") {
                // create or update

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
