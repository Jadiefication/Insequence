package io.insequence.renderer

import io.insequence.exception.ExceptionHandler
import io.insequence.renderer.exception.GLFWInitializeException
import io.insequence.renderer.exception.WindowCreationException
import org.lwjgl.PointerBuffer
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWVulkan.Functions.VulkanSupported
import org.lwjgl.glfw.GLFWVulkan.glfwCreateWindowSurface
import org.lwjgl.glfw.GLFWVulkan.glfwGetRequiredInstanceExtensions
import org.lwjgl.glfw.GLFWVulkan.glfwVulkanSupported
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.vulkan.*
import org.lwjgl.vulkan.KHRSurface.vkDestroySurfaceKHR
import org.lwjgl.vulkan.VK10.*
import org.lwjgl.vulkan.VK13.VK_API_VERSION_1_3
import java.nio.ByteBuffer
import java.nio.LongBuffer
import kotlin.properties.Delegates


class Renderer {

    var window by Delegates.notNull<Long>()
    private lateinit var vkInstance: VkInstance
    private var surfacePtr by Delegates.notNull<Long>()

    fun openWindow(windowStats: Window) {
        if (glfwPlatformSupported(GLFW_PLATFORM_X11)) {
            glfwInitHint(GLFW_PLATFORM, GLFW_PLATFORM_X11)
        }

        if (!glfwInit()) {
            throw GLFWInitializeException()
        }
        glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API)
        MemoryStack.stackPush().use {
            val info: VkApplicationInfo = VkApplicationInfo.calloc(it).apply {
                sType(VK_STRUCTURE_TYPE_APPLICATION_INFO)
                pApplicationName(it.UTF8(windowStats.name))
                applicationVersion(VK_MAKE_VERSION(1, 0, 0))
                apiVersion(VK_API_VERSION_1_3)
            }

            val createInfo = VkInstanceCreateInfo.calloc(it).apply {
                sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO)
                pApplicationInfo(info)
                ppEnabledExtensionNames(glfwGetRequiredInstanceExtensions())
            }

            val pInstance = it.mallocPointer(1)

            if (vkCreateInstance(createInfo, null, pInstance) != VK_SUCCESS) {
                throw WindowCreationException(window = windowStats)
            }

            // Retrieve instance handle
            val instance = pInstance.get(0)
            vkInstance = VkInstance(instance, createInfo)

            println("Vulkan Instance Created Successfully: $instance")
        }

        window = glfwCreateWindow(windowStats.width, windowStats.height, windowStats.name, NULL, NULL)
        surfacePtr = MemoryStack.stackPush().use {
            val pSurface = it.mallocLong(1)
            if (glfwCreateWindowSurface(vkInstance, window, null, pSurface) != VK_SUCCESS)
                throw RuntimeException("Failed to create Vulkan surface")
            pSurface[0]
        }
    }

    fun mainLoop(function: () -> Unit) {
        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents()
            function()
        }
        vkDestroySurfaceKHR(vkInstance, surfacePtr, null)
        vkDestroyInstance(vkInstance, null)
        glfwDestroyWindow(window)
        glfwTerminate()
    }
}