package id.gdev.regist.utils

import android.content.Context
import android.widget.Toast

class PopMessage(private val context: Context){
    fun show(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}