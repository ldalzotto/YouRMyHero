package flappy.physics

import flappy.MyWorld
import flappy.physics.PhysicsProvider.{BodyDefProvider, FixtureDefProvider, ShapeProvider}
import org.jbox2d.collision.shapes.Shape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.{Body, BodyDef, FixtureDef}

trait Moveable {

  val SIZE: Float

  val fixtureDefProvider: FixtureDefProvider
  val bodyDefProvider: BodyDefProvider
  val shapeProvider: ShapeProvider

  val physicPosition: Vec2

  val body: Body = {
    val bodyDef = bodyDefProvider.apply()
    bodyDef.position.set(physicPosition)
    val body = MyWorld.world.createBody(bodyDef)
    val shape = shapeProvider.apply()
    val fixtureDef = fixtureDefProvider.apply()
    fixtureDef.shape = shape
    body.createFixture(fixtureDef)
    body
  }

}

object PhysicsProvider {
  type BodyDefProvider = () => BodyDef
  type FixtureDefProvider = () => FixtureDef
  type ShapeProvider = () => Shape
}