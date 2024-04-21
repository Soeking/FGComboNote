package net.soeki.fcn.query

import net.soeki.fcn.Character
import net.soeki.fcn.GameVersion
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

fun createCharacter(newCharacter: Character) {
    Character.insert {
        it[name] = newCharacter.name
        it[order] = newCharacter.order
    }
}

fun readAllCharacters(): List<ResultRow> {
    return Character.selectAll().orderBy(Character.order, SortOrder.ASC).toList()
}

fun updateCharacter(newCharacter: Character) {
    Character.update({ Character.id eq newCharacter.id }) {
        it[name] = newCharacter.name
        it[order] = newCharacter.order
    }
}

fun deleteCharacter(id: Int) {
    Character.deleteWhere { Character.id eq id }
}

fun createVersion(newVersion: String) {
    GameVersion.insert {
        it[version] = newVersion
    }
}

fun readAllVersion(): List<ResultRow> {
    return GameVersion.selectAll().orderBy(GameVersion.version, SortOrder.ASC).toList()
}

fun updateVersion(newGameVersion: GameVersion) {
    GameVersion.update({ GameVersion.id eq newGameVersion.id }) {
        it[version] = newGameVersion.version
    }
}

fun deleteVersion(id: Int) {
    GameVersion.deleteWhere { GameVersion.id eq id }
}
