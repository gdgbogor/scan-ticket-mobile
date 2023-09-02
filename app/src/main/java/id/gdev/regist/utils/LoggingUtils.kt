package id.gdev.regist.utils

import android.util.Log

object CreateLog {
    fun Any.d(message: String){
        Log.d(this::class.java.simpleName, message)
    }
}