package flappy.level

import flappy.game.GraphEntity
import flappy.graphics.render.RenderableScaledHeight
import flappy.graphics.scroll.XScrollable
import flappy.level.constants.ScrollSpeeds
import org.jbox2d.common.Vec2

class BackGround(override val physicsPosition: Vec2 = new Vec2(0, 0))
  extends GraphEntity
    with RenderableScaledHeight
    with XScrollable {

  override lazy val vertexPath: String = "shaders/bird.vert"
  override lazy val fragmentPath: String = "shaders/bird.frag"
  override lazy val texturePath: String = "res/town.png"

  override lazy val scrollSpeed: Vec2 = ScrollSpeeds.BACKGROUND_SPEED

  def update(delta: Float): Unit = {
    updateScroll(delta)
  }

  override def render(): Unit = {
    val viewmatrixs = scrollViewMatrix()
    super.render(viewmatrixs)
  }

}
