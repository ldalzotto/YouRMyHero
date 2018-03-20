package flappy

import flappy.level.{Bird, Ground}
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.{GL, GL11}
import org.lwjgl.system.MemoryUtil


class AnotherMain extends Runnable {


  override def run(): Unit = {
    init()
    gameLoop()
  }

  val width: Int = 1066
  val height = 600

  var window: Long = _

  var bird: Bird = _
  var bird2: Bird = _

  var body: Body = _
  var ground: Ground = _

  def start(): Unit = {
    val thread = new Thread(this, "Game")
    thread.start()
  }

  def init(): Unit = {
    if (!glfwInit()) {
      return
    }

    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
    window = glfwCreateWindow(width, height, "Flappy", MemoryUtil.NULL, MemoryUtil.NULL)
    if (window == MemoryUtil.NULL) {
      return
    }

    val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
    if (vidMode == null) {
      return
    }

    glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2)

    glfwMakeContextCurrent(window)
    glfwShowWindow(window)
    GL.createCapabilities()

    glClearColor(1f, 1f, 1f, 1f)
    glEnable(GL_DEPTH_TEST)

    glActiveTexture(GL_TEXTURE1)

    ground = new Ground(1f, 1f, physicsPosition = new Vec2(0f, -5f))
    bird = new Bird(2f, 2f)
    bird2 = new Bird(5f, 10f, physicsPosition = new Vec2(0, 5f))

  }

  def gameLoop(): Unit = {

    while (!glfwWindowShouldClose(window)) {

      MyWorld.world.step(0.01f, 8, 6)

      ground.update()
      bird.update()
      bird2.update()

      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
      glfwPollEvents()

      ground.render()
      bird.render()
      bird2.render()
      glfwSwapBuffers(window)


      val error = GL11.glGetError
      if (error != GL11.GL_NO_ERROR) System.out.println(error)


    }

  }


}

object AnotherMain {
  def main(args: Array[String]): Unit = {
    new AnotherMain().start()
  }

}
