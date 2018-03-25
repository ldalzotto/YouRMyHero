package flappy.physics.contact

import flappy.game.PhysEntity
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
    (contact.getFixtureA.getUserData == initialPhysEntity.userDataTag) || (contact.getFixtureB.getUserData == initialPhysEntity.userDataTag)
  }

  private def currentContactEntityFilter(contact: Contact): Boolean = {

    if (contact.getFixtureA.getUserData == initialPhysEntity.userDataTag) {
      if (listOfDesiredEntityHandling == Nil || listOfDesiredEntityHandling.contains(contact.getFixtureB.getUserData)) {
        return true
      }
    }
    if (contact.getFixtureB.getUserData == initialPhysEntity.userDataTag) {
      if (listOfDesiredEntityHandling == Nil || listOfDesiredEntityHandling.contains(contact.getFixtureA.getUserData)) {
        return true
      }
    }
    false
  }

}
