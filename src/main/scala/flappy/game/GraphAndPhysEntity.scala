package flappy.game

import flappy.physics.contact.Contactable
import flappy.physics.{Moveable, PhysicsContants}
import org.jbox2d.callbacks.ContactListener
import org.jbox2d.common.Vec2
import org.joml.Matrix4f

abstract class GraphAndPhysEntity(override val physicsWidthSize: Float, override val physicsHeightSize: Float,
                                  override val physicsPosition: Vec2 = new Vec2(0, 0),
                                  override val vertexPath: String,
                                  override val fragmentPath: String,
                                  override val texturePath: String)
  extends GraphEntity(physicsPosition, vertexPath, fragmentPath, texturePath) with Moveable with Contactable {

  override lazy val physicPosition: Vec2 = {
    Option(body)
      .map(_.m_xf)
      .map(_.p)
      .getOrElse(physicsPosition)
  }

  def update(): Unit = {

  }

  override lazy val contactListener: Option[ContactListener] = None

  def render(viewMatrix: Matrix4f, additionalViewMatriOperation: (Matrix4f) => Matrix4f): Unit = {
    viewMatrix.translate(body.getPosition.x * PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL,
      body.getPosition.y * PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL, 0).rotate(body.getAngle, 0, 0, 1)
    val modifiedViewMatrix = additionalViewMatriOperation.apply(viewMatrix)
    super.render(modifiedViewMatrix)
  }

  override def render(viewMatrix: Matrix4f): Unit = {
    render(viewMatrix, m => m)
  }

  override def render(viewMatrixs: List[Matrix4f]): Unit = {
    viewMatrixs.foreach(render(_, m => m))
  }

}
