package id.gdev.regist.utils

import com.google.mlkit.vision.barcode.common.Barcode

enum class FilterSort{
    ASC, DESC
}

data class SheetFilter(
    val name: String,
    var selected: Boolean? = false,
)

const val LIMIT_DATA = 10

enum class BarcodeEncoding{
    NONE, GDG
}

fun Array<BarcodeEncoding>.getList(): List<String> = this.toList().map { it.toString() }

fun String.getBarcodeEncoding(): BarcodeEncoding {
    return when(this){
        BarcodeEncoding.GDG.toString() -> BarcodeEncoding.GDG
        BarcodeEncoding.NONE.toString() -> BarcodeEncoding.NONE
        else -> BarcodeEncoding.NONE
    }
}

fun Barcode.decode(barcodeEncoding: BarcodeEncoding): String {
    return when(barcodeEncoding){
        BarcodeEncoding.NONE -> this.rawValue.toString()
        BarcodeEncoding.GDG -> this.rawValue.toString().split(":").last()
    }
}

object Filter {
    const val Name = "Name"
    const val Header = "Header"
    const val Title = "Title"
    const val Subtitle = "Subtitle"
    const val CheckInDate = "Check In Date"

    fun getFieldName(filterName: String): String {
        return when (filterName.removePrefix("Filter by ")) {
            CheckInDate -> "lastCheckInTime"
            Title -> "title"
            Header -> "header"
            Subtitle -> "subtitle"
            else -> ""
        }
    }
}