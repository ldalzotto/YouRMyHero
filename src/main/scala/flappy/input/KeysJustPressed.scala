package flappy.input

import scala.collection.mutable.ListBuffer

trait KeysJustPressed {

  val keysJustPressed = new Array[Boolean](65536)

  private val keysToCheck: ListBuffer[Int] = ListBuffer.empty

  def keyPushed(keyCode: Int): Unit = {
    keysJustPressed(keyCode) = true
    keysToCheck.append(keyCode)
  }

  def update(): Unit = {
    keysToCheck.foreach(keysJustPressed(_) = false)
    keysToCheck.clear()
  }

  def isJustPressed(keyCode: Int): Boolean = keysJustPressed(keyCode)

}
