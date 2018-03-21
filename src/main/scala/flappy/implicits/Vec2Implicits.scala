package flappy.implicits

import org.jbox2d.common.Vec2

object Vec2Implicits {

  implicit class Vec2Scl(vec2This: Vec2) {
    def scl(vec2That: Vec2): Vec2 = {
      vec2This.clone().set(vec2This.x * vec2That.x, vec2This.y * vec2That.y)
    }
  }

}
