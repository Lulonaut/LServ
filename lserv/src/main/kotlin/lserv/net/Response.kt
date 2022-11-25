package lserv.net

import lserv.util.StatusCodeUtil
import java.io.OutputStream

class Response(
    val responseCode: Int = 404,
    val headers: Headers = Headers(
        hashMapOf(
            "Connection" to "Closed",
            "Content-Type" to "text/plain;charset=utf-8"
        )
    ),
    val body: String = String()
) {


    fun build(): String {
        val s = StringBuilder(128)
        s.append("HTTP/1.1")
        s.append(' ')
        s.append(responseCode)
        s.append(' ')
        s.append(StatusCodeUtil.getMessage(responseCode))
        s.append('\n')


        for (header in headers.raw) {
            s.append(header.key)
            s.append(':')
            s.append(header.value)
            s.append('\n')
        }
        if (body.isNotEmpty()) {
            //add content length and end the headers block
            s.append("Content-Length: ${body.length}\n\n")
            s.append(body)
        }


        return s.toString()
    }

    fun send(outputStream: OutputStream) {
        outputStream.write(build().toByteArray(Charsets.UTF_8))
    }
}