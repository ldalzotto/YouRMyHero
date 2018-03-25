package flappy.input

import org.lwjgl.glfw.{GLFW, GLFWKeyCallback}

object Input extends GLFWKeyCallback with KeysJustPressed {

  val keys = new Array[Boolean](65536)

  override def invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int): Unit = {
    if (action != GLFW.GLFW_RELEASE) {
      keys(key) = action != GLFW.GLFW_RELEASE

      if (action != GLFW.GLFW_REPEAT) {
        keyPushed(key)
      }
    } else {
      keys(key) = false
    }
  }

  def isKeyDown(keycode: Int) = keys(keycode)
}
