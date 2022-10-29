import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

val lock = ReentrantLock()
val condition: Condition = lock.newCondition()
var flag = false

fun main() {
    val thread1 = Thread { run(false) }
    val thread2 = Thread { run(true) }
    thread1.start()
    thread2.start()
}

fun run(which: Boolean) { for (i in 0..3) { lock.withLock {
    while (if (!which) flag else !flag) condition.await()
    println((if (!flag) "ping" else "pong") + ' ' + Thread.currentThread().name)
    flag = !flag
    condition.signal()
} } }
