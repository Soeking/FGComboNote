package net.soeki.fcn

import CharacterData
import GameVersionData
import net.soeki.fcn.database.*

fun createOrUpdateCharacter(characterData: CharacterData) {
    if (characterData.id == 0)
        createCharacter(characterData)
    else
        updateCharacter(characterData)
}

fun createOrUpdateVersion(gameVersionData: GameVersionData) {
    if (gameVersionData.id ==0)
        createVersion(gameVersionData)
    else
        updateVersion(gameVersionData)
}
