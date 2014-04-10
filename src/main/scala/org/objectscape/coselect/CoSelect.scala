package org.objectscape.coselect

import org.fusesource.hawtdispatch.{Dispatch, DispatchQueue}
import java.util.concurrent.{ConcurrentMap, ConcurrentLinkedQueue, BlockingQueue, ConcurrentHashMap}
import java.util.{Queue}

import org.objectscape.coselect.QueueUtils.QueueExtensions
import org.objectscape.coselect.MapUtils.ConcurrentMapExtensions

import scala.collection.JavaConversions._
import java.lang.Object
import java.util

/**
 * Created with IntelliJ IDEA.
 * User: plohmann
 * Date: 21.01.14
 * Time: 11:40
 * To change this template use File | Settings | File Templates.
 */
class CoSelect {

  private val queue: DispatchQueue = Dispatch.getGlobalQueue
  private val itemQueueMap : ConcurrentHashMap[BlockingQueue[_], Queue[Callback[Any]]] = new ConcurrentHashMap()

  protected[coselect] def addChannelCallback[ChannelType, ItemType](channel: Channel[ChannelType], callback: Callback[ItemType])
  {
    require(channel.dispatchQueue.equals(queue), "select/channel mismatch")
    require(channel.select.equals(this), "select/channel mismatch")

    itemQueueMap.getIfAbsentPut(channel.itemQueue, new ConcurrentLinkedQueue[Callback[Any]]).add(callback.asInstanceOf[Callback[Any]])
  }

  def newChannel[ChannelType]: Channel[ChannelType] = new Channel[ChannelType](this, queue)

  def execute[ItemType](itemQueue: BlockingQueue[ItemType])
  {
    synchronized
    {
      // TODO - write comment why synchronized block here canot be avoided
      val element: Any = itemQueue.poll()
      if (element == null) {
        return
      }

      val callbacks = itemQueueMap.get(itemQueue)
      if (callbacks == null) {
        return
      }

      val currentCallbacks = callbacks.pollAll()
      if (currentCallbacks.isEmpty) {
        return
      }

      callbacks.addAll(currentCallbacks)

      for (callback <- currentCallbacks) {
        callback.accept(element)
      }
    }
  }

  def removeChannelCallback[ItemType](channel: Channel[ItemType], callback: ItemType => Unit): Boolean = {
    val callbacks = Some(itemQueueMap.get(channel.itemQueue))
    if (callbacks.isDefined) {
      for (currentCallback <- callbacks.get) {
        if (currentCallback.contains(callback)) {
          return callbacks.get.remove(currentCallback)
        }
      }
    }
    return false
  }

  def removeChannelCallback[ItemType](channel: Channel[ItemType], callback: Long): Boolean = {
    val callbacks = Some(itemQueueMap.get(channel.itemQueue))
    if (callbacks.isDefined) {
      for (currentCallback <- callbacks.get) {
        if (currentCallback.isCallbackId(callback)) {
          return callbacks.get.remove(currentCallback)
        }
      }
    }
    return false
  }

}
