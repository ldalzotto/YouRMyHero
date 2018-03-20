package flappy.graphics

trait Renderable {

  val mesh: VertexArray
  val shader: Shader
  val texture: Texture

  def render(): Unit = {

    shader.bind()
    texture.bind()
    mesh.bind()
    mesh.draw()
    mesh.unbind()
    texture.unbind()
    shader.unbind()

  }

}
