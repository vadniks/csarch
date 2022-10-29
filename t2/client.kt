package t2

import java.rmi.registry.LocateRegistry

// x^2-x-1=0 -> 1.618034, -0.618034
fun main() = print((LocateRegistry
    .getRegistry()
    .lookup(Calc::class.simpleName) as Calc
).calc(1f, -1f, -1f))
