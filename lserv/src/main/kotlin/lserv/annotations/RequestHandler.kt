package lserv.annotations

import lserv.net.Method

@Target(AnnotationTarget.FUNCTION)
annotation class RequestHandler(val method: Method = Method.ANY)
