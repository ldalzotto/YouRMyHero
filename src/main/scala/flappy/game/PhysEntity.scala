package flappy.game

import flappy.physics.Moveable
import flappy.physics.contact.Contactable
import org.jbox2d.callbacks.ContactListener
import org.jbox2d.common.Vec2

trait PhysEntity extends Moveable with Contactable {

  val initialPhysicsPosition: Vec2

  override lazy val physicPosition: Vec2 = {
    Option(body)
      .map(_.m_xf)
      .map(_.p)
      .getOrElse(initialPhysicsPosition)
  }

  override lazy val contactListener: Option[ContactListener] = None

  def update(): Unit = {

  }

}
