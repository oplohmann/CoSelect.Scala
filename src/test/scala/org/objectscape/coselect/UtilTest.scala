package org.objectscape.coselect

import org.scalatest.junit.AssertionsForJUnit
import java.util.concurrent.{LinkedBlockingQueue, BlockingQueue}
import org.objectscape.coselect.QueueUtils.QueueExtensions
import org.junit.Test
import junit.framework.Assert

/**
 * Created with IntelliJ IDEA.
 * User: Oliver
 * Date: 04.02.14
 * Time: 08:19
 * To change this template use File | Settings | File Templates.
 */
class UtilTest extends AssertionsForJUnit {

  @Test
  def pollAll =
  {
    val queue = new LinkedBlockingQueue[Int]()
    queue.add(1)
    queue.add(323)

    val polledValues = queue.pollAll()
    Assert.assertTrue(queue.isEmpty)
    Assert.assertTrue(polledValues.size() == 2)
    Assert.assertTrue(polledValues.contains(1))
    Assert.assertTrue(polledValues.contains(323))
  }

}
