package lserv.net

import org.apache.commons.text.StringEscapeUtils

/**
 * Represents an incoming HTTP request
 */
class Request(val method: Method, val rawMethod: String, val target: String, val headers: Headers) {

    companion object {
        /**
         * Parse a full HTTP request
         */
        fun parse(request: String): Request {
            Result
            //split the first line of the request
            val split = request.split(' ', '\r')
            if (split.size < 3 || split[2] != "HTTP/1.1") {
                println("Invalid Request: No HTTP 1.1!")
            }
            request.p()


            val rawRequestMethod = split[0]
            val requestMethod = Method.fromString(rawRequestMethod)
            val target = split[1]
            val headers = Headers.parse(request)


            return Request(requestMethod, rawRequestMethod, target, headers)
        }
    }
}

fun String.p() {
    println(StringEscapeUtils.escapeJava(this))
}