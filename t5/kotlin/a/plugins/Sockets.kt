package a.plugins

import a.gt
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing { webSocket("/ws") {
        for (f in incoming) if (f is Frame.Text)
            when (f.readText()) {
                "a" -> gt("a") { runBlocking { outgoing.send(Frame.Text(it)) } }
                "b" -> gt("b") { runBlocking { outgoing.send(Frame.Text(it)) } }
                else -> close(CloseReason(CloseReason.Codes.NORMAL, "close"))
            }
    } }
}
