package flappy.physics.contact

import flappy.game.PhysEntity
import flappy.physics.UserData
import org.jbox2d.callbacks.{ContactImpulse, ContactListener}
import org.jbox2d.collision.Manifold
import org.jbox2d.dynamics.contacts.Contact

/**
  * <p>[[ContactListenerInstanceRestricted]] est un contact listener pour lequel un filtre sur les contact est appliqué.
  * Ce filtre est représenté par une liste de tag représentatif de [[flappy.physics.Moveable.userDataTag]].</p>
  * <p>Seule les entitées physiques en correspondance avec ces tags verront la logique de contact appliquée.</p>
  */
trait ContactListenerInstanceRestricted extends ContactListener {

  /**
    * <p>L'entitée physique "source", c-à-d, l'entitée propriétaire du contact listener.</p>
    */
  val initialPhysEntity: PhysEntity

  /**
    * <p>Précise si les contacts autres que ceux filtrés grâce à [[ContactListenerInstanceRestricted.listOfDesiredEntityHandling]] sont activés.</p>
    */
  val physicsContactEnabled: Boolean = true

  /**
    * <p>La liste des tag appliquant le filtre sur certaines entités. (représentatif de [[flappy.physics.Moveable.userDataTag]])</p>
    */
  val listOfDesiredEntityHandling: Seq[String] = Nil

  /**
    * <p>Callback implémentant la logique de endContact après application du filtre.
    * (AnyRef) fait référence à l'entité physique en contact avec [[initialPhysEntity]].</p>
    */
  val endContactAfterFilter: (Contact, AnyRef) => (Unit) = (contact: Contact, contactedEntity: AnyRef) => {}

  /**
    * <p>Callback implémentant la logique de postSolveContact après application du filtre.
    * (AnyRef) fait référence à l'entité physique en contact avec [[initialPhysEntity]].</p>
    */
  val postSolveAfterFilter: (Contact, ContactImpulse, AnyRef) => (Unit) = (contact: Contact, contactImpulse: ContactImpulse, contactedEntity: AnyRef) => {}

  /**
    * <p>Callback implémentant la logique de preSolveContact après application du filtre.
    * (AnyRef) fait référence à l'entité physique en contact avec [[initialPhysEntity]].</p>
    */
  val preSolveAfterFilter: (Contact, Manifold, AnyRef) => (Unit) = (contact: Contact, manifold: Manifold, contactedEntity: AnyRef) => {}

  /**
    * <p>Callback implémentant la logique de beginContact après application du filtre.
    * (AnyRef) fait référence à l'entité physique en contact avec [[initialPhysEntity]].</p>
    */
  val beginContactAfterFilter: (Contact, AnyRef) => (Unit) = (contact: Contact, contactedEntity: AnyRef) => {}

  override def endContact(contact: Contact): Unit = {
    Some(contact)
      .map(retriveContactedEntityAfterFilter)
      .filter(_ != null)
      .foreach(endContactAfterFilter(contact, _))
  }

  override def postSolve(contact: Contact, impulse: ContactImpulse): Unit = {
    Some(contact)
      .map(retriveContactedEntityAfterFilter)
      .filter(_ != null)
      .foreach(postSolveAfterFilter(contact, impulse, _))
  }

  override def preSolve(contact: Contact, oldManifold: Manifold): Unit = {
    Some(contact).filter(isCurrentEntity).foreach(_.setEnabled(physicsContactEnabled))
    Some(contact)
      .map(retriveContactedEntityAfterFilter)
      .filter(_ != null)
      .foreach(preSolveAfterFilter(contact, oldManifold, _))
  }

  override def beginContact(contact: Contact): Unit = {
    Some(contact)
      .map(retriveContactedEntityAfterFilter)
      .filter(_ != null)
      .foreach(beginContactAfterFilter(contact, _))
  }

  private def isCurrentEntity(contact: Contact): Boolean = {

    val fixtureAUD: UserData = contact.getFixtureA.getUserData.asInstanceOf[UserData]
    val fixtureBUD: UserData = contact.getFixtureB.getUserData.asInstanceOf[UserData]

    checkIfTagAreEquals(fixtureAUD, initialPhysEntity) || checkIfTagAreEquals(fixtureBUD, initialPhysEntity)
  }

  private def retriveContactedEntityAfterFilter(contact: Contact): AnyRef = {
    val fixtureAUD: UserData = contact.getFixtureA.getUserData.asInstanceOf[UserData]
    val fixtureBUD: UserData = contact.getFixtureB.getUserData.asInstanceOf[UserData]

    if (checkIfTagAreEquals(fixtureAUD, initialPhysEntity)) {
      if (checkIfTagIsContainedInDesiredEntityTags(fixtureBUD)) {
        return fixtureBUD.instance
      }
    }
    if (checkIfTagAreEquals(fixtureBUD, initialPhysEntity)) {
      if (checkIfTagIsContainedInDesiredEntityTags(fixtureAUD)) {
        return fixtureAUD.instance
      }
    }

    null
  }

  private def checkIfTagAreEquals(userData: UserData, physEntity: PhysEntity): Boolean = {
    userData.tag == physEntity.userDataTag
  }

  private def checkIfTagIsContainedInDesiredEntityTags(userData: UserData): Boolean = {
    listOfDesiredEntityHandling == Nil || listOfDesiredEntityHandling.contains(userData.tag)
  }

}
