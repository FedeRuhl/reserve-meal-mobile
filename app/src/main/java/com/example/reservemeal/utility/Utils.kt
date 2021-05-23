package com.example.reservemeal.utility

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import android.view.View
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject

fun ContentResolver.getFileName(fileUri: Uri): String {
    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }
    return name
}

fun getPayloadValue(jwt: String, key: String): String {
    val data = getPayloadJWT(jwt ?: "")
    if (data.isEmpty()) return ""

    val obj = JSONObject(data)
    return obj.optString(key)
    //return obj[key].toString()
}

private fun getPayloadJWT(jwt: String): String {
    return if (jwt.isNotEmpty()) {
        val array = jwt.split(".")
        val decode = Base64.decode(array[1], Base64.URL_SAFE)
        String(decode, Charsets.UTF_8)
    } else
        ""
}