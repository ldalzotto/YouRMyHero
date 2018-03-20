package flappy.graphics

object Camera {

  val CAMERA_WIDTH: Float = 20f
  //val RATIO: Float = 16f / 9f
  val RATIO: Float = Screen.SCREEN_WIDTH.toFloat / Screen.SCREEN_HEIGHT.toFloat
  val CAMERA_HEIGHT: Float = CAMERA_WIDTH * (1f / RATIO)

}
