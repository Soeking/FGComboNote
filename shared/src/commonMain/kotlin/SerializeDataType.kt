import kotlinx.serialization.Serializable

@Serializable
data class GameCharacterData(
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
    val description: String
)

@Serializable
data class ComboWithVideo(
    val detailData: ComboDetailData,
    val videos: List<ByteArray>
)

@Serializable
data class ComboVersionData(
    val id: Int,
    val comboId: Int,
    val versionId: Int
)

@Serializable
data class ComboListInfo(
    val id: Int,
    val recipe: String,
    val damage: Int
)

@Serializable
data class ComboVersionName(
    val id: Int, // comboVersion id
    val comboId: Int,
    val versionId: Int,
    val version: String
)

@Serializable
data class APIResult(
    val status: Int,
    val message: String
)
