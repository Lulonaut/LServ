package lserv.net

class Headers(val raw: HashMap<String, String>) {
    /**
     * check if the given key exists
     */
    fun exists(key: String): Boolean {
        return raw.containsKey(key)
    }

    fun getOrDefault(key: String, default: String): String {
        return if (exists(key)) {
            raw[key]!!
        } else {
            default
        }
    }

    /**
     * Get a specific header value. If it does not exist, return an empty String (instead of null if accessing directly)
     *
     * @return value or empty string
     */
    fun get(key: String): String {
        return if (exists(key)) {
            raw[key]!!
        } else {
            ""
        }
    }

    companion object {
        /**
         * Parse all existent headers from a HTTP Request
         */
        fun parse(from: String): Headers {
            val headers: HashMap<String, String> = HashMap()

            val startIndex = from.indexOf("\r\n") + 2
            var endIndex = from.indexOf("\r\n\r\n")
            if (endIndex == -1) {
                endIndex = from.length - 1
            }

            val headersString = from.substring(startIndex, endIndex)
            for (line in headersString.split("\r\n")) {
                val split = line.split(':', limit = 2)
                headers[split[0]] = split[1].trim()
            }
            println(headers)
            return Headers(headers)
        }

        fun empty(): Headers {
            return Headers(HashMap())
        }
    }
}