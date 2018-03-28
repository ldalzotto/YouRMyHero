package flappy.physics.contact

import org.jbox2d.callbacks.ContactListener

/**
  * <p>Le trait [[Contactable]] permet d'ajouter une logique de contact via un [[ContactListener]] ([[Contactable.contactListener]]). </p>
  * <p>L'ajout de la gestion du contact dans le monde physique s'effectue grâce au manager [[ContactListeners.addContactListener]].</p>
  */
trait Contactable {

  /**
    * <p>Le contact listener à ajouter.</p>
    */
  val contactListener: Option[ContactListener]
  contactListener.foreach(ContactListeners.addContactListener)

}
