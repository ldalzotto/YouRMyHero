package flappy

import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.World

object MyWorld {

  val world: World = new World(new Vec2(0, -10))

}
