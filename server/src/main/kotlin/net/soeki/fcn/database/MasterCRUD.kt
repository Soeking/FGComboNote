package net.soeki.fcn.database

import CharacterData
import GameVersionData
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

fun createCharacter(newCharacter: Character) {
    Character.insert {
        it[name] = newCharacter.name
        it[order] = newCharacter.order
    }
}

fun getAllCharacters(): List<CharacterData> {
    return Character.selectAll().orderBy(Character.order, SortOrder.ASC).map {
        CharacterData(
            it[Character.id],
            it[Character.name],
            it[Character.order]
        )
    }
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

fun getAllVersion(): List<GameVersionData> {
    return GameVersion.selectAll().orderBy(GameVersion.version, SortOrder.ASC).map {
        GameVersionData(it[GameVersion.id], it[GameVersion.version])
    }
}

fun updateVersion(newGameVersion: GameVersion) {
    GameVersion.update({ GameVersion.id eq newGameVersion.id }) {
        it[version] = newGameVersion.version
    }
}

fun deleteVersion(id: Int) {
    GameVersion.deleteWhere { GameVersion.id eq id }
}
