package net.soeki.fcn.database

import ComboDetailData
import ComboListInfo
import ComboVersionName
import ComboVideoData
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction

fun createComboDetail(comboDetail: ComboDetailData): Int {
    return transaction {
        ComboDetail.insertAndGetId {
            it[characterId] = comboDetail.characterId
            it[recipe] = comboDetail.recipe
            it[damage] = comboDetail.damage
            it[situation] = comboDetail.situation
            it[description] = comboDetail.description
        }.run { value }
    }
}

fun createComboVideo(comboVideoData: ComboVideoData): Int {
    return transaction {
        ComboVideo.insertAndGetId {
            it[comboId] = comboVideoData.comboId
            it[video] = ExposedBlob(comboVideoData.video)
        }.run { value }
    }
}

fun getComboList(character: Int, version: Int): List<ComboListInfo> {
    return transaction {
        val query = ComboDetail.select(ComboDetail.id, ComboDetail.recipe, ComboDetail.damage)
        if (character != 0)
            query.andWhere { ComboDetail.characterId eq character }
        if (version != 0)
            query.andWhere {
                exists(
                    ComboVersion.selectAll()
                        .where { (ComboVersion.versionId eq version) and (ComboVersion.comboId eq ComboDetail.id) })
            }
        query.orderBy(ComboDetail.characterId, SortOrder.ASC).map {
            ComboListInfo(
                it[ComboDetail.id].value, it[ComboDetail.recipe], it[ComboDetail.damage]
            )
        }
    }
}

fun getComboDetail(id: Int): ComboDetailData {
    return transaction {
        ComboDetail.selectAll().adjustWhere { ComboDetail.id eq id }.limit(1).map {
            ComboDetailData(
                it[ComboDetail.id].value,
                it[ComboDetail.characterId],
                it[ComboDetail.recipe],
                it[ComboDetail.damage],
                it[ComboDetail.situation],
                it[ComboDetail.description]
            )
        }.first()
    }
}

fun getComboVideoIds(id: Int): List<Int> {
    return transaction {
        ComboVideo.select(ComboVideo.id)
            .where { ComboVideo.comboId eq id }
            .orderBy(ComboVideo.id, SortOrder.ASC)
            .map {
                it[ComboVideo.id].value
            }
    }
}

fun getComboVideo(id: Int): ByteArray {
    return transaction {
        ComboVideo.select(ComboVideo.video)
            .where { ComboVideo.id eq id }
            .map {
                it[ComboVideo.video].bytes
            }
            .first()
    }
}

fun updateComboDetail(comboDetail: ComboDetailData) {
    transaction {
        ComboDetail.update({ ComboDetail.id eq comboDetail.id }) {
            it[characterId] = comboDetail.characterId
            it[recipe] = comboDetail.recipe
            it[damage] = comboDetail.damage
            it[situation] = comboDetail.situation
            it[description] = comboDetail.description
        }
    }
}

fun deleteComboDetail(id: Int) {
    transaction {
        ComboDetail.deleteWhere { ComboDetail.id eq id }
    }
}

fun deleteComboVideo(id: Int) {
    transaction {
        ComboVideo.deleteWhere { ComboVideo.id eq id }
    }
}

fun deleteComboVideosByComboId(id: Int) {
    transaction {
        ComboVideo.deleteWhere { ComboVideo.comboId eq id }
    }
}

fun createComboVersion(combo: Int, version: Int) {
    transaction {
        ComboVersion.insert {
            it[comboId] = combo
            it[versionId] = version
        }
    }
}

fun getVersionIdsByCombo(comboId: Int): List<ComboVersionName> {
    return transaction {
        ComboVersion.join(
            GameVersion,
            JoinType.INNER,
            onColumn = ComboVersion.versionId,
            otherColumn = GameVersion.id
        ).select(
            ComboVersion.id,
            ComboVersion.comboId,
            ComboVersion.versionId,
            GameVersion.version
        ).where { ComboVersion.comboId eq comboId }.withDistinct().map {
            ComboVersionName(
                it[ComboVersion.id],
                it[ComboVersion.comboId].value,
                it[ComboVersion.versionId],
                it[GameVersion.version]
            )
        }
    }
}

fun deleteComboVersion(id: Int) {
    transaction {
        ComboVersion.deleteWhere { ComboVersion.id eq id }
    }
}

fun deleteComboVersionsByComboId(id: Int) {
    transaction {
        ComboVersion.deleteWhere { ComboVersion.comboId eq id }
    }
}
