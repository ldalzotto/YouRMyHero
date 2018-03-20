package flappy.level

import flappy.game.GraphAndPhysEntity
import flappy.physics.PhysicsContants
import flappy.physics.PhysicsProvider.{BodyDefProvider, FixtureDefProvider, ShapeProvider}
import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.{BodyDef, BodyType, FixtureDef}

class Ground(override val SIZE: Float, override val physicsPosition: Vec2 = new Vec2(0, 0)) extends GraphAndPhysEntity(SIZE, physicsPosition) {
  override lazy val fixtureDefProvider: FixtureDefProvider = () => {
    val fixtureDef = new FixtureDef
    fixtureDef.density = 10f
    fixtureDef.restitution = 0.5f
    fixtureDef
  }
  override lazy val bodyDefProvider: BodyDefProvider = () => {
    val bodyDef = new BodyDef
    bodyDef.`type` = BodyType.STATIC
    bodyDef
  }
  override lazy val shapeProvider: ShapeProvider = () => {
    val shape = new PolygonShape
    shape.setAsBox(SIZE / PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL / 2, SIZE / PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL / 2)
    shape
  }
}
