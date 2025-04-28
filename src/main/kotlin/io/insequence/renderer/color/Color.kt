package io.insequence.renderer.color

import org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT
import org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT
import org.lwjgl.opengl.GL11.glClear
import org.lwjgl.opengl.GL11.glClearColor
import kotlin.random.Random

data class Color(
    val red: Float,
    val green: Float,
    val blue: Float,
    val alpha: Float
) {

    companion object {
        fun randomColor(): Color {
            return Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1.0f)
        }
        fun setColor(rgba: Color) {
            val (red, green, blue, alpha) = rgba
            glClearColor(red, green, blue, alpha)
        }
        fun clear(depthBuffer: Boolean = false) {
            glClear(if (depthBuffer) GL_DEPTH_BUFFER_BIT else GL_COLOR_BUFFER_BIT)
        }
    }
}