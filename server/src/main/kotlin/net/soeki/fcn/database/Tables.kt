package net.soeki.fcn.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.api.ExposedBlob

object Character : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val name: Column<String> = varchar("name", 50)
    val order: Column<Int> = integer("order").default(0)

    override val primaryKey = PrimaryKey(id)
}

object GameVersion : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val version: Column<String> = varchar("version", 50)

    override val primaryKey = PrimaryKey(id)
}

object ComboDetail : IntIdTable() {
    val characterId: Column<Int> = integer("character_id") references Character.id
    val recipe: Column<String> = varchar("recipe", 200) // コンボレシピ
    val damage: Column<Int> = integer("damage") // ダメージ
    val situation: Column<String> = varchar("situation", 50) // 始動状況
    val description: Column<String> = varchar("description", 200) // 説明・備考
}

object ComboVideo : IntIdTable() {
    val comboId = reference("combo_id", ComboDetail, onDelete = ReferenceOption.CASCADE)
    val video: Column<ExposedBlob> = blob("video")
}

object ComboVersion : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val comboId = reference("combo_id", ComboDetail, onDelete = ReferenceOption.CASCADE)
    val versionId: Column<Int> = integer("version_id") references GameVersion.id

    override val primaryKey = PrimaryKey(id)
}
