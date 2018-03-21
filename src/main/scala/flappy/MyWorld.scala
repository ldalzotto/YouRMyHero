package flappy

import flappy.graphics.Camera
import flappy.physics.PhysicsContants
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.World

object MyWorld {

  val world: World = new World(new Vec2(0, -100))
  val WORLD_STEP_S = 0.015f

  val DISPLAYED_PHYSICS_WIDTH = Camera.CAMERA_WIDTH / PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL


}
