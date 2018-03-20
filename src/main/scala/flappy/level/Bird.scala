package flappy.level

import flappy.graphics.{Renderable, Shader, Texture, VertexArray}
import org.joml.Matrix4f

class Bird extends Renderable {

  val shader: Shader = new Shader("shaders/bird.vert", "shaders/bird.frag")
  val mesh: VertexArray = {
    val vertices = Array[Float](-1 / 2f, -1 / 2f, 0.1f, -1 / 2f, 1 / 2f, 0.1f, 1 / 2f, 1 / 2f, 0.1f, 1 / 2f, -1 / 2f, 0.1f)

    val indices = Array[Byte](0, 1, 2, 2, 3, 0)

    val tcs = Array[Float](0, 1, 0, 0, 1, 0, 1, 1)

    new VertexArray(vertices, indices, tcs)
  }
  val texture: Texture = new Texture("res/block.png")


  val pr_matrix: Matrix4f = new Matrix4f().ortho(-10f, 10f, -10 * 9 / 16, 10 * 9 / 16, -1, 1)

  shader.setUniformMat4f("pr_matrix", pr_matrix)
  shader.setUniformli("tex", 1)

}
