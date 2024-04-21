import kotlinx.serialization.Serializable

@Serializable
data class CharacterData(
    val id: Int,
    val name: String,
    val order: Int
)

@Serializable
data class GameVersionData(
    val id: Int,
    val version: String
)

@Serializable
data class ComboDetailData(
    val id: Int,
    val characterId: Int,
    val recipe: String,
    val damage: Int,
    val situation: String,
    val description: String,
    val video: ByteArray
)

@Serializable
data class ComboVersionData(
    val id: Int,
    val comboId: Int,
    val versionId: Int
)
