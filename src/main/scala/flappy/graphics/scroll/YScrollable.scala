package flappy.graphics.scroll

import flappy.graphics.Camera
import flappy.graphics.scroll.Scrollable._
import org.jbox2d.common.Vec2

trait YScrollable extends Scrollable {
  override lazy val scrollSpeed: Vec2 = new Vec2(0, -20)
  override lazy val textureNumberProvider: TextureNumberProvider = () => {
    val screenHeightWithLeftAndRightTextures = 2 * (screenHeightInPhysics + physicsHeightSize)
    val textureNumber = Math.ceil(screenHeightWithLeftAndRightTextures / physicsHeightSize).toInt
    textureNumber
  }
  override lazy val firstTextureInFromPositionProvider: FirstTextureInFromPositionProvider =
    () => new Vec2(0, Camera.CAMERA_HEIGHT - (physicsHeightSize / 2))

  override lazy val whenTextureHaveToRespawn: WhenTextureHaveToRespawn = pos => (pos.y + physicsHeightSize / 2) <= -screenHeightInPhysics
  override lazy val deltaPosAppliedToTexturesHaveToRespawn: DeltaPosAppliedToTexturesHaveToRespawn = () => {
    val maxYPosition = scrollPositions.map(_.y).max
    new Vec2(0, maxYPosition + physicsHeightSize)
  }
  override lazy val deltaForClippingApplyer: DeltaForClippingApplyer = {
    //TODO repositioning clipping is bugged for YScrolling
    case (position, (_, deltaY)) => position
  }
}
