package flappy.graphics.scroll

import flappy.graphics.Camera
import flappy.graphics.scroll.Scrollable._
import flappy.implicits.Vec2Implicits._
import flappy.physics.PhysicsContants
import org.jbox2d.common.Vec2
import org.joml.Matrix4f

/**
  * <p>[[Scrollable]] permet de générer des matrices de vue correspondant à la translation rectiligne d'une ou plusieurs positions
  * à une certaine vitesse. </p>
  * <p>
  * Le système de scroll possède égalment une correction anti-clipping permettant d'ajuster les positions si une desynchronisation
  * de déplacement survient.
  * </p>
  */
trait Scrollable {

  /**
    * <p>Permet de récupérer le nombre de texture à traiter par le scroll.</p>
    */
  val textureNumberProvider: TextureNumberProvider

  /**
    * <p>Permet de récupérer la première position initiale dans le sens du scroll.</p>
    */
  val firstTextureInFromPositionProvider: FirstTextureInFromPositionProvider

  /**
    * <p>Critère permettant de déterminer si une position doit être mise en fin de scroll.</p>
    */
  val whenTextureHaveToRespawn: WhenTextureHaveToRespawn

  /**
    * <p>Permet de calculer de delta de position qui sera appliqué lors du renouveau d'une position de scroll.</p>
    */
  val deltaPosAppliedToTexturesHaveToRespawn: DeltaPosAppliedToTexturesHaveToRespawn

  /**
    * <p>Permet d'activer ou non l'anti clipping
    */
  val antiClippingActivated: Boolean = true

  /**
    * <p>Permet d'appliquer le delta de position pour l'anti-clipping.</p>
    */
  val deltaForClippingApplyer: DeltaForClippingApplyer

  /**
    * <p>Permet de trier par ordre croissant les positions [[Scrollable.scrollPositions]] afin d'être traitée par la calcul des deltas.</p>
    */
  val positionSorterForAntiClipping: PositionSorterForAntiClipping

  /**
    * <p>La largeur de la texture sur l'échelle du monde physique.</p>
    */
  val physicsWidthSize: Float

  /**
    * <p>La hauteur de la texture sur l'échelle du monde physique.</p>
    */
  val physicsHeightSize: Float

  /**
    * <p>La hauteur de la fenêtre de jeu sur l'échelle du monde physique.</p>
    */
  val screenWidthInPhysics: Float = Camera.CAMERA_WIDTH / PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL

  /**
    * <p>La largeur de la fenêtre de jeu sur l'échelle du monde physique.</p>
    */
  val screenHeightInPhysics: Float = Camera.CAMERA_HEIGHT / PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL

  /**
    * <p>La vitesse de scroll</p>
    */
  val scrollSpeed: Vec2
  assert(scrollSpeed.x == 0 || scrollSpeed.y == 0, "ScrollSpeed must be for Y or X only. 2D scroll is not supported.")

  private val normalizedSpeed = scrollSpeed.clone().abs()
  normalizedSpeed.normalize()

  /**
    * <p>
    * Les différentes positions des éléments à scroller.
    * </p>
    * <ol>
    * <li>Récupération du nombre de texture via [[Scrollable.textureNumberProvider]]</li>
    * <li>Récupération de la position initiale de la première texture via [[Scrollable.firstTextureInFromPositionProvider]]</li>
    * <li>Déduction des autres positions initiales</li>
    * </ol>
    */
  val scrollPositions: List[Vec2] = {
    val textureNumber = textureNumberProvider.apply()
    val firstTexturePos = firstTextureInFromPositionProvider.apply()

    (for (i <- 0 until textureNumber;
          texturePos = firstTexturePos.add(normalizedSpeed.scl(new Vec2(physicsWidthSize * i, physicsHeightSize * i))))
      yield texturePos).toList
  }


  /**
    * <p>Calcule l'ensemble des matrices de vues correspondant aux translations du scroll. Ces données sont calculées à partir
    * de [[Scrollable.scrollPositions]]</p>
    *
    * @return Les matrices de vues des translations de scroll.
    */
  def scrollViewMatrix(): List[Matrix4f] = {
    scrollPositions.map(pos => new Matrix4f().identity().translate(pos.x * PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL,
      pos.y * PhysicsContants.PHYSICS_WORLD_UNIT_INT_PIXEL, 0))
  }

  /**
    * <p>Met à jour la valeur [[Scrollable.scrollPositions]] en appliquant la vitesse [[Scrollable.scrollSpeed]].</p>
    * <ol>
    * <li>Ajout de la vitesse [[Scrollable.scrollPositions]]</li>
    * <li>Détermine si les positions doivent être replacée en appliquant le filtre [[Scrollable.whenTextureHaveToRespawn]].</li>
    * <li>Applique le changement de position si nécessaire [[Scrollable.deltaPosAppliedToTexturesHaveToRespawn]].</li>
    * <li>Ajuste les position pour le clipping.</li>
    * </ol>
    *
    * @param delta Le delta de temps entre chaque frames
    */
  def updateScroll(delta: Float): Unit = {

    scrollPositions.foreach(pos => pos.set(pos.add(scrollSpeed.mul(delta))))

    val allTextureThatHaveToMove =
      scrollPositions.filter(whenTextureHaveToRespawn)

    allTextureThatHaveToMove
      .foreach(_.set(deltaPosAppliedToTexturesHaveToRespawn.apply()))

    if (antiClippingActivated) {
      adjustForClipping()
    }
  }

  private def adjustForClipping(): Unit = {

    //Tri des listes pour préparation au calcul du delta
    val orderedList =
      scrollPositions.sortWith(positionSorterForAntiClipping)

    //Calcul du delta en commencant par le deuxième élément (pas de delta pour le premier)
    (for (i <- 1 until orderedList.length;
          //delta = (différence entre les coordonnées de deux élements) - la largeur/longueur de la texture
          xDelta = Math.abs(orderedList(i - 1).x - orderedList(i).x) - physicsWidthSize;
          yDelta = Math.abs(orderedList(i - 1).y - orderedList(i).y) - physicsHeightSize;
          posAndAssociatedDelta = (orderedList(i), (xDelta, yDelta)))
      yield posAndAssociatedDelta)
      .foreach(deltaForClippingApplyer)
  }

}

object Scrollable {
  /**
    * <p>Permet de récupérer le nombre de texture à traiter par le scroll.</p>
    */
  type TextureNumberProvider = () => Int

  /**
    * <p>Permet de récupérer la première position initiale dans le sens du scroll.</p>
    */
  type FirstTextureInFromPositionProvider = () => Vec2

  /**
    * <p>Critère permettant de déterminer si une position doit être mise en fin de scroll.</p>
    */
  type WhenTextureHaveToRespawn = (Vec2) => Boolean

  /**
    * <p>Permet de calculer de delta de position qui sera appliqué lors du renouveau d'une position de scroll.</p>
    */
  type DeltaPosAppliedToTexturesHaveToRespawn = () => Vec2

  /**
    * <p>Une simple représentation d'une position avec les composante x et y d'un delta à appliquer.</p>
    */
  type PositionAndDelta = (Vec2, (Float, Float))

  /**
    * <p>Permet d'appliquer le delta de position pour l'anti-clipping.</p>
    */
  type DeltaForClippingApplyer = (PositionAndDelta) => Unit

  /**
    * <p>Permet de trier par ordre croissant les positions [[Scrollable.scrollPositions]] afin d'être traitée par la calcul des deltas.</p>
    */
  type PositionSorterForAntiClipping = (Vec2, Vec2) => Boolean

}
