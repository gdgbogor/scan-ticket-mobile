package id.gdev.regist.utils

enum class FilterSort{
    ASC, DESC
}

data class SheetFilter(
    val name: String,
    var selected: Boolean? = false,
)

object Filter {
    const val Name = "Name"
    const val Header = "Header"
    const val Title = "Title"
    const val Subtitle = "Subtitle"
    const val CheckInDate = "Check In Date"
}