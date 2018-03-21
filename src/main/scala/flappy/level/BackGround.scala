package flappy.level

import flappy.game.GraphEntity
import flappy.graphics.render.RenderableScaledHeight
import flappy.graphics.scroll.XScrollable
import org.jbox2d.common.Vec2

class BackGround(override val physicsPosition: Vec2 = new Vec2(0, 0))
  extends GraphEntity(physicsPosition, "shaders/bird.vert", "shaders/bird.frag", "res/bg.jpeg")
    with RenderableScaledHeight
    with XScrollable {

  override lazy val scrollSpeed: Vec2 = new Vec2(-10f, 0f)

  def update(delta: Float): Unit = {
    updateScroll(delta)
  }

  override def render(): Unit = {
    val viewmatrixs = scrollViewMatrix()
    super.render(viewmatrixs)
  }

}
