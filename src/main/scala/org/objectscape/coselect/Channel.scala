package org.objectscape.coselect

import _root_.org.fusesource.hawtdispatch._
import java.util.concurrent.{BlockingQueue, TimeUnit, LinkedBlockingQueue}

/**
 * Created with IntelliJ IDEA.
 * User: plohmann
 * Date: 21.01.14
 * Time: 11:39
 * To change this template use File | Settings | File Templates.
 */
class Channel[ItemType](protected[coselect] val select: CoSelect, protected[coselect] val dispatchQueue: DispatchQueue) {

  require(select != null)
  require(dispatchQueue != null)

  @transient
  protected[coselect] var itemQueue = new LinkedBlockingQueue[ItemType]

  def add(item: ItemType): Boolean = {
    val result = itemQueue.add(item)
    enqueue
    return result
  }

  def offer(item: ItemType): Boolean = {
    val result: Boolean = itemQueue.offer(item)
    enqueue
    return result
  }

  def put(item: ItemType) {
    itemQueue.put(item)
    enqueue
  }

  def offer(item: ItemType, timeout: Long, unit: TimeUnit): Boolean = {
    val result: Boolean = itemQueue.offer(item)
    enqueue
    return result
  }

  private def enqueue {
    dispatchQueue{
      select.execute(itemQueue)
    }
  }

  def onElementAdded(consumer: ItemType => Unit): ItemType => Unit = {
    select.addChannelCallback(this, new OneArgCallback[ItemType](consumer))
    consumer
  }

  def removeCallback(consumer: ItemType => Unit): Boolean = {
    select.removeChannelCallback(this, consumer)
  }

  def removeCallback(callback: Long): Boolean = {
    select.removeChannelCallback(this, callback)
  }

  def onElementAdded(consumer: (ItemType, Long) => Unit): (ItemType, Long) => Unit = {
    select.addChannelCallback(this, new TwoArgCallback[ItemType](consumer))
    consumer
  }

}
