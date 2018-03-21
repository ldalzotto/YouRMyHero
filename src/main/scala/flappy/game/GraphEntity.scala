package flappy.game

import flappy.graphics.render.Renderable
import flappy.graphics.shader.{Shader, ShaderManager}
import flappy.graphics.texture.{Texture, TextureManager}
import org.jbox2d.common.Vec2
import org.joml.Matrix4f

abstract class GraphEntity(val physicsPosition: Vec2 = new Vec2(0, 0),
                           val vertexPath: String, val fragmentPath: String, val texturePath: String)
  extends Renderable {
  val shader: Shader = ShaderManager.getOrDefine(vertexPath + fragmentPath, () => new Shader(vertexPath, fragmentPath))
  override lazy val texture: Texture = TextureManager.getOrDefine(texturePath, () => new Texture(texturePath))

  shader.setUniformMat4f(Shader.PROJECTION_MATRIX, pr_matrix)
  shader.setUniformli("tex", 1)

  def render(viewMatrix: Matrix4f): Unit = {
    shader.setUniformMat4f(Shader.VIEW_MATTRIX, viewMatrix)
    super.render()
  }

  def render(viewMatrixs: List[Matrix4f]): Unit = {
    viewMatrixs.foreach(render(_))
  }

}
