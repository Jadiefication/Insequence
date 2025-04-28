package io.jade

import io.insequence.exception.ExceptionHandler
import io.insequence.renderer.Renderer
import io.insequence.renderer.Window

fun main() {
    val exceptionHandler = ExceptionHandler()
    exceptionHandler.setOutput(System.err)

    val renderer = Renderer()
    renderer.openWindow(Window(300,300, "test"))
    renderer.mainLoop {
        println("Main loop iteration")
        Thread.sleep(1000) // Slow down the loop for debugging
    }
}