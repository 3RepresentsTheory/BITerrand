package com.example.biterrand_fix.data.token
import android.content.Context
import java.io.File

class TokenManager(private val context: Context) {

    private val FILENAME = "my_token.txt"

    fun saveToken(token: String) {
        val file = File(context.filesDir, FILENAME)
        file.writeText(token)
    }

    fun getToken(): String? {
        val file = File(context.filesDir, FILENAME)
        return if (file.exists()) {
            file.readText()
        } else {
            null
        }
    }

}
