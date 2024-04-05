package net.soeki.fcn

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun connectDB() {
    val url = System.getenv("DB_URL")
    val user = System.getenv("DB_USER")
    val pass = System.getenv("DB_PASS")

    if (url == null || user == null || pass == null)
        throw Exception("not found environment variable about db")
    Database.connect("jdbc:$url", driver = "org.postgresql.Driver", user = user, password = pass)
}

fun createTables() {
    connectDB()
    transaction {
        SchemaUtils.create(Character, GameVersion, ComboDetail, ComboVersion)
    }
}

fun readTest() {
    transaction {
        Test.selectAll().toList().forEach {
            println("${it[Test.id]} ${it[Test.name]} ${it[Test.active]}")
        }
    }
}
