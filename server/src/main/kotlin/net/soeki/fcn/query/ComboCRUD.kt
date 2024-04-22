package net.soeki.fcn.query

import ComboDetailData
import net.soeki.fcn.ComboDetail
import net.soeki.fcn.ComboVersion
import net.soeki.fcn.GameVersion
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

fun createComboDetail(comboDetail: ComboDetail) {
    ComboDetail.insert {
        it[characterId] = comboDetail.characterId
        it[recipe] = comboDetail.recipe
        it[damage] = comboDetail.damage
        it[situation] = comboDetail.situation
        it[description] = comboDetail.description
        it[video] = comboDetail.video
    }
}

fun readComboDetail(id: Int): ComboDetailData {
    return transaction {
        ComboDetail.selectAll().adjustWhere { ComboDetail.id eq id }.limit(1)
    }.map {
        ComboDetailData(
            it[ComboDetail.id],
            it[ComboDetail.characterId],
            it[ComboDetail.recipe],
            it[ComboDetail.damage],
            it[ComboDetail.situation],
            it[ComboDetail.description],
            it[ComboDetail.video]?.bytes ?: ByteArray(0)
        )
    }.first()
}

fun updateComboDetail(comboDetail: ComboDetail) {
    ComboDetail.update({ ComboDetail.id eq comboDetail.id }) {
        it[characterId] = comboDetail.characterId
        it[recipe] = comboDetail.recipe
        it[damage] = comboDetail.damage
        it[situation] = comboDetail.situation
        it[description] = comboDetail.description
        it[video] = comboDetail.video
    }
}

fun deleteComboDetail(id: Int) {
    ComboDetail.deleteWhere { ComboDetail.id eq id }
}

fun createComboVersion(comboVersion: ComboVersion) {
    ComboVersion.insert {
        it[comboId] = comboVersion.comboId
        it[versionId] = comboVersion.versionId
    }
}

fun getComboIdsByVersion(version: Int): List<Int> {
    return ComboVersion.select(ComboVersion.comboId).where { ComboVersion.versionId eq version }.withDistinct()
        .map { it[ComboVersion.comboId] }
}

fun getVersionIdsByCombo(comboId: Int): List<Int> {
    return ComboVersion.select(ComboVersion.versionId).where { ComboVersion.comboId eq comboId }.withDistinct()
        .map { it[ComboVersion.versionId] }
}

fun deleteComboVersion(id: Int) {
    ComboVersion.deleteWhere { ComboVersion.id eq id }
}
