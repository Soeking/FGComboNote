package net.soeki.fcn.database

import GameCharacterData
import GameVersionData
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun createCharacter(newCharacter: GameCharacterData) {
    transaction {
        Character.insert {
            it[name] = newCharacter.name
            it[order] = newCharacter.order
        }
    }
}

fun getAllCharacters(): List<GameCharacterData> {
    return transaction {
        Character.selectAll().orderBy(Character.order, SortOrder.ASC).map {
            GameCharacterData(
                it[Character.id],
                it[Character.name],
                it[Character.order]
            )
        }
    }
}

fun updateCharacter(newCharacter: GameCharacterData) {
    transaction {
        Character.update({ Character.id eq newCharacter.id }) {
            it[name] = newCharacter.name
            it[order] = newCharacter.order
        }
    }
}

fun deleteCharacter(id: Int) {
    transaction {
        Character.deleteWhere { Character.id eq id }
    }
}

fun createVersion(newVersion: GameVersionData) {
    transaction {
        GameVersion.insert {
            it[version] = newVersion.version
        }
    }
}

fun getAllVersion(): List<GameVersionData> {
    return transaction {
        GameVersion.selectAll().orderBy(GameVersion.version, SortOrder.ASC).map {
            GameVersionData(it[GameVersion.id], it[GameVersion.version])
        }
    }
}

fun getLatestVersion(): Int {
    return transaction {
        GameVersion.select(GameVersion.id).orderBy(GameVersion.id, SortOrder.DESC).limit(1).map {
            it[GameVersion.id]
        }.first()
    }
}

fun updateVersion(newGameVersion: GameVersionData) {
    transaction {
        GameVersion.update({ GameVersion.id eq newGameVersion.id }) {
            it[version] = newGameVersion.version
        }
    }
}

fun deleteVersion(id: Int) {
    transaction {
        GameVersion.deleteWhere { GameVersion.id eq id }
    }
}
