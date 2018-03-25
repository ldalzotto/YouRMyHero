package flappy.level

import flappy.game.{GraphAndPhysEntity, PhysEntity}
import flappy.level.constants.ScrollSpeeds
import flappy.physics.PhysicsContants
import flappy.physics.PhysicsProvider.{BodyDefProvider, FixtureDefProvider, ShapeProvider}
import flappy.physics.contact.ContactListenerInstanceRestricted
import flappy.physics.definitions.FixtureDefDefinitions
import flappy.physics.shape.RectangleBody
import org.jbox2d.callbacks.ContactListener
import org.jbox2d.collision.Manifold
import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.contacts.Contact
import org.jbox2d.dynamics.{BodyDef, BodyType}
import org.joml.Matrix4f

class Target(override val physicsWidthSize: Float,
             override val physicsHeightSize: Float,
             override val initialPhysicsPosition: Vec2 = new Vec2(0, 0))
  extends GraphAndPhysEntity(physicsWidthSize, physicsHeightSize, initialPhysicsPosition, "shaders/bird.vert", "shaders/bird.frag", "res/hero/hero.png") with RectangleBody {

  override lazy val userDataTag: String = Target.USER_DATA_TAG

  def update(delta: Float): Unit = {
    body.setLinearVelocity(ScrollSpeeds.GROUND_SPEED)
    super.update()
  }

  override def render(): Unit = super.render(new Matrix4f().identity())

  override lazy val fixtureDefProvider: FixtureDefProvider =
    () => FixtureDefDefinitions.createFixtureDef(Some(10f), None, Some(1f))

  override lazy val bodyDefProvider: BodyDefProvider = () => {
    val bodyDef = new BodyDef
    bodyDef.`type` = BodyType.DYNAMIC
    bodyDef.gravityScale = 0f
    bodyDef
  }

  override lazy val shapeProvider: ShapeProvider = () => {
    val shape = new PolygonShape
    shape.setAsBox(physicsWidthSize / PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL / 2, physicsHeightSize / PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL / 2)
    shape
  }

  override lazy val contactListener: Option[ContactListener] = Some(Target.targetContactListener(this))

}

object Target {

  val USER_DATA_TAG: String = Target.getClass.getSimpleName

  def targetContactListener(instance: Target): ContactListener = new ContactListenerInstanceRestricted {
    override val initialPhysEntity: PhysEntity = instance
    override val listOfDesiredEntityHandling: Seq[String] = Seq(Bird.USER_DATA_TAG)
    override val physicsContactEnabled: Boolean = false
    override val preSolveAfterFilter: (Contact, Manifold) => Unit = (contact, _) => {
      println(s"Target $instance entered in contact:")
    }
  }

}
