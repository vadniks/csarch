package a

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import a.plugins.*

fun main() { embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
//    cr()
    configureSockets()
    configureRouting()
}.start(wait = true) }
