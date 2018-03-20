package flappy.graphics


import flappy.util.ShaderUtils
import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20._

class Shader(vertexPath: String, fragPath: String) {

  val shaderId = ShaderUtils.load(vertexPath, fragPath)

  def setUniformMat4f(name: String, matrix4f: Matrix4f): Unit = {

    bind()

    val uniformId = glGetUniformLocation(shaderId, name)

    val floatBuffer = BufferUtils.createFloatBuffer(16)
    matrix4f.get(floatBuffer)

    glUniformMatrix4fv(uniformId, false, floatBuffer)

    unbind()
  }

  def setUniformli(name: String, value: Int): Unit = {
    bind()
    val uniformId = glGetUniformLocation(shaderId, name)
    glUniform1i(uniformId, value)
    unbind()
  }

  def bind(): Unit = {
    glUseProgram(shaderId)
  }

  def unbind(): Unit = {
    glUseProgram(0)
  }


}

object Shader {

  val VERTEX_ATTRIB = 0
  val TEXCOORD_ATTRIB = 1

}