package flappy.physics.contact

import flappy.MyWorld
import org.jbox2d.callbacks.{ContactImpulse, ContactListener}
import org.jbox2d.collision.Manifold
import org.jbox2d.dynamics.contacts.Contact

import scala.collection.mutable.ListBuffer

/**
  * <p>Cet objet est un manager de [[ContactListener]]. Il permet de combiner plusieurs comportements au sein d'un unique
  * contact listener.</p>
  * <p>L'ensemble des comportements ajoutés au monde physique est assuré par le parcours d'une liste de contact listener : [[ContactListeners.contactListeners]].</p>
  * <p>Seule l'instance [[ContactListeners]] est associée au monde physique [[MyWorld.world.setContactListener]]</p>
  */
object ContactListeners extends ContactListener {

  /**
    * <p>La liste des [[ContactListener]] regroupant l'ensemble des différentes logiques de contact.</p>
    */
  private val contactListeners: ListBuffer[ContactListener] = ListBuffer.empty

  /**
    * <p>Méthode permettant d'ajouter un contact au monde physique et à [[ContactListeners.contactListeners]].</p>
    *
    * @param contactListener Le contact listener à ajouter
    */
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
