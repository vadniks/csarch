package t2

import java.rmi.Remote
import java.rmi.RemoteException
import java.rmi.registry.LocateRegistry
import java.rmi.server.UnicastRemoteObject
import kotlin.jvm.Throws
import kotlin.math.sqrt

interface a : Remote
{ @Throws(RemoteException::class) fun a(a: Float, b: Float, c: Float): Pair<Float, Float?> }

class b : a { override fun a(a: Float, b: Float, c: Float): Pair<Float, Float?> {
    val d = a * a - 4 * a * c
    return (-b + sqrt(d)) / (2 * a) to if (d > 0) (-b - sqrt(d)) / (2 * a) else null
} }

fun main() = LocateRegistry.createRegistry(1099).bind(
    a::class.simpleName,
    UnicastRemoteObject.exportObject(b(), 0)
)
