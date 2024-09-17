package net.soeki.fcn

import ComboVideoData
import ComboWithVideo
import io.ktor.http.*
import net.soeki.fcn.database.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import kotlin.math.min

fun createOrUpdateComboDetail(comboWithVideos: ComboWithVideo) {
    transaction {
        if (comboWithVideos.detailData.id == 0) {
            // create and connect with latest version
            val detailId = createComboDetail(comboWithVideos.detailData)

            val latestVersion = getLatestVersion()
            createComboVersion(detailId, latestVersion)
            comboWithVideos.videos.forEach {
                createComboVideo(ComboVideoData(detailId, it))
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

fun getVideoByRange(videoId: Int, requestRange: String?): Result<Pair<ByteArray, Boolean>> {
    val file = getComboVideo(videoId)
    if (requestRange == null)
        return Result.success(Pair(file, false))

    val rangesSpecifier = parseRangesSpecifier(requestRange) ?: return Result.failure(Exception())
    val range = rangesSpecifier.ranges.first()
    if (range is ContentRange.Bounded) {
        if (range.from <= file.size) {
            val end = min(range.to.toInt(), file.size)
            return Result.success(Pair(file.sliceArray(range.from.toInt()..end), true))
        }
    }
    return Result.failure(Exception())
}

fun deleteComboData(id: Int) {
    deleteComboVideosByComboId(id)
    deleteComboVersionsByComboId(id)
    deleteComboDetail(id)
}
