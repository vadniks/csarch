package a

import org.jetbrains.annotations.TestOnly
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

val db by lazy { Database.connect("jdbc:sqlite:file:db2.db", "org.sqlite.JDBC") }

object Table : IntIdTable() { val char = varchar("char", 10) }

fun get(which: String, action: (String) -> Unit) = transaction(db) { action(Table.select { Table.char match which }.single()[Table.char]) }

@TestOnly @Suppress("unused")
fun create() = transaction(db) {
    SchemaUtils.create(Table)
    Table.insert { it[char] = "a" }
    Table.insert { it[char] = "b" }
    Table.insert { it[char] = "c" }
}
