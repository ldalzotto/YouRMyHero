package flappy.physics.shape

import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.dynamics.Body

trait RectangleBody {

  val body: Body

  assert(body.m_fixtureCount == 1)
  assert(body.m_fixtureList.m_shape.isInstanceOf[PolygonShape])

  private val shape: PolygonShape = body.m_fixtureList.m_shape.asInstanceOf[PolygonShape]

  def initialLeftWorld(): PolygonSide = PolygonSide(shape.m_vertices(0), shape.m_vertices(1)).offset(body.m_xf.p)

  def initialTopWorld(): PolygonSide = PolygonSide(shape.m_vertices(1), shape.m_vertices(2)).offset(body.m_xf.p)

  def initialRightWorld(): PolygonSide = PolygonSide(shape.m_vertices(2), shape.m_vertices(3)).offset(body.m_xf.p)

  def initialBottomWorld(): PolygonSide = PolygonSide(shape.m_vertices(3), shape.m_vertices(0)).offset(body.m_xf.p)

}
