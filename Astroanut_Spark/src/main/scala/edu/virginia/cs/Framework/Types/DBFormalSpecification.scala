package edu.virginia.cs.Framework.Types

/**
 * Created by tang on 8/8/14.
 */
import scala.io.Source
import java.util.ArrayList
import java.util.HashMap
import edu.virginia.cs.Synthesizer.Sig
import edu.virginia.cs.Synthesizer.AlloyOMToAlloyDM

class DBFormalSpecification (specPath: String) { //extends FrameworkTypeWrapper {
  //override type WrapperType = String
//  private var specPath: String = ""

  private var ids: ArrayList[String] = new ArrayList[String]();
  private var associations: ArrayList[String] = null;
  private var typeMap: HashMap[String, String] = null;
  private var sigs: ArrayList[Sig] = null;

  // store benchmark path to innerValue
  //makeWrapper(path)

  // get file content
  def getSpecContent(): String = {
    val fileContent = Source.fromFile(specPath).getLines().mkString
    fileContent
  }

//  def setSpec(path: String) = {
//    specPath = path
//  }

  def getSpec() = specPath
  
  def parseSpec() = {
    // Chong: in order to fill in these members, and call legacy code, 
    // I have to create AlloyDM here, and in this procedure, fill those information
    
//    var fSpecPath = specPath.asInstanceOf[DBFormalSpecification].getSpec
    var objSpecPath = specPath.substring(0, specPath.length() - 4) + "_dm.als"
    var intScope = 6

    var aotad: AlloyOMToAlloyDM = new AlloyOMToAlloyDM()
    // by calling run, (legacy) Object Specification will be created
    aotad.run(specPath, objSpecPath, intScope)
    this.ids  = aotad.getIDs()
    this.associations = aotad.getAss()
    this.typeMap = aotad.getTypeList()
    this.sigs = aotad.getSigs()
  }
  
  def getIds() : ArrayList[String] = {
    this.ids 
  }
  
  def getAssociations() : ArrayList[String] = {
    this.associations 
  }
  
  def getTypeMap() : HashMap[String, String] = {
    this.typeMap 
  }
  
  def getSigs() : ArrayList[Sig] = {
    this.sigs 
  }
}
