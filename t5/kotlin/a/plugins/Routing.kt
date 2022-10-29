package a.plugins

import io.ktor.server.routing.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.coroutines.runBlocking

fun Application.configureRouting() { routing {
    get("/a") {
        a.get("c") { runBlocking { call.respondText(it) } }
    }
    static {
        resource("/", "static/index.html")
        resources("static")
    }
} }
