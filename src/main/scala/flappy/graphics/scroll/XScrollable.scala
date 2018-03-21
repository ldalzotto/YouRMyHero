package flappy.graphics.scroll

import flappy.graphics.Camera
import flappy.graphics.scroll.Scrollable._
import org.jbox2d.common.Vec2

trait XScrollable extends Scrollable {

  override lazy val textureNumberProvider: TextureNumberProvider = () => {
    val screenWidthWithLeftAndRightTextures = 2 * (screenWidthInPhysics + physicsWidthSize)
    val textureNumber = Math.ceil(screenWidthWithLeftAndRightTextures / physicsWidthSize).toInt
    textureNumber
  }

  override lazy val firstTextureInFromPositionProvider: FirstTextureInFromPositionProvider =
    () => new Vec2(-Camera.CAMERA_WIDTH + (physicsWidthSize / 2), 0)

  override lazy val scrollSpeed: Vec2 = new Vec2(-25f, 0)

  override lazy val whenTextureHaveToRespawn: WhenTextureHaveToRespawn = pos => (pos.x + physicsWidthSize / 2) <= -screenWidthInPhysics

  override lazy val deltaPosAppliedToTexturesHaveToRespawn: DeltaPosAppliedToTexturesHaveToRespawn = () => {
    val maxXPosition = scrollPositions.map(_.x).max
    new Vec2(maxXPosition + physicsWidthSize, 0)
  }
  override lazy val deltaForClippingApplyer: DeltaForClippingApplyer = {
    case (position, (deltaX, _)) => position.set(position.add(new Vec2(-deltaX, 0)))
  }
}
