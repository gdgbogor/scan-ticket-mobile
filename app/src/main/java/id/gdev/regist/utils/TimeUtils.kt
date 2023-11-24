package id.gdev.regist.utils

import android.os.Environment
import com.google.firebase.Timestamp
import id.gdev.regist.utils.TimeUtils.formatFile
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale


object TimeUtils {

    val Timestamp.simple get(): String {
        val sdf = SimpleDateFormat("EEE, MMM dd, k:mm a", Locale.getDefault())
        return sdf.format(this.toDate()).plus(" (WIB)")
    }

    val Timestamp.hourMinutes get() : String {
        val sdf = SimpleDateFormat("k:mm a", Locale.getDefault())
        return sdf.format(this.toDate())
    }

    val Timestamp.dateSimple get() : String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(this.toDate())
    }

    val Timestamp.formatFile get() : String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH.mm.ss", Locale.getDefault())
        return sdf.format(this.toDate())
    }

    val Timestamp.pastEvent get() : Boolean {
        return System.currentTimeMillis() > this.toDate().time
    }

    val Timestamp.upComingEvent get() : Boolean {
        return System.currentTimeMillis() <= this.toDate().time
    }
}

val String.toFileName get(): String {
    return  this.lowercase().replace(" ", "-").plus(" ").plus(Timestamp.now().formatFile)
}

fun getStorageDirectory(): File {
    return if (Environment.getExternalStorageState() == null) {
        val f = File(Environment.getDataDirectory().absolutePath + "/Download/Registration App/")
        if (!f.exists()) f.mkdirs()
        f
    } else {
        val f =
            File(Environment.getExternalStorageDirectory().absolutePath + "/Download/Registration App/")
        if (!f.exists()) f.mkdirs()
        f
    }
}