package net.soeki.fcn.database

import ComboDetailData
import ComboListInfo
import ComboVersionName
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
            it[video] = ExposedBlob(comboDetail.video)
        }.run { value }
    }
}

fun getComboList(character: Int?, version: Int?): List<ComboListInfo> {
    return transaction {
        val query = ComboDetail.join(
            ComboVersion,
            JoinType.INNER,
            onColumn = ComboDetail.id,
            otherColumn = ComboVersion.comboId
        ).select(ComboDetail.id, ComboDetail.recipe, ComboDetail.damage)
        character?.let {
            query.andWhere { ComboDetail.characterId eq character }
        }
        version?.let {
            query.andWhere { ComboVersion.versionId eq version }
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
                it[ComboDetail.description],
                it[ComboDetail.video]?.bytes ?: ByteArray(0)
            )
        }.first()
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
            it[video] = ExposedBlob(comboDetail.video)
        }
    }
}

fun deleteComboDetail(id: Int) {
    transaction {
        ComboDetail.deleteWhere { ComboDetail.id eq id }
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
                it[ComboVersion.versionId],
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
