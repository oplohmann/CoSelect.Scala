package org.objectscape.coselect

import org.fusesource.hawtdispatch.internal.DispatcherConfig
import org.junit.After

/**
 * Created with IntelliJ IDEA.
 * User: Oliver
 * Date: 31.01.14
 * Time: 08:53
 * To change this template use File | Settings | File Templates.
 */
class AbstractDispatchTest {

  @After
  def shutDown {
    DispatcherConfig.getDefaultDispatcher.shutdown
  }

}
