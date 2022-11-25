package lserv.net

enum class Method {
    GET, HEAD, POST, PUT, DELETE, CONNECT, OPTIONS, TRACE, PATCH, CUSTOM;

    companion object {
        /**
         * Try to parse the request method0. If the method is not known it will be CUSTOM
         */
        fun fromString(from: String): Method {
            return values().firstOrNull { it.name.equals(from, true) } ?: return CUSTOM
        }
    }
}