package flappy.physics.contact

import flappy.game.PhysEntity
import flappy.physics.UserData
import org.jbox2d.callbacks.{ContactImpulse, ContactListener}
import org.jbox2d.collision.Manifold
import org.jbox2d.dynamics.contacts.Contact

trait ContactListenerInstanceRestricted extends ContactListener {

  val initialPhysEntity: PhysEntity

  val physicsContactEnabled: Boolean = true

  val listOfDesiredEntityHandling: Seq[String] = Nil

  val endContactAfterFilter: (Contact) => (Unit) = (contact: Contact) => {}
  val postSolveAfterFilter: (Contact, ContactImpulse) => (Unit) = (contact: Contact, contactImpulse: ContactImpulse) => {}
  val preSolveAfterFilter: (Contact, Manifold) => (Unit) = (contact: Contact, manifold: Manifold) => {}
  val beginContactAfterFilter: (Contact) => (Unit) = (contact: Contact) => {}

  override def endContact(contact: Contact): Unit = {
    Some(contact).filter(currentContactEntityFilter).foreach(endContactAfterFilter)
  }

  override def postSolve(contact: Contact, impulse: ContactImpulse): Unit = {
    Some(contact).filter(currentContactEntityFilter).foreach(postSolveAfterFilter(_, impulse))
  }

  override def preSolve(contact: Contact, oldManifold: Manifold): Unit = {
    Some(contact).filter(isCurrentEntity).foreach(_.setEnabled(physicsContactEnabled))
    Some(contact).filter(currentContactEntityFilter).foreach(preSolveAfterFilter(_, oldManifold))
  }

  override def beginContact(contact: Contact): Unit = {
    Some(contact).filter(currentContactEntityFilter).foreach(beginContactAfterFilter)
  }

  private def isCurrentEntity(contact: Contact): Boolean = {

    val fixtureAUD: UserData = contact.getFixtureA.getUserData.asInstanceOf[UserData]
    val fixtureBUD: UserData = contact.getFixtureB.getUserData.asInstanceOf[UserData]

    checkIfTagAreEquals(fixtureAUD, initialPhysEntity) || checkIfTagAreEquals(fixtureBUD, initialPhysEntity)
  }

  private def currentContactEntityFilter(contact: Contact): Boolean = {

    val fixtureAUD: UserData = contact.getFixtureA.getUserData.asInstanceOf[UserData]
    val fixtureBUD: UserData = contact.getFixtureB.getUserData.asInstanceOf[UserData]

    if (checkIfTagAreEquals(fixtureAUD, initialPhysEntity)) {
      if (checkIfTagIsContainedInDesiredEntityTags(fixtureBUD)) {
        return true
      }
    }
    if (checkIfTagAreEquals(fixtureBUD, initialPhysEntity)) {
      if (checkIfTagIsContainedInDesiredEntityTags(fixtureAUD)) {
        return true
      }
    }

    false
  }

  private def checkIfTagAreEquals(userData: UserData, physEntity: PhysEntity): Boolean = {
    userData.tag == physEntity.userDataTag
  }

  private def checkIfTagIsContainedInDesiredEntityTags(userData: UserData): Boolean = {
    listOfDesiredEntityHandling == Nil || listOfDesiredEntityHandling.contains(userData.tag)
  }

}
