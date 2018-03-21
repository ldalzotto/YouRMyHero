package flappy.graphics.scroll

import flappy.graphics.Camera
import flappy.graphics.scroll.Scrollable._
import org.jbox2d.common.Vec2

/**
  * [[XScrollable]] Définit les différents propriétés de [[Scrollable]] pour autoriser une scrolling vertical.
  */
trait XScrollable extends Scrollable {

  val physicsPosition: Vec2 = new Vec2(0, 0)

  override lazy val textureNumberProvider: TextureNumberProvider = () => {
    val screenWidthWithLeftAndRightTextures = 2 * (screenWidthInPhysics + physicsWidthSize)
    val textureNumber = Math.ceil(screenWidthWithLeftAndRightTextures / physicsWidthSize).toInt
    textureNumber
  }

  override lazy val firstTextureInFromPositionProvider: FirstTextureInFromPositionProvider =
    () => new Vec2(-Camera.CAMERA_WIDTH + (physicsWidthSize / 2), physicsPosition.y)

  override lazy val whenTextureHaveToRespawn: WhenTextureHaveToRespawn = pos => (pos.x + physicsWidthSize / 2) <= -screenWidthInPhysics

  override lazy val deltaPosAppliedToTexturesHaveToRespawn: DeltaPosAppliedToTexturesHaveToRespawn = () => {
    val maxXPosition = scrollPositions.map(_.x).max
    new Vec2(maxXPosition + physicsWidthSize, physicsPosition.y)
  }
  override lazy val deltaForClippingApplyer: DeltaForClippingApplyer = {
    case (position, (deltaX, _)) => position.set(position.add(new Vec2(-deltaX, 0)))
  }
  override val positionSorterForAntiClipping: PositionSorterForAntiClipping = (v1, v2) => v1.x <= v2.x
}
