package id.gdev.regist.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.opencsv.CSVReaderBuilder
import com.opencsv.CSVWriterBuilder
import id.gdev.regist.BuildConfig
import id.gdev.regist.domain.model.Participant
import id.gdev.regist.utils.TimeUtils.formatFile
import java.io.File
import java.io.FileWriter
import java.io.InputStreamReader

fun Context.readCSV(uri: Uri): List<Array<String>> {
    return try {
        val csvFile = contentResolver.openInputStream(uri)
        val values: MutableList<Array<String>> =
            CSVReaderBuilder(InputStreamReader(csvFile)).build().readAll()
        values.toList()
    } catch (e: Exception) {
        Toast.makeText(this, "Error Read File", Toast.LENGTH_SHORT).show()
        listOf()
    }
}

object CsvField {
    const val IsAttended = "Is Attended"
    const val CheckInDate = "Checkin Date (UTC)"
    const val IsRegular = "Regular Role"
    const val TicketNumber = "Ticket number"
    const val UserId = "User ID"
    const val AttendedId = "Attendee ID"
}

fun Context.shareCsv(eventName: String, listParticipant: List<Participant>) {
    val csvFile = File(getStorageDirectory(), eventName.toFileName.plus(".csv"))
    val writer = CSVWriterBuilder(FileWriter(csvFile)).withSeparator('\t').build()
    val header = listParticipant.first().fullData.keys.reversed().sorted().toMutableList()
        .apply { add(CsvField.IsAttended) }.toTypedArray()
    writer.writeNext(header)
    listParticipant.subList(1, listParticipant.size - 1).forEach { participant ->
        val sortedMap = mutableMapOf<String, String>()
        participant.fullData.entries.sortedBy { it.key }.forEach { sortedMap[it.key] = it.value }
            .also {
                sortedMap[CsvField.CheckInDate] =
                    if (participant.checkIn == true) participant.lastCheckInTime?.formatFile
                        ?: "-" else "-"
                sortedMap[CsvField.IsAttended] =
                    (participant.checkIn ?: false).toString().uppercase()
            }
        writer.writeNext(sortedMap.values.map { it.ifBlank { "-" } }.toTypedArray())
    }
    writer.close()

    val uri = FileProvider.getUriForFile(this,  BuildConfig.APP_AUTHORITY, csvFile)

    val sharingIntent = Intent().apply {
        action = Intent.ACTION_SEND
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        putExtra(Intent.EXTRA_STREAM, uri)
        type = "text/*"
    }

    startActivity(Intent.createChooser(sharingIntent, "Share $eventName Data with"))
}