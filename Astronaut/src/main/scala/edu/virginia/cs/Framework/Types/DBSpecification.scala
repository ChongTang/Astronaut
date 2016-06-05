package edu.virginia.cs.Framework.Types

/**
 * Created by tang on 8/9/14.
 */

import scala.io.Source

class DBSpecification (specFile:String) {
  
  def getSpecFile:String = this.specFile
  
  def getFileContent = {
    val content = Source.fromFile(this.specFile).getLines().mkString
    content
  }

}
