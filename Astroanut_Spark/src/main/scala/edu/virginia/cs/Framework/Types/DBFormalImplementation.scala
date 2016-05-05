package edu.virginia.cs.Framework.Types

import java.util.ArrayList
import java.util.HashMap
import edu.virginia.cs.Synthesizer.Sig

// This class must be Scala class to be pluged into the framework
class DBFormalImplementation { // extends FrameworkTypeWrapper {
  //  override type WrapperType = String
  private var formalImplementation: String = ""

    // Chong: these members are really need to be in DBImplementation
    // when create schemas, these members can be filled with values

  private var sigs: ArrayList[Sig] = null
  private var ids: ArrayList[String] = new ArrayList[String]();
  private var associationsForCreateSchemas: ArrayList[String] = null;
  private var typeMap: HashMap[String, String] = null;
  
  
  def getAssociationsForCreateSchemas() = {
    this.associationsForCreateSchemas 
  }

  def getTypeMap(): HashMap[String, String] = {
    this.typeMap 
  }
  def setIds(list: ArrayList[String]) = {
    this.ids = list
  }
  
  def setAssociationsForCreateSchemas(list: ArrayList[String]) = {
    this.associationsForCreateSchemas  = list
  }
  
  def setTypeMap(hm: HashMap[String, String]) = {
    this.typeMap = hm
  }

  def getSigs(): ArrayList[Sig] = {
    this.sigs
  }

  def setSigs(sigs: ArrayList[Sig]) = {
    this.sigs = sigs
  }
  
  
  def getIds() :ArrayList[String]  = {
    this.ids 
  }
  

  // store benchmark path to innerValue
  //  makeWrapper(solutionPath)
  def getImplementation() = formalImplementation

  def setImp(imp: String) = {
    this.formalImplementation = imp
  }

  /**
   * Need to set all needed information for test cases generation here
   */
  //    schemas.put(fImpFileName, parser.getDataSchemas());
  //    parents.put(fImpFileName, parser.getParents());
  //    reverseTAss.put(fImpFileName, parser.getReverseTAssociate());
  //    foreignKeys.put(fImpFileName, parser.getForeignKey());
  //    association.put(fImpFileName, parser.getAssociation());
  //    primaryKeys.put(fImpFileName, parser.getPrimaryKeys());
  //    fields.put(fImpFileName, parser.getFields());
  //    allFields.put(fImpFileName, parser.getallFields());
  //    fieldsTable.put(fImpFileName, parser.getFieldsTable());
  //    fieldType.put(fImpFileName, parser.getFieldType());
}
