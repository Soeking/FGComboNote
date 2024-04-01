package net.soeki.fcn

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class DBManage {
}

object Test:Table(){
    val id:Column<Int> = integer("id")
    val name:Column<String> = varchar("name",50)
    val active:Column<Boolean> = bool("active")
}

fun connectDB(){
    val url = System.getenv("DB_URL")
    val user = System.getenv("DB_USER")
    val pass = System.getenv("DB_PASS")

    if (url == null || user == null || pass == null)
        throw Exception("not found environment variable about db")
    Database.connect("jdbc:$url", driver = "org.postgresql.Driver", user = user, password = pass)
}

fun readTest(){
    transaction {
        Test.selectAll().toList().forEach {
            println("${it[Test.id]} ${it[Test.name]} ${it[Test.active]}")
        }
    }
}
