package flappy.graphics

import org.joml.Matrix4f

object Camera {

  val CAMERA_WIDTH: Float = 50f

  //val RATIO: Float = 16f / 9f
  var RATIO: Float = Screen.SCREEN_WIDTH.toFloat / Screen.SCREEN_HEIGHT.toFloat
  var pr_matrix: Matrix4f = createProjectionMatrix()

  var CAMERA_HEIGHT: Float = CAMERA_WIDTH * (1f / RATIO)

  private def createProjectionMatrix(): Matrix4f = {
    new Matrix4f().ortho(-Camera.CAMERA_WIDTH, Camera.CAMERA_WIDTH, -Camera.CAMERA_WIDTH * (1 / Camera.RATIO), Camera.CAMERA_WIDTH * (1 / Camera.RATIO), -1, 1)
  }

}
