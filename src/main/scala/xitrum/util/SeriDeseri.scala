package xitrum.util

import scala.runtime.ScalaRunTime
import scala.util.{Try, Success, Failure}
import scala.util.control.NonFatal

import com.twitter.chill.{KryoInstantiator, KryoPool, KryoSerializer}
import xitrum.Log

object SeriDeseri extends Log {
  // Use this utility instead of using Kryo directly because Kryo is not threadsafe!
  // https://github.com/EsotericSoftware/kryo#threading
  private val kryoPool = {
    val r = KryoSerializer.registerAll
    val ki = (new KryoInstantiator).withRegistrar(r)
    KryoPool.withByteArrayOutputStream(Runtime.getRuntime.availableProcessors * 2, ki)
  }

  def serialize(any: Any): Array[Byte] = kryoPool.toBytesWithoutClass(any)

  def deserialize(bytes: Array[Byte]): Option[Any] = {
    try {
      val any = kryoPool.fromBytes(bytes)
      Option(any)
    } catch {
      case NonFatal(e) =>
        if (log.isDebugEnabled) log.debug("Could not deserialize: " + ScalaRunTime.stringOf(bytes), e)
        None
    }
  }
}
