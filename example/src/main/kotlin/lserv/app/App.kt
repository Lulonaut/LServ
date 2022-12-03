package lserv.app

import lserv.LServ
import lserv.annotations.RequestHandler
import lserv.net.Method
import lserv.net.Request

class App {
    fun run() {
        LServ().port(8002).registerHandler(::handler).start()
    }

    companion object {
        @RequestHandler(method = Method.GET)
        fun handler(req: Request) {
            println("called!")
            println(req.headers.get("User-Agent"))
        }


        @RequestHandler()
        fun random(req: Request) {

        }

    }
}

fun main() {
    App().run()
}

