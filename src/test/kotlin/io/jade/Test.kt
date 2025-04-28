package io.jade

import io.insequence.exception.ExceptionHandler
import io.insequence.renderer.Renderer
import io.insequence.renderer.Window
import io.insequence.renderer.color.Color

fun main() {
    val exceptionHandler = ExceptionHandler()
    exceptionHandler.setOutput(System.err)

    val renderer = Renderer()
    renderer.openWindow(Window(300,300, "test"))
    renderer.mainLoop {
        Color.setColor(Color.randomColor())
        Color.clear()
        Thread.sleep(1000) // Slow down the loop for debugging
    }
}