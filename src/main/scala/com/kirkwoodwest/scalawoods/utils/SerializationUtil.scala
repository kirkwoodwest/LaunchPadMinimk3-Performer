package com.kirkwoodwest.scalawoods.utils

import java.io._
import java.util
import java.util.Base64


import java.io._
import java.util.Base64
import scala.util.{Try, Using}
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

object SerializationUtil {

  def serialize(o: Serializable): String = {
    Using(new ByteArrayOutputStream()) { baos =>
      Using(new ObjectOutputStream(baos)) { oos =>
        oos.writeObject(o)
        oos.flush()
        Base64.getEncoder.encodeToString(baos.toByteArray)
      }.getOrElse("")
    }.getOrElse("")
  }

  def deserialize(s: String): Option[AnyRef] = {
    val data = Base64.getDecoder.decode(s)
    Using(new ObjectInputStream(new ByteArrayInputStream(data))) { ois =>
      ois.readObject()
    }.toOption
  }

  def deserializeAsList[T](serialized: String, clazz: Class[T]): Option[List[T]] = {
    deserialize(serialized) match {
      case Some(deserializedObject: java.util.List[_]) =>
        Try {
          deserializedObject.asScala.toList.collect {
            case item if clazz.isInstance(item) => clazz.cast(item)
          }
        }.toOption
      case _ =>
        None
    }
  }
}
