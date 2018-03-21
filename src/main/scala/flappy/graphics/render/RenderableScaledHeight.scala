package flappy.graphics.render

import flappy.graphics.texture.Texture
import flappy.physics.PhysicsContants

trait RenderableScaledHeight {

  val texture: Texture

  lazy val physicsHeightSize: Float = PhysicsContants.DISPLAYED_HEIGHT_IN_PHYS_COORD * 2
  lazy val physicsWidthSize: Float = physicsHeightSize * texture.ratio
}
