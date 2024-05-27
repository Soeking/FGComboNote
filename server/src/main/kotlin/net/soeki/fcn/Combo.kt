package net.soeki.fcn

import ComboDetailData
import net.soeki.fcn.database.*

fun createOrUpdateComboDetail(comboDetail: ComboDetailData) {
    if (comboDetail.id == 0) {
        // create and connect with latest version
        val detailId = createComboDetail(comboDetail)

        val latestVersion = getLatestVersion()
        createComboVersion(detailId, latestVersion)
    } else
        updateComboDetail(comboDetail)
}

fun createComboVersionWrapNull(combo: Int?, version: Int?) {
    if (combo == null || version == null)
        return
    createComboVersion(combo, version)
}
