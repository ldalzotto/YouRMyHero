package flappy.game

import flappy.graphics.render.Renderable
import flappy.graphics.shader.{Shader, ShaderManager}
import flappy.graphics.texture.{Texture, TextureManager}
import flappy.physics.contact.Contactable
import flappy.physics.{Moveable, PhysicsContants}
import org.jbox2d.callbacks.ContactListener
import org.jbox2d.common.Vec2
import org.joml.Matrix4f

abstract class GraphAndPhysEntity(override val physicsWidthSize: Float, override val physicsHeightSize: Float, val physicsPosition: Vec2 = new Vec2(0, 0))
  extends Renderable with Moveable with Contactable {

  val shader: Shader = ShaderManager.getOrDefine("shaders/bird.vert" + "shaders/bird.frag", () => new Shader("shaders/bird.vert", "shaders/bird.frag"))
  override lazy val texture: Texture = TextureManager.getOrDefine("res/block.png", () => new Texture("res/block.png"))

  override lazy val physicPosition: Vec2 = {
    Option(body)
      .map(_.m_xf)
      .map(_.p)
      .getOrElse(physicsPosition)
  }

  shader.setUniformMat4f(Shader.PROJECTION_MATRIX, pr_matrix)
  shader.setUniformli("tex", 1)

  def update(): Unit = {

  }


  override lazy val contactListener: Option[ContactListener] = None

  override def render(): Unit = {
    val viewMatrix = new Matrix4f().translate(body.getPosition.x * PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL,
      body.getPosition.y * PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL, 0).rotate(body.getAngle, 0, 0, 1)
    shader.setUniformMat4f(Shader.VIEW_MATTRIX, viewMatrix)
    super.render()
  }

}
