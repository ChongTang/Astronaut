package edu.virginia.cs

import java.util

/**
 * @author tang
 */


// This is the Main class, the entry point of the DB instance of Trademaker Framework
object Main {
  var isDebugOn = AppConfig.getDebug

  def main(args: Array[String]) {
    var spec = args(0)
    var specList = new util.ArrayList[String]()
    specList.add(spec)
    AppConfig.setSpecList(specList)
    var myDBTrademaker = new DBTrademaker()
    myDBTrademaker.run()

    if (isDebugOn) {
      println("Done")
    }
  }
}