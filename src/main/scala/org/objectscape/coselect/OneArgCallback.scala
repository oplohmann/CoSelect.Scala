package org.objectscape.coselect

/**
 * Created with IntelliJ IDEA.
 * User: plohmann
 * Date: 20.01.14
 * Time: 16:00
 * To change this template use File | Settings | File Templates.
 */

// TODO - make sure no null object can be passed in as the block

class OneArgCallback[ArgType](private val block: (ArgType) => Unit) extends Callback[ArgType] {

  def accept(arg: ArgType): Unit = block(arg)

  def contains(callback: AnyRef): Boolean = block.equals(callback)

  def isCallbackId(callback: Long): Boolean = false

}
