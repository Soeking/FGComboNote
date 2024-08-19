package net.soeki.fcn

import GameCharacterData
import GameVersionData
import net.soeki.fcn.database.*

fun createOrUpdateCharacter(gameCharacterData: GameCharacterData) {
    if (gameCharacterData.id == 0)
        createCharacter(gameCharacterData)
    else
        updateCharacter(gameCharacterData)
}

fun createOrUpdateVersion(gameVersionData: GameVersionData) {
    if (gameVersionData.id ==0)
        createVersion(gameVersionData)
    else
        updateVersion(gameVersionData)
}
