package flappy.game

import flappy.graphics.render.Renderable
import flappy.graphics.shader.{Shader, ShaderManager}
import flappy.graphics.texture.{Texture, TextureManager}
import org.joml.Matrix4f

trait GraphEntity extends Renderable {

  val vertexPath: String
  val fragmentPath: String
  val texturePath: String

  val shader: Shader = ShaderManager.getOrDefine(vertexPath + fragmentPath, () => new Shader(vertexPath, fragmentPath))
  shader.setUniformMat4f(Shader.PROJECTION_MATRIX, pr_matrix)
  shader.setUniformli("tex", 1)

  override lazy val texture: Texture = TextureManager.getOrDefine(texturePath, () => new Texture(texturePath))

  def render(viewMatrix: Matrix4f): Unit = {
    shader.setUniformMat4f(Shader.VIEW_MATTRIX, viewMatrix)
    super.render()
  }

  def render(viewMatrixs: List[Matrix4f]): Unit = {
    viewMatrixs.foreach(render(_))
  }
}
