package flappy

import flappy.graphics.Screen
import flappy.level.{BackGround, Bird, Ground}
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


  var window: Long = _

  var timer: Timer = _

  var bird: Bird = _
  var bird2: Bird = _

  var backgounrd: BackGround = _

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
    window = glfwCreateWindow(Screen.SCREEN_WIDTH, Screen.SCREEN_HEIGHT, "Flappy", MemoryUtil.NULL, MemoryUtil.NULL)
    if (window == MemoryUtil.NULL) {
      return
    }

    val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
    if (vidMode == null) {
      return
    }

    glfwSetWindowPos(window, (vidMode.width() - Screen.SCREEN_WIDTH) / 2, (vidMode.height() - Screen.SCREEN_HEIGHT) / 2)

    glfwMakeContextCurrent(window)
    glfwShowWindow(window)
    GL.createCapabilities()

    glClearColor(1f, 1f, 1f, 1f)
    glEnable(GL_DEPTH_TEST)

    glActiveTexture(GL_TEXTURE1)

    ground = new Ground(1f, 1f, physicsPosition = new Vec2(0f, -5f))
    bird = new Bird(2f, 2f)
    bird2 = new Bird(5f, 10f, physicsPosition = new Vec2(0, 5f))

    backgounrd = new BackGround()

    timer = new Timer(window)
  }

  def gameLoop(): Unit = {

    timer.init()

    while (!glfwWindowShouldClose(window)) {


      glfwPollEvents()

      timer.updateTimer(() => {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
        ground.render()
        bird.render()
        bird2.render()
        backgounrd.render()

        glfwSwapBuffers(window)
      }, (delta) => {
        MyWorld.world.step(delta, 8, 3)
        ground.update()
        backgounrd.update(delta)
        bird.update()
        bird2.update()
      })


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
