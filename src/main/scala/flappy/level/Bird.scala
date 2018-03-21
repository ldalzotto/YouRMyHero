package flappy.level

import flappy.game.GraphAndPhysEntity
import flappy.input.Input
import flappy.physics.PhysicsContants
import flappy.physics.PhysicsProvider.{BodyDefProvider, FixtureDefProvider, ShapeProvider}
import org.jbox2d.callbacks.{ContactImpulse, ContactListener}
import org.jbox2d.collision.Manifold
import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.contacts.Contact
import org.jbox2d.dynamics.{BodyDef, BodyType, FixtureDef}
import org.lwjgl.glfw.GLFW

class Bird(override val physicsWidthSize: Float,
           override val physicsHeightSize: Float,
           override val physicsPosition: Vec2 = new Vec2(0, 0)) extends GraphAndPhysEntity(physicsWidthSize, physicsHeightSize, physicsPosition) {

  override lazy val fixtureDefProvider: FixtureDefProvider = () => {
    val fixtureDef = new FixtureDef
    fixtureDef.density = 10f
    fixtureDef.restitution = 1f
    fixtureDef
  }

  override lazy val bodyDefProvider: BodyDefProvider = () => {
    val bodyDef = new BodyDef
    bodyDef.`type` = BodyType.DYNAMIC
    bodyDef
  }

  override lazy val shapeProvider: ShapeProvider = () => {
    val shape = new PolygonShape
    shape.setAsBox(physicsWidthSize / PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL / 2, physicsHeightSize / PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL / 2)
    shape
  }

  override def update(): Unit = {
    if (Input.isJustPressed(GLFW.GLFW_KEY_SPACE)) {
      setSpeed(new Vec2(0, 0))
      applyForceAtCenter(new Vec2(0, 650000))
    }
    super.update()
  }

  override lazy val contactListener: Option[ContactListener] = Some(Bird.BirdContactListener)
}

object Bird {

  val BirdContactListener: ContactListener = new ContactListener {
    override def endContact(contact: Contact): Unit = {}

    override def postSolve(contact: Contact, impulse: ContactImpulse): Unit = {}

    override def preSolve(contact: Contact, oldManifold: Manifold): Unit = {}

    override def beginContact(contact: Contact): Unit = println("CONTANT")
  }

}