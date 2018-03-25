package flappy.physics.definitions

import org.jbox2d.dynamics.FixtureDef

object FixtureDefDefinitions {

  def createFixtureDef(density: Option[Float], isSensor: Option[Boolean], resitution: Option[Float]): FixtureDef = {
    val fixtureDef = new FixtureDef
    density.foreach(fixtureDef.density = _)
    isSensor.foreach(fixtureDef.isSensor = _)
    resitution.foreach(fixtureDef.restitution = _)
    fixtureDef
  }

}
