package flappy.graphics

import flappy.graphics.shader.Shader
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._

class VertexArray(vertices: Array[Float], indices: Array[Byte], textureCoordinates: Array[Float]) {

  val count: Int = indices.length

  var vao: Int = _
  var vbo: Int = _
  var vio: Int = _
  var vto: Int = _

  create()

  def create(): Unit = {
    vao = glGenVertexArrays()
    glBindVertexArray(vao)

    val vboBuffer = BufferUtils.createFloatBuffer(vertices.length)
    vboBuffer.put(vertices).flip()

    val vtoBuffer = BufferUtils.createFloatBuffer(textureCoordinates.length)
    vtoBuffer.put(textureCoordinates).flip()

    val vioBuffer = BufferUtils.createByteBuffer(indices.length)
    vioBuffer.put(indices).flip()

    vbo = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, vbo)
    glBufferData(GL_ARRAY_BUFFER, vboBuffer, GL_STATIC_DRAW)
    glVertexAttribPointer(Shader.VERTEX_ATTRIB, 3, GL_FLOAT, false, 0, 0)
    glEnableVertexAttribArray(Shader.VERTEX_ATTRIB)

    vto = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, vto)
    glBufferData(GL_ARRAY_BUFFER, vtoBuffer, GL_STATIC_DRAW)
    glVertexAttribPointer(Shader.TEXCOORD_ATTRIB, 2, GL_FLOAT, false, 0, 0)
    glEnableVertexAttribArray(Shader.TEXCOORD_ATTRIB)

    vio = glGenBuffers()
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vio)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, vioBuffer, GL_STATIC_DRAW)

    glBindBuffer(GL_ARRAY_BUFFER, 0)
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
    glBindVertexArray(0)

  }

  def bind(): Unit = {
    glBindVertexArray(vao)
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vio)
    glEnableVertexAttribArray(0)
    glEnableVertexAttribArray(1)
  }

  def unbind(): Unit = {
    glBindVertexArray(0)
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
  }

  def draw(): Unit = {
    glDrawElements(GL_TRIANGLES, count, GL_UNSIGNED_BYTE, 0)
  }

}
