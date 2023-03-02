package com.example.crm

import android.util.Log
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class Upload {
    private var serverResponseCode = 0

    fun uploadVideo(file: String): String? {
        var conn: HttpURLConnection? = null
        var dos: DataOutputStream? = null
        val lineEnd = "\r\n"
        val twoHyphens = "--"
        val boundary = "*****"
        var bytesRead: Int
        var bytesAvailable: Int
        var bufferSize: Int
        val buffer: ByteArray
        val maxBufferSize = 1 * 1024 * 1024
        val sourceFile = File(file)
        if (!sourceFile.isFile()) {
            Log.e("Huzza", "Source File Does not exist")
            return null
        }
        try {
            val fileInputStream = FileInputStream(sourceFile)
            val url = URL("http://192.168.0.106/crm/registrationapi.php?apicall=")
            conn = url.openConnection() as HttpURLConnection
            conn.setDoInput(true)
            conn.setDoOutput(true)
            conn.setUseCaches(false)
            conn.setRequestMethod("POST")
            conn.setRequestProperty("Connection", "Keep-Alive")
            conn.setRequestProperty("ENCTYPE", "multipart/form-data")
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=$boundary")
            conn.setRequestProperty("myFile", file)
            dos = DataOutputStream(conn.getOutputStream())
            dos.writeBytes(twoHyphens + boundary + lineEnd)
            dos.writeBytes("Content-Disposition: form-data; name=\"myFile\";filename=\"$file\"$lineEnd")
            dos.writeBytes(lineEnd)
            bytesAvailable = fileInputStream.available()
            Log.i("Huzza", "Initial .available : $bytesAvailable")
            bufferSize = Math.min(bytesAvailable, maxBufferSize)
            buffer = ByteArray(bufferSize)
            bytesRead = fileInputStream.read(buffer, 0, bufferSize)
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize)
                bytesAvailable = fileInputStream.available()
                bufferSize = Math.min(bytesAvailable, maxBufferSize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize)
            }
            dos.writeBytes(lineEnd)
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)
            serverResponseCode = conn.getResponseCode()
            fileInputStream.close()
            dos.flush()
            dos.close()
        } catch (ex: MalformedURLException) {
            ex.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return if (serverResponseCode == 200) {
            val sb = StringBuilder()
            try {
                val rd = BufferedReader(
                    InputStreamReader(
                        conn!!.getInputStream()
                    )
                )
                var line: String?
                while (rd.readLine().also { line = it } != null) {
                    sb.append(line)
                }
                rd.close()
            } catch (ioex: IOException) {
            }
            sb.toString()
        } else {
            "Could not upload"
        }
    }

}