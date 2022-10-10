package a

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

val db by lazy { Database.connect("jdbc:sqlite:file:db.db", "org.sqlite.JDBC") }

object a : IntIdTable() { val a = varchar("a", 10) }

fun gt(c: String, b: (String) -> Unit) = transaction(db) { b(a.select { a.a match c }.single()[a.a]) }

fun cr() = transaction(db) {
    SchemaUtils.create(a)
    a.insert { it[a] = "a" }
    a.insert { it[a] = "b" }
    a.insert { it[a] = "c" }
}

suspend fun <T> q(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }
