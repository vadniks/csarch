package t2

import java.rmi.Remote
import java.rmi.RemoteException
import java.rmi.registry.LocateRegistry
import java.rmi.server.UnicastRemoteObject
import kotlin.jvm.Throws
import kotlin.math.sqrt

interface Calc : Remote
{ @Throws(RemoteException::class) fun calc(a: Float, b: Float, c: Float): Pair<Float, Float?> }

class CalcImpl : Calc { override fun calc(a: Float, b: Float, c: Float): Pair<Float, Float?> {
    val d = a * a - 4 * a * c
    return (-b + sqrt(d)) / (2 * a) to if (d > 0) (-b - sqrt(d)) / (2 * a) else null
} }

fun main() = LocateRegistry.createRegistry(1099).bind(
    Calc::class.simpleName,
    UnicastRemoteObject.exportObject(CalcImpl(), 0)
)
