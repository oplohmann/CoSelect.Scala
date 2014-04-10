package org.objectscape.coselect

import java.util.concurrent.atomic.AtomicLong

/**
 * Created with IntelliJ IDEA.
 * User: plohmann
 * Date: 21.01.14
 * Time: 09:58
 * To change this template use File | Settings | File Templates.
 */

private object TwoArgCallback {
  private val RunningConsumerId = new AtomicLong(-1)
}

class TwoArgCallback[ArgType](private val block: (ArgType, Long) => Unit) extends Callback[ArgType] {

  private val consumerId = TwoArgCallback.RunningConsumerId.incrementAndGet()

  def accept(arg: ArgType): Unit = block(arg, consumerId)

  def isCallbackId(callback: Long): Boolean = consumerId.equals(callback)

  def contains(callback: AnyRef): Boolean = block.equals(callback)

}
