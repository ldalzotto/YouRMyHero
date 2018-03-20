package flappy.graphics

import flappy.graphics.shader.Shader
import flappy.graphics.texture.Texture
import org.joml.Matrix4f

trait Renderable {

  val SIZE: Float

  val mesh: VertexArray = {
    val vertices = Array[Float](
      -SIZE / 2f, -SIZE / 2f, 0.1f,
      -SIZE / 2f, SIZE / 2f, 0.1f,
      SIZE / 2f, SIZE / 2f, 0.1f,
      SIZE / 2f, -SIZE / 2f, 0.1f)

    val indices = Array[Byte](0, 1, 2, 2, 3, 0)

    val tcs = Array[Float](0, 1, 0, 0, 1, 0, 1, 1)

    new VertexArray(vertices, indices, tcs)
  }
  val shader: Shader
  val texture: Texture

  val pr_matrix: Matrix4f = new Matrix4f().ortho(-Camera.CAMERA_WIDTH, Camera.CAMERA_WIDTH, -Camera.CAMERA_WIDTH * 9 / 16, Camera.CAMERA_WIDTH * 9 / 16, -1, 1)

  def render(): Unit = {

    shader.bind()
    texture.bind()
    mesh.bind()
    mesh.draw()
    mesh.unbind()
    texture.unbind()
    shader.unbind()

  }

}
