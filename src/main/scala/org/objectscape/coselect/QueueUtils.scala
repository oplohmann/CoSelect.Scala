package org.objectscape.coselect

import java.util

/**
 * Created with IntelliJ IDEA.
 * User: plohmann
 * Date: 28.01.14
 * Time: 17:16
 * To change this template use File | Settings | File Templates.
 */
object QueueUtils
{
  implicit class QueueExtensions[T](val queue: util.Queue[T])
  {
    def pollAll() : util.List[T] = {
      println("q: " + queue.size())
      var next = queue.poll()
      val list = new util.ArrayList[T]()
      while (next != null) {
        list.add(next)
        next = queue.poll()
      }
      println("list: " + list.size())
      list
    }
  }
}
