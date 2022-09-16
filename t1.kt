import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

val a = ReentrantLock()
val e: Condition = a.newCondition()
var d = false

/*
ping Thread-0
pong Thread-1
ping Thread-0
pong Thread-1
ping Thread-0
pong Thread-1
ping Thread-0
pong Thread-1
 */
fun main() {
    val b = Thread { a(false) }
    val c = Thread { a(true) }
    b.start()
    c.start()
}

fun a(f: Boolean) { for (i in 0..3) { a.withLock {
    while (if (!f) d else !d) e.await()
    println((if (!d) "ping" else "pong") + ' ' + Thread.currentThread().name)
    d = !d
    e.signal()
} } }
