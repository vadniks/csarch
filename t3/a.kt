package t3

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.System.currentTimeMillis
import java.net.ServerSocket
import java.net.Socket
import java.util.LinkedList
import java.util.Queue

class st(private val s: Socket, private val a: ArrayList<st>) : Thread() {
    private lateinit var p: PrintWriter
    private val q: Queue<String> = LinkedList()
    private var l = currentTimeMillis()

    override fun run() { try {
        val r = BufferedReader(InputStreamReader(s.getInputStream()))
        p = PrintWriter(s.getOutputStream(), true)

        var w = false
        while (true) {
            if (r.ready()) {
                val t = r.readLine()
                q.add(t)
                println("Received: $t")
                w = true
            }

            val h = currentTimeMillis()
            if (h - l >= 5000) {
                l = h
                if (!w) continue
                w = false

                println("Broadcasting...")
                var b: String?
                while (run { b = q.poll(); b } != null) a.forEach { it .p.println(b) }
            }
        }
    } catch (_: Exception) {} }
}

fun main() {
    val a = ArrayList<st>()
    ServerSocket(5000).use { s ->
        while (true) a.add(st(s.accept(), a).apply { start() })
    }
}
