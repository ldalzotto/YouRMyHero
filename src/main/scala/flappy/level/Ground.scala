package flappy.level

import flappy.game.GraphAndPhysEntity
import flappy.graphics.scroll.XScrollable
import flappy.physics.PhysicsContants
import flappy.physics.PhysicsProvider.{BodyDefProvider, FixtureDefProvider, ShapeProvider}
import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.{BodyDef, BodyType, FixtureDef}
import org.joml.Matrix4f

class Ground(override val physicsWidthSize: Float,
             override val physicsHeightSize: Float, override val physicsPosition: Vec2 = new Vec2(0, 0))
  extends GraphAndPhysEntity(physicsWidthSize, physicsHeightSize, physicsPosition, "shaders/bird.vert", "shaders/bird.frag", "res/ground.png")
    with XScrollable {

  override lazy val scrollSpeed: Vec2 = new Vec2(-40, 0)

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

  def update(delta: Float): Unit = {
    super.updateScroll(delta)
    super.update()
  }

  override def render(): Unit = {
    val viewmatrixs = scrollViewMatrix()
    viewmatrixs.foreach(scrollMatrix => super.render(new Matrix4f().identity(), m => scrollMatrix))
  }

  override lazy val shapeProvider: ShapeProvider = () => {
    val shape = new PolygonShape
    shape.setAsBox(PhysicsContants.DISPLAYED_WIDTH_IN_PHYS_COORD / PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL,
      physicsHeightSize / PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL / 2)
    shape
  }

}
