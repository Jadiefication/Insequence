package io.insequence.exception

import org.lwjgl.glfw.GLFWErrorCallback
import java.io.PrintStream

class ExceptionHandler {

    companion object {
        lateinit var printStream: PrintStream
    }

    fun setOutput(_printStream: PrintStream) {
        GLFWErrorCallback.createPrint(_printStream).set()
        printStream = _printStream
    }
}