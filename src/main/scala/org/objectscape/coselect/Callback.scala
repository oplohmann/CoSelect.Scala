package org.objectscape.coselect

/**
 * Created with IntelliJ IDEA.
 * User: plohmann
 * Date: 20.01.14
 * Time: 14:06
 * To change this template use File | Settings | File Templates.
 */
trait Callback[ArgType] {

  def accept(arg: ArgType)

  def contains(callback: AnyRef): Boolean

  def isCallbackId(callback: Long): Boolean

}
