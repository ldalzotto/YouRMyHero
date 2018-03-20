package flappy.graphics.render

import flappy.graphics.shader.Shader
import flappy.graphics.texture.Texture
import flappy.graphics.{Camera, VertexArray}
import flappy.physics.PhysicsContants
import org.joml.Matrix4f

trait Renderable {

  val shader: Shader
  val texture: Texture

  val physicsWidthSize: Float
  val physicsHeightSize: Float

  def pr_matrix: Matrix4f = {
    Camera.pr_matrix
  }

  val mesh: VertexArray = {
    val vertices = Array[Float](
      -physicsWidthSize * PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL / 2f, -physicsHeightSize * PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL / 2f, 0.1f,
      -physicsWidthSize * PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL / 2f, physicsHeightSize * PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL / 2f, 0.1f,
      physicsWidthSize * PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL / 2f, physicsHeightSize * PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL / 2f, 0.1f,
      physicsWidthSize * PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL / 2f, -physicsHeightSize * PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL / 2f, 0.1f)

    val indices = Array[Byte](0, 1, 2, 2, 3, 0)

    val tcs = Array[Float](0, 1, 0, 0, 1, 0, 1, 1)

    new VertexArray(vertices, indices, tcs)
  }

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
