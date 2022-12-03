package lserv.util

class Result<T, R>(val obj: T? = null, val error: R? = null) {

    fun isError(): Boolean = obj == null

    fun isSuccess(): Boolean = obj != null
}
