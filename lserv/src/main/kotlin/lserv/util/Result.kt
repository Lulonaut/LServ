package lserv.util

import lserv.net.Response

class Result<T>(val thing: T, val error: Response?) {

}
