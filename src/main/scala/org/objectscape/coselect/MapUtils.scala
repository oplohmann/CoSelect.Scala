package org.objectscape.coselect

import java.util
import java.util.concurrent.ConcurrentMap

/**
 * Created with IntelliJ IDEA.
 * User: plohmann
 * Date: 28.01.14
 * Time: 17:02
 * To change this template use File | Settings | File Templates.
 */
object MapUtils {

  implicit class ConcurrentMapExtensions[KeyType, ValueType](val map: ConcurrentMap[KeyType, ValueType])
  {
    def getIfAbsentPut(key: KeyType, newValue: ValueType) : ValueType = {
      val value = Option(map.get(key))
      if(value.isDefined) {
        value.get
      }
      else {
        Option(map.putIfAbsent(key, newValue)).getOrElse(newValue)
      }
    }
  }

}
