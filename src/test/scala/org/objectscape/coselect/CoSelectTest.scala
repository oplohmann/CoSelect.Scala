package org.objectscape.coselect

import org.scalatest.junit.AssertionsForJUnit
import org.junit._
import Assert._
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created with IntelliJ IDEA.
 * User: plohmann
 * Date: 21.01.14
 * Time: 11:01
 * To change this template use File | Settings | File Templates.
 */
class CoSelectTest extends AbstractDispatchTest
{

  @Test
  def selectTwoChannels
  {
    val latch: CountDownLatch = new CountDownLatch(2)
    val select: CoSelect = new CoSelect
    val count: AtomicInteger = new AtomicInteger(0)
    val channelOne: Channel[String] = select.newChannel

    channelOne.onElementAdded((str: String) => {
      println(str)
      count.incrementAndGet()
      latch.countDown()
    })

    val channelTwo: Channel[Int] = select.newChannel
    channelTwo.onElementAdded((i: Int) => {
      println(i)
      count.incrementAndGet()
      latch.countDown()
    })

    channelOne.add("hello world!")
    channelTwo.add(123)
    latch.await
    assertEquals(count.get, 2)
  }

  @Test
  def selectSingleChannel
  {
    val latch: CountDownLatch = new CountDownLatch(1)
    val select: CoSelect = new CoSelect
    val count: AtomicInteger = new AtomicInteger(0)
    val channel: Channel[String] = select.newChannel

    channel.onElementAdded((str: String) => {
      println(str)
      count.incrementAndGet()
      latch.countDown()
    })

    channel.add("hello world!")
    latch.await
    assertEquals(count.get, 1)
  }

  @Test
  def removeCallbackFromWithinCallback
  {
    val latch: CountDownLatch = new CountDownLatch(1)
    val select: CoSelect = new CoSelect
    val count: AtomicInteger = new AtomicInteger(0)
    val channel: Channel[String] = select.newChannel

    channel.onElementAdded((str: String, callback: Long) => {
      count.incrementAndGet()
      val found = channel.removeCallback(callback)
      assertTrue(found)
      latch.countDown()
    })

    channel.add("hello world!")
    latch.await
    assertEquals(count.get, 1)
  }

  @Test
  def removeCallbackFromChannel
  {
    val latch: CountDownLatch = new CountDownLatch(1)
    val select: CoSelect = new CoSelect
    val count: AtomicInteger = new AtomicInteger(0)
    val channel: Channel[String] = select.newChannel

    val callback = channel.onElementAdded((str: String) => {
      println(str)
      count.incrementAndGet()
      latch.countDown()
    })

    channel.add("hello world!")
    latch.await
    assertEquals(count.get, 1)
    val callbackFound: Boolean = channel.removeCallback(callback)
    assertTrue(callbackFound)
    channel.add("another hello world!")
    Thread.sleep(1000)
    assertEquals(count.get, 1)
  }

  @Test
  def selectOneChannelTwoCallbacks 
  {
    val latch: CountDownLatch = new CountDownLatch(4)
    val select: CoSelect = new CoSelect
    val count: AtomicInteger = new AtomicInteger(0)
    val channel: Channel[String] = select.newChannel
    
    channel.onElementAdded((str: String) => {
      println(str + "!")
      count.incrementAndGet()
      latch.countDown()
    })

    channel.onElementAdded((str: String) => {
      println(str + " world!")
      count.incrementAndGet()
      latch.countDown()
    })

    val success1 = channel.add("1 hello")
    assertTrue(success1)

    val success2 = channel.add("2 hello")
    assertTrue(success2)

    latch.await
    assertEquals(count.get, 4)
  }
}
