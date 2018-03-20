package flappy.graphics.render

import flappy.physics.PhysicsContants

trait RenderableScaledHeight extends Renderable {
  override lazy val physicsHeightSize: Float = PhysicsContants.DISPLAYED_HEIGHT_IN_PHYS_COORD * 2
  override lazy val physicsWidthSize: Float = physicsHeightSize * texture.ratio
}
