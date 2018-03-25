package flappy.physics

import flappy.MyWorld
import flappy.physics.PhysicsProvider.{BodyDefProvider, FixtureDefProvider, ShapeProvider}
import org.jbox2d.collision.shapes.Shape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.{Body, BodyDef, FixtureDef}

trait Moveable {

  val fixtureDefProvider: FixtureDefProvider
  val bodyDefProvider: BodyDefProvider
  val shapeProvider: ShapeProvider

  val userDataTag: String

  val physicPosition: Vec2

  val body: Body = {
    val bodyDef = bodyDefProvider.apply()
    bodyDef.position.set(physicPosition)
    val body = MyWorld.world.createBody(bodyDef)
    val shape = shapeProvider.apply()
    val fixtureDef = fixtureDefProvider.apply()
    fixtureDef.userData = UserData(userDataTag, this)
    fixtureDef.shape = shape
    body.createFixture(fixtureDef)
    body.setUserData(UserData(userDataTag, this))
    body
  }

  def applyForceAtCenter(force: Vec2): Unit = {
    body.applyForceToCenter(force)
  }

  def setSpeed(speed: Vec2): Unit = {
    body.m_linearVelocity.set(speed)
  }

  def addSpeed(speed: Vec2): Unit = {
    setSpeed(body.m_linearVelocity.add(speed))
  }

  def setPosition(position: Vec2): Unit = {
    body.m_xf.p.set(position)
  }

}

object PhysicsProvider {
  type BodyDefProvider = () => BodyDef
  type FixtureDefProvider = () => FixtureDef
  type ShapeProvider = () => Shape
}