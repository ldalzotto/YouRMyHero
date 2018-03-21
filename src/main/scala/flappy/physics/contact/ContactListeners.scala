package flappy.physics.contact

import flappy.MyWorld
import org.jbox2d.callbacks.{ContactImpulse, ContactListener}
import org.jbox2d.collision.Manifold
import org.jbox2d.dynamics.contacts.Contact

import scala.collection.mutable.ListBuffer

object ContactListeners extends ContactListener {

  val contactListeners: ListBuffer[ContactListener] = ListBuffer.empty

  def addContactListener(contactListener: ContactListener): Unit = {
    if (!contactListeners.contains(contactListener)) {
      contactListeners.append(contactListener)
    }

    //TODO useful ?
    MyWorld.world.setContactListener(this)
  }

  override def endContact(contact: Contact): Unit = {
    contactListeners.foreach(_.endContact(contact))
  }

  override def postSolve(contact: Contact, impulse: ContactImpulse): Unit = {
    contactListeners.foreach(_.postSolve(contact, impulse))
  }

  override def preSolve(contact: Contact, oldManifold: Manifold): Unit = {
    contactListeners.foreach(_.preSolve(contact, oldManifold))
  }

  override def beginContact(contact: Contact): Unit = {
    contactListeners.foreach(_.beginContact(contact))
  }
}
