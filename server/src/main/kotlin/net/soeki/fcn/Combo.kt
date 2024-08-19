package net.soeki.fcn

import ComboWithVideo
import net.soeki.fcn.database.*
import org.jetbrains.exposed.sql.transactions.transaction

fun createOrUpdateComboDetail(comboWithVideos: ComboWithVideo) {
    transaction {
        if (comboWithVideos.detailData.id == 0) {
            // create and connect with latest version
            val detailId = createComboDetail(comboWithVideos.detailData)

            val latestVersion = getLatestVersion()
            createComboVersion(detailId, latestVersion)
            comboWithVideos.videos.forEach {
                createComboVideo(detailId, it)
            }
        } else
            updateComboDetail(comboWithVideos.detailData)
    }
}

fun createComboVersionWrapNull(combo: Int?, version: Int?) {
    if (combo == null || version == null)
        return
    createComboVersion(combo, version)
}

fun deleteComboData(id: Int) {
    deleteComboVideosByComboId(id)
    deleteComboVersionsByComboId(id)
    deleteComboDetail(id)
}
