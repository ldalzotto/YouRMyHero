package flappy.game

import flappy.graphics.Camera
import flappy.physics.PhysicsContants
import org.jbox2d.common.Vec2
import org.joml.Matrix4f

trait Scrollable {

  //le width de la texture mise à l'échelle
  val physicsWidthSize: Float

  private val screenWidthInPhysics = (Camera.CAMERA_WIDTH * 2) / PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL

  val scrollPositions: List[Vec2] = {

    val textureNumber = Math.ceil((screenWidthInPhysics + 2 * physicsWidthSize) / physicsWidthSize).toInt

    val initialTexturePosition = -Camera.CAMERA_WIDTH + (physicsWidthSize / 2)

    (for (i <- 0 until textureNumber;
          texturePos = (physicsWidthSize * i) + initialTexturePosition;
          vectors = new Vec2(texturePos, 0))
      yield vectors).toList
  }

  //val scrollPosition: Vec2 = new Vec2(0, 0)
  val scrollSpeed: Vec2 = new Vec2(-60f, 0)

  def scrollViewMatrix(): List[Matrix4f] = {
    scrollPositions.map(pos => new Matrix4f().identity().translate(pos.x * PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL,
      pos.y * PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL, 0))
  }

  def updateScroll(delta: Float): Unit = {

    //debugPrintDelta()

    scrollPositions.foreach(pos => pos.set(pos.add(scrollSpeed.mul(delta))))
    val allTextureThatHaveToMove =
      scrollPositions.filter(pos => ((pos.x + physicsWidthSize / 2)
        * PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL) <= -Camera.CAMERA_WIDTH)

    allTextureThatHaveToMove.foreach(pos => {
      val maxXPosition = getRighetestTexturePosition()
      pos.set(maxXPosition + physicsWidthSize, 0)
    })

    adjustForClipping()
    //.foreach(_.set(Camera.CAMERA_WIDTH + (screenWidthInPhysics / 2), 0))
  }

  def getRighetestTexturePosition(): Float = {
    scrollPositions.map(_.x).max
  }

  def adjustForClipping(): Unit = {

    val orderedList =
      scrollPositions.sortWith((v1, v2) => v1.x <= v2.x)
    (for (i <- 1 until orderedList.length;
          posAndAssociatedDelta = (orderedList(i), Math.abs(orderedList(i - 1).x - orderedList(i).x) - physicsWidthSize))
      yield posAndAssociatedDelta)
      .foreach(posAndDelta => posAndDelta._1.set(posAndDelta._1.add(new Vec2(-posAndDelta._2, 0))))

  }

  def debugPrintDelta(): Unit = {

    println("Start")


    (for (i <- 1 until scrollPositions.length - 1;
          delta = scrollPositions(i).x - scrollPositions(i - 1).x)
      yield delta)
      .foreach(println(_))

    println("End")
  }

}
