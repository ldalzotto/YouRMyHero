package flappy.graphics.texture

import java.io.FileInputStream
import javax.imageio.ImageIO

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.{glGenTextures, _}
import org.lwjgl.opengl.GL30._

class Texture(path: String) {

  var width: Int = _
  var height: Int = _
  var ratio: Float = _

  val textureId: Int = load(path)

  def load(path: String): Int = {
    val image = ImageIO.read(new FileInputStream(path))
    width = image.getWidth
    height = image.getHeight
    ratio = width.toFloat / height.toFloat
    val pixels = new Array[Int](width * height)
    image.getRGB(0, 0, width, height, pixels, 0, width)
    val data = new Array[Int](width * height)
    var i = 0
    while ( {
      i < width * height
    }) {
      val a = (pixels(i) & 0xff000000) >> 24
      val r = (pixels(i) & 0xff0000) >> 16
      val g = (pixels(i) & 0xff00) >> 8
      val b = pixels(i) & 0xff
      data(i) = a << 24 | b << 16 | g << 8 | r

      {
        i += 1
        i - 1
      }
    }

    val result = glGenTextures()


    val textureBuffer = BufferUtils.createIntBuffer(data.length)
    textureBuffer.put(data).flip()

    glBindTexture(GL_TEXTURE_2D, result)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, textureBuffer)
    glGenerateMipmap(GL_TEXTURE_2D)
    glBindTexture(GL_TEXTURE_2D, 0)
    result
  }

  def bind(): Unit = {
    glBindTexture(GL_TEXTURE_2D, textureId)
  }

  def unbind(): Unit = {
    glBindTexture(GL_TEXTURE_2D, 0)
  }

}
