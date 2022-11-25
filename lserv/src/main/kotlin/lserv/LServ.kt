package lserv

import lserv.net.Request
import lserv.net.Response
import java.io.OutputStream
import java.net.ServerSocket

class LServ {
    private var port: Int = 8005
    private var maxInputLength: Int = 1_000_000

    fun port(port: Int): LServ {
        this.port = port
        return this
    }

    fun maxInputLength(maxInputLength: Int): LServ {
        this.maxInputLength = maxInputLength
        return this
    }

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
                        val r = Response(responseCode = 413).send(socket.getOutputStream())
                        socket.close()
                        return@Thread
                    }
                }
                val request = Request.parse(input)
                val response = Response(responseCode = 200, body = "Hello!")
                response.send(socket.getOutputStream())

                socket.close()
            }.start()
        }
    }
}

private fun OutputStream.write(s: String) {
    write(s.toByteArray())
}
