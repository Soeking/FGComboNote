package net.soeki.fcn.query

import ComboDetailData
import net.soeki.fcn.ComboDetail
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

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
