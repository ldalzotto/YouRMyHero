package flappy.util

import java.util.UUID

import scala.collection.mutable

trait Cached[T] {

  private val cache: mutable.Map[UUID, T] = mutable.Map.empty

  private def generateUUID(stringId: String): UUID
  = UUID.nameUUIDFromBytes(stringId.getBytes)

  def getOrDefine(stringId: String, createInstance: () => T): T = {
    cache.getOrElse(generateUUID(stringId), {
      {
        Option(createInstance.apply())
          .map((generateUUID(stringId), _))
          .foreach(v => cache(v._1) = v._2)
        cache.get(generateUUID(stringId)).get
      }
    })
  }

  def getAll: Seq[T] = cache.values.toList

}
