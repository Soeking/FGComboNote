package net.soeki.fcn.database

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.api.ExposedBlob

object Test : Table() {
    val id: Column<Int> = integer("id")
    val name: Column<String> = varchar("name", 50)
    val active: Column<Boolean> = bool("active")
}

object Character : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 50)
    val order: Column<Int> = integer("order").default(0)

    override val primaryKey = PrimaryKey(id)
}

object GameVersion : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val version: Column<String> = varchar("name", 50)

    override val primaryKey = PrimaryKey(id)
}

object ComboDetail : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val characterId: Column<Int> = integer("character_id") references Character.id
    val recipe: Column<String> = varchar("recipe", 200) // コンボレシピ
    val damage: Column<Int> = integer("damage") // ダメージ
    val situation: Column<String> = varchar("situation", 50) // 始動状況
    val description: Column<String> = varchar("description", 200) // 説明・備考
    val video: Column<ExposedBlob?> = blob("video").nullable()

    override val primaryKey = PrimaryKey(id)
}

object ComboVersion : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val comboId: Column<Int> = integer("combo_id") references ComboDetail.id
    val versionId: Column<Int> = integer("version_id") references GameVersion.id

    override val primaryKey = PrimaryKey(id)
}
