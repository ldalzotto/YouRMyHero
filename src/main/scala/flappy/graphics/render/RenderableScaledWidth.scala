package flappy.graphics.render

import flappy.graphics.texture.Texture
import flappy.physics.PhysicsContants

trait RenderableScaledWidth {

  val texture: Texture

  lazy val physicsWidthSize: Float = PhysicsContants.DISPLAYED_WIDTH_IN_PHYS_COORD * 2
  lazy val physicsHeightSize: Float = physicsWidthSize * texture.ratio

}
