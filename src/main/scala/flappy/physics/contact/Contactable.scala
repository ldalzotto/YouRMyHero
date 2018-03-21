package flappy.physics.contact

import org.jbox2d.callbacks.ContactListener

trait Contactable {

  val contactListener: Option[ContactListener]
  contactListener.foreach(ContactListeners.addContactListener)

}
