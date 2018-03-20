package flappy

import org.lwjgl.glfw.GLFW

class Timer(window: Long) {

  var nanoSeconds: Double = _
  var timer: Double = _
  var elapsedTime: Double = _
  var deltaTimeInS: Float = _


  var timeFPSCount: Double = _
  var frames: Int = 0
  var ticks: Int = 0

  var beforeTimeWorldCount: Double = _
  var elapsedTimeWorldCount: Double = _

  def init(): Unit = {
    timer = System.nanoTime()
    timeFPSCount = System.currentTimeMillis()
    beforeTimeWorldCount = System.nanoTime()
    elapsedTime = 0d
    elapsedTimeWorldCount = 0d
    nanoSeconds = 1000000000.0 / 60
  }

  def updateTimer(renderer: () => Unit, worldUpdater: (Float) => Unit): Unit = {

    val now = System.nanoTime()

    elapsedTimeWorldCount = now - beforeTimeWorldCount
    elapsedTime = now - timer


    if (elapsedTimeWorldCount > MyWorld.WORLD_STEP_S / 1000000000) {

      ticks = ticks + 1
      beforeTimeWorldCount = beforeTimeWorldCount + (MyWorld.WORLD_STEP_S / 1000000000)
      worldUpdater.apply(MyWorld.WORLD_STEP_S)

      elapsedTimeWorldCount = elapsedTimeWorldCount - beforeTimeWorldCount

    }

    if (elapsedTime > nanoSeconds) {
      timer = timer + nanoSeconds
      frames = frames + 1
      renderer.apply()
      elapsedTime = elapsedTime - nanoSeconds
    }

    if (System.currentTimeMillis() - timeFPSCount > 1000) {


      timeFPSCount += 1000
      GLFW.glfwSetWindowTitle(window, s"UPS : $ticks, FPS : $frames")

      frames = 0
      ticks = 0

    }

  }

}
