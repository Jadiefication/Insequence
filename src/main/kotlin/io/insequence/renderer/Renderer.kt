package io.insequence.renderer

import io.insequence.exception.ExceptionHandler
import io.insequence.renderer.exception.WindowCreationException
import org.lwjgl.PointerBuffer
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.vulkan.*
import org.lwjgl.vulkan.VK10.*
import org.lwjgl.vulkan.VK13.VK_API_VERSION_1_3
import java.nio.ByteBuffer
import java.nio.LongBuffer
import kotlin.properties.Delegates


class Renderer {

    var window by Delegates.notNull<Long>()

    fun openWindow(windowStats: Window) {
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
            }

            val pInstance = it.mallocPointer(1)

            val result = vkCreateInstance(createInfo, null, pInstance)
            if (result != VK_SUCCESS) {
                throw WindowCreationException(window = windowStats)
            }

            // Retrieve instance handle
            val instance = pInstance.get(0)
            val vkInstance = VkInstance(instance, createInfo)

            println("Vulkan Instance Created Successfully: $instance")

            // Destroy instance at the end
            vkDestroyInstance(vkInstance, null)
        }
    }
}