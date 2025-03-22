package io.jade

import io.insequence.renderer.Renderer
import io.insequence.renderer.Window

fun main() {
    val renderer = Renderer()
    renderer.openWindow(Window(300,300, "test"))
    renderer.mainLoop {

    }
}