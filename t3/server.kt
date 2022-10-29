package t3

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.System.currentTimeMillis
import java.net.ServerSocket
import java.net.Socket
import java.util.LinkedList
import java.util.Queue

class SocketThread(private val socket: Socket, private val threads: ArrayList<SocketThread>) : Thread() {
    private lateinit var writer: PrintWriter
    private val queue: Queue<String> = LinkedList()
    private var lastTimeStamp = currentTimeMillis()

    override fun run() { try {
        val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        writer = PrintWriter(socket.getOutputStream(), true)

        var flag = false
        while (true) {
            if (reader.ready()) {
                val line = reader.readLine()
                queue.add(line)
                println("Received: $line")
                flag = true
            }

            val millis = currentTimeMillis()
            if (millis - lastTimeStamp >= 5000) {
                lastTimeStamp = millis
                if (!flag) continue
                flag = false

                println("Broadcasting...")
                var temp: String?
                while (run { temp = queue.poll(); temp } != null) threads.forEach { it .writer.println(temp) }
            }
        }
    } catch (_: Exception) {} }
}

fun main() {
    val threads = ArrayList<SocketThread>()
    ServerSocket(5000).use { socket ->
        while (true) threads.add(SocketThread(socket.accept(), threads).apply { start() })
    }
}
