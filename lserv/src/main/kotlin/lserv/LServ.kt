package lserv

import lserv.annotations.RequestHandler
import lserv.net.Method
import lserv.net.Request
import lserv.net.Response
import java.net.ServerSocket
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

class LServ {
    private var port: Int = 8005
    private var maxInputLength: Int = 10_000_000
    private var handlers: MutableList<KFunction<*>> = ArrayList()

    fun port(port: Int): LServ {
        this.port = port
        return this
    }

    fun maxInputLength(maxInputLength: Int): LServ {
        this.maxInputLength = maxInputLength
        return this
    }

    fun registerHandler(func: KFunction<*>): LServ {
        //does it have the RequestHandler annotation?
        if (!func.annotations.any { it is RequestHandler }) {
            return this
        }

        //is the parameter the Request type?
        func.parameters.filter { it.kind == KParameter.Kind.VALUE }.forEach { param ->
            if (param.type.classifier != Request::class) {
                return this
            }
        }

        handlers.add(func)
        return this
    }

//    fun registerHandler(clazz: KClass<*>): LServ {
//        val candidates = clazz.memberFunctions.filter { func ->
//            //does it have the RequestHandler annotation?
//            if (!func.annotations.any { it is RequestHandler }) {
//                return@filter false
//            }
//
//            //is the parameter the Request type?
//            func.parameters.filter { it.kind == KParameter.Kind.VALUE }.forEach { param ->
//                if (param.type.classifier != Request::class) {
//                    return@filter false
//                }
//            }
//            return@filter true
//        }
//
//        if (candidates.isEmpty()) {
//            System.err.println("No handler candidates found in: " + clazz.simpleName)
//            return this
//        }
//
//        handlers.addAll(candidates)
//        return this
//    }

    fun start() {
        val serverSocket = ServerSocket(port)
        println("Server started")
        while (true) {
            //start a new thread for every connection
            val socket = serverSocket.accept()
            Thread {
                var input = ""
                //read until the end
                while (socket.getInputStream().available() > 0) {
                    val data = socket.getInputStream().read()
                    input += data.toChar()
                    if (input.length > maxInputLength) {
                        System.err.println("Input is longer than the maximum input length (${maxInputLength})! Closing connection.")
                        Response(responseCode = 413).send(socket.getOutputStream())
                        socket.close()
                        return@Thread
                    }
                }
                val request = Request.parse(input)
                if (request.isError()) {
                    request.error!!.send(socket.getOutputStream())
                    socket.close()
                    return@Thread
                }
                val req = request.obj!!
                handlers.forEach { func ->
                    val annotation = func.annotations.first { it is RequestHandler } as RequestHandler
                    if (req.method == annotation.method || annotation.method == Method.ANY) {
                        func.call(req)
                    }
                }

                val response = Response(responseCode = 200, body = "Hello!")
                response.send(socket.getOutputStream())

                socket.close()
            }.start()
        }
    }
}
