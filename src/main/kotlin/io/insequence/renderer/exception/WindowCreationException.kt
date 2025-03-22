package io.insequence.renderer.exception

import io.insequence.renderer.Window

class WindowCreationException(window: Window): Exception("Failed to initialize window ${window.name}") {
}