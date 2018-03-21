package flappy.graphics.scroll

import flappy.graphics.Camera
import flappy.graphics.scroll.Scrollable._
import flappy.implicits.Vec2Implicits._
import flappy.physics.PhysicsContants
import org.jbox2d.common.Vec2
import org.joml.Matrix4f

trait Scrollable {

  val textureNumberProvider: TextureNumberProvider
  val firstTextureInFromPositionProvider: FirstTextureInFromPositionProvider
  val whenTextureHaveToRespawn: WhenTextureHaveToRespawn
  val deltaPosAppliedToTexturesHaveToRespawn: DeltaPosAppliedToTexturesHaveToRespawn
  val deltaForClippingApplyer: DeltaForClippingApplyer

  //le width de la texture mise à l'échelle
  val physicsWidthSize: Float
  val physicsHeightSize: Float

  val screenWidthInPhysics: Float = Camera.CAMERA_WIDTH / PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL
  val screenHeightInPhysics: Float = Camera.CAMERA_HEIGHT / PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL

  val scrollSpeed: Vec2
  assert(scrollSpeed.x == 0 || scrollSpeed.y == 0, "ScrollSpeed must be for Y or X only. 2D scroll is not supported.")

  private val normalizedSpeed = scrollSpeed.clone().abs()
  normalizedSpeed.normalize()

  val scrollPositions: List[Vec2] = {
    val textureNumber = textureNumberProvider.apply()
    val firstTexturePos = firstTextureInFromPositionProvider.apply()

    (for (i <- 0 until textureNumber;
          texturePos = firstTexturePos.add(normalizedSpeed.scl(new Vec2(physicsWidthSize * i, physicsHeightSize * i))))
      yield texturePos).toList
  }


  def scrollViewMatrix(): List[Matrix4f] = {
    scrollPositions.map(pos => new Matrix4f().identity().translate(pos.x * PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL,
      pos.y * PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL, 0))
  }

  def updateScroll(delta: Float): Unit = {

    scrollPositions.foreach(pos => pos.set(pos.add(scrollSpeed.mul(delta))))

    val allTextureThatHaveToMove =
      scrollPositions.filter(whenTextureHaveToRespawn)

    allTextureThatHaveToMove
      .foreach(_.set(deltaPosAppliedToTexturesHaveToRespawn.apply()))

    adjustForClipping()
  }

  private def adjustForClipping(): Unit = {

    val orderedList =
      scrollPositions.sortWith((v1, v2) => v1.x <= v2.x)
        .sortWith((v1, v2) => v1.y <= v1.y)
    (for (i <- 1 until orderedList.length;
          xDelta = Math.abs(orderedList(i - 1).x - orderedList(i).x) - physicsWidthSize;
          yDelta = Math.abs(orderedList(i - 1).y - orderedList(i).y) - physicsHeightSize;
          posAndAssociatedDelta = (orderedList(i), (xDelta, yDelta)))
      yield posAndAssociatedDelta)
      .foreach(deltaForClippingApplyer)
    //.foreach({ case (position, (deltaX, deltaY)) => position.set(position.add(new Vec2(-deltaX, -deltaY))) })
  }

}

object Scrollable {
  type TextureNumberProvider = () => Int
  type FirstTextureInFromPositionProvider = () => Vec2
  type WhenTextureHaveToRespawn = (Vec2) => Boolean
  type DeltaPosAppliedToTexturesHaveToRespawn = () => Vec2

  type PositionAndDelta = (Vec2, (Float, Float))
  type DeltaForClippingApplyer = (PositionAndDelta) => Unit

}
