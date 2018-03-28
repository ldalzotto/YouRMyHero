package flappy.physics.shape

import org.jbox2d.common.Vec2

case class PolygonSide(p1: Vec2, p2: Vec2) {
  def center(): Vec2 = {
    new Vec2(
      Math.min(p2.x, p1.x) + Math.abs(p2.x - p1.x) / 2,
      Math.min(p2.y, p1.y) + Math.abs(p2.y - p1.y) / 2
    )
  }

  def distance(): Vec2 = {
    new Vec2(
      Math.abs(p2.x - p1.x),
      Math.abs(p2.y - p2.y)
    )
  }

  def offset(offset: Vec2): PolygonSide = {
    PolygonSide(p1.add(offset), p2.add(offset))
  }

}
