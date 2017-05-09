package edu.virginia.cs

import java.util
import java.io._

/**
 * @author tang
 */


// This is the Main class, the entry point of the DB instance of Trademaker Framework
object Main {
  var isDebugOn = AppConfig.getDebug

  def main(args: Array[String]) {
//  if specification is give by arguments,
//  we will use it instead of those set in configuration file
    // first get database
    val testdb:String = args(0)

    if(testdb.toLowerCase == "mysql" || testdb.toLowerCase == "postgres") {
      AppConfig.setTestDB(testdb.toLowerCase.trim)
      println("In Main: "+ AppConfig.getTestDB())
    } else {
      println("Main: Non supported RDBMS...")
      return
    }

    val specs: Array[String] = args.slice(1, args.length)
    if(specs.length > 0) {
      val specList = new util.ArrayList[String]()
      for(spec <- specs){
        //    get full path of the spec file
        val specPath = new File(spec).getAbsolutePath()
        specList.add(specPath)
      }
      AppConfig.setSpecList(specList)
    }

    val myDBTrademaker = new DBTrademaker()
    myDBTrademaker.run()

    if (isDebugOn) {
      println("Done")
    }
  }
}