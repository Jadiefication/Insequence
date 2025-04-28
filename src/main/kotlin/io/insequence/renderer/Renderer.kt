package io.insequence.renderer

import io.insequence.renderer.exception.GLFWInitializeException
import org.lwjgl.glfw.GLFW.GLFW_PLATFORM
import org.lwjgl.glfw.GLFW.GLFW_PLATFORM_X11
import org.lwjgl.glfw.GLFW.glfwCreateWindow
import org.lwjgl.glfw.GLFW.glfwDestroyWindow
import org.lwjgl.glfw.GLFW.glfwGetWindowSize
import org.lwjgl.glfw.GLFW.glfwInit
import org.lwjgl.glfw.GLFW.glfwInitHint
import org.lwjgl.glfw.GLFW.glfwMakeContextCurrent
import org.lwjgl.glfw.GLFW.glfwPlatformSupported
import org.lwjgl.glfw.GLFW.glfwPollEvents
import org.lwjgl.glfw.GLFW.glfwShowWindow
import org.lwjgl.glfw.GLFW.glfwSwapBuffers
import org.lwjgl.glfw.GLFW.glfwSwapInterval
import org.lwjgl.glfw.GLFW.glfwTerminate
import org.lwjgl.glfw.GLFW.glfwWindowShouldClose
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.NULL
import kotlin.properties.Delegates


class Renderer {

    var window by Delegates.notNull<Long>()

    fun openWindow(windowStats: Window) {
        if (glfwPlatformSupported(GLFW_PLATFORM_X11)) {
            glfwInitHint(GLFW_PLATFORM, GLFW_PLATFORM_X11)
        }
        if (!glfwInit()) {
            throw GLFWInitializeException()
        }

        window = glfwCreateWindow(windowStats.width, windowStats.height, windowStats.name, NULL, NULL)
        MemoryStack.stackPush().use {
            val pWidth = it.mallocInt(1)
            val pHeight = it.mallocInt(1)

            glfwGetWindowSize(window, pWidth, pHeight)
        }

        glfwMakeContextCurrent(window)
        createCapabilities()
        glfwSwapInterval(1)
        glfwShowWindow(window)
    }

    fun mainLoop(function: () -> Unit) {
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents()
            function()
            glfwSwapBuffers(window)
        }
        glfwDestroyWindow(window)
        glfwTerminate()
    }
}