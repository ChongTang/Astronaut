package edu.virginia.cs.Framework.Types

import scala.io.Source
import java.util.ArrayList
import java.util.HashMap
import edu.virginia.cs.Synthesizer.CodeNamePair
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.DocumentBuilder
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import org.w3c.dom.Node
import edu.virginia.cs.Synthesizer.Sig
import scala.collection.JavaConversions._
import edu.virginia.cs.Synthesizer.DataProvider
import java.io.Serializable

/**
 * Created by tang on 8/9/14.
 */

// ImpelementationType here will be the file path of SQL schema script

class DBImplementation(path:String) extends Serializable { //extends FrameworkTypeWrapper {

  private var implPath: String = path

  private var dataProvider: DataProvider = null
  private var reverseTAssociate: ArrayList[CodeNamePair] = null
  private var foreignKeys: ArrayList[CodeNamePair] = null
  // HashMap[Association Name, pair[src, dst], src and dst are class name
  private var associations: HashMap[String, CodeNamePair] = null
  private var primaryKeys: ArrayList[CodeNamePair] = null
  private var fields: ArrayList[CodeNamePair] = null
  private var allFields: ArrayList[String] = null
  private var fieldsTable: ArrayList[CodeNamePair] = null


  // this the reverse of "id" in implementation
  private var reverseIDs: ArrayList[CodeNamePair] = null

  private var sigs: ArrayList[Sig] = null
  private var ids: ArrayList[String] = null
  private var associationsForCreateSchemas: ArrayList[String] = null
  private var typeMap: HashMap[String, String] = null

  def setSigs(sigs: ArrayList[Sig]) = {
    this.sigs = sigs
  }

  def getIds(): ArrayList[String] = {
    this.ids
  }

  def setIds(ids: ArrayList[String]) = {
    this.ids = ids
  }

  def getAssociationsForCreateSchemas(): ArrayList[String] = {
    this.associationsForCreateSchemas
  }

  def setAssociationsForCreateSchemas(asss: ArrayList[String]) = {
    this.associationsForCreateSchemas = asss
  }

  def getTypeMap(): HashMap[String, String] = {
    this.typeMap
  }

  def setTypeMap(typeMap: HashMap[String, String]) = {
    this.typeMap = typeMap
  }

  // store benchmark path to innerValue
  //makeWrapper(implementationPath)

  // get file content, file path can be retrieved by calling getInnerValue()
  def getFileContent():String = {
    val content = Source.fromFile(implPath).getLines().mkString
    content
  }

  def getImPath():String = implPath

  def getDataProvider(): DataProvider = {
    this.dataProvider
  }

  def setDataProvider(dp: DataProvider) = {
    this.dataProvider = dp
  }

  def getReverseTAssociate(): ArrayList[CodeNamePair] = {
    this.reverseTAssociate 
  }

  def setReverseTAssociate(rTAss: ArrayList[CodeNamePair]) = {
    this.reverseTAssociate = rTAss
  }

  def getForeignKeys(): ArrayList[CodeNamePair] = {
    this.foreignKeys 
  }

  def setForeignKeys(fKeys: ArrayList[CodeNamePair]) = {
    this.foreignKeys = fKeys
  }

  def getAssociations(): HashMap[String, CodeNamePair] = {
    this.associations 
  }

  def setAssociations(ass: HashMap[String, CodeNamePair]) = {
    this.associations = ass
  }

  def getPrimaryKeys(): ArrayList[CodeNamePair] = {
    this.primaryKeys 
  }

  def setPrimaryKeys(pKeys: ArrayList[CodeNamePair]) = {
    this.primaryKeys = pKeys
  }

  def getFields(): ArrayList[CodeNamePair] = {
    this.fields 
  }

  def setFields(fields: ArrayList[CodeNamePair]) = {
    this.fields = fields
  }

  def getAllFields(): ArrayList[String] = {
    this.allFields 
  }

  def setAllFields(af: ArrayList[String]) = {
    this.allFields = af
  }

  def getFieldsTable(): ArrayList[CodeNamePair] = {
    this.fieldsTable 
  }

  def setFieldsTable(ft: ArrayList[CodeNamePair]) = {
    this.fieldsTable = ft
  }

  def getReverseIDs: ArrayList[CodeNamePair] = {
    this.reverseIDs
  }

  def setReverseIDs(ids:ArrayList[CodeNamePair]) = {
    this.reverseIDs = ids
  }

  def getSigs(): ArrayList[Sig] = {
    this.sigs
  }

  // parse the xml file and store all information back to data structures
  def parseImplXMLFile() = {
    var schemas: HashMap[String, ArrayList[CodeNamePair]] = new HashMap[String, ArrayList[CodeNamePair]]()
    var dbf: DocumentBuilderFactory = DocumentBuilderFactory.newInstance();
    try {
      //Using factory get an instance of document builder
      var db: DocumentBuilder = dbf.newDocumentBuilder();
      //parse using builder to get DOM representation of the XML file
      var dom: Document = db.parse(this.implPath);

      //get the root element
      var docEle: Element = dom.getDocumentElement();
      //get a nodelist of elements
      var fieldnodes: NodeList = docEle.getElementsByTagName("field");
      // handle parent first
      for (i <- 0 to fieldnodes.getLength()) {
        var node: Node = fieldnodes.item(i)
        if (node.hasAttributes()) {
          var element: Element = node.asInstanceOf[Element];
          var labelValue: String = element.getAttribute("label");
          if (labelValue.equalsIgnoreCase("parent")) {
            parseParent(element);
          }
        }
      }

      // handle other labels
      for (i <- 0 to fieldnodes.getLength()) {
        var node: Node = fieldnodes.item(i);
        // find different sub nodes based on label value
        // find node if the "label" attribute is "primaryKey"
        if (node.hasAttributes()) {
          var element: Element = node.asInstanceOf[Element];
          var labelValue: String = element.getAttribute("label");
          if (labelValue.equalsIgnoreCase("primarykey")) {
            parsePK(element);
          } else if (labelValue.equalsIgnoreCase("fields")) {
            parseFields(element);
          } else if (labelValue.equalsIgnoreCase("foreignKey")) {
            parseFK(element);
          } else if (labelValue.equalsIgnoreCase("tAssociate")) {
            parse_tAssociate(element);
          } else if (labelValue.equalsIgnoreCase("fAssociate")) {
            parse_fAssociate(element);
          } else if (labelValue.equalsIgnoreCase("attrSet")) {
            parseAttrSet(element);
          } else if (labelValue.equalsIgnoreCase("id")) {
            parseId(element);
          } else if (labelValue.equalsIgnoreCase("src")) {
            parseSrc(element);
          } else if (labelValue.equalsIgnoreCase("dst")) {
            parseDst(element);
          } else if (labelValue.equalsIgnoreCase("src_multiplicity")) {
            parseSrcMultiplicity(element);
          } else if (labelValue.equalsIgnoreCase("dst_multiplicity")) {
            parseDstMultiplicity(element);
          }
        }
      }

      // get sig nodes
      var signodes: NodeList = docEle.getElementsByTagName("sig");
      for (i <- 0 to signodes.getLength()) {
        var node: Node = signodes.item(i);
        // find different subnodes based on label value
        // find node if the "label" attribute is "primaryKey"
        if (node.hasAttributes()) {
          val element = node.asInstanceOf[Element]
          val labelValue = element.getAttribute("label")
          val tmp = labelValue.split("/")
          val `type` = tmp(tmp.length - 1)
          if (`type`.equalsIgnoreCase("Real") || `type`.equalsIgnoreCase("Integer") ||
            `type`.equalsIgnoreCase("string") ||
            `type`.equalsIgnoreCase("Class") ||
            `type`.equalsIgnoreCase("DType") ||
            `type`.equalsIgnoreCase("Bool") ||
            `type`.equalsIgnoreCase("Longblob") ||
            `type`.equalsIgnoreCase("Time")) {
            val atoms = element.getElementsByTagName("atom")
            for (j <- 0 until atoms.getLength) {
              val node1 = atoms.item(j)
              val tableCode = node1.asInstanceOf[Element]
              val code = tableCode.getAttribute("label")
              val atomLabel = code.split("/")
              val name = atomLabel(atomLabel.length - 1)
              this.dataProvider.addType(name, `type`)
            }
          }
        }
      }
    } catch {
      case pce: Exception => pce.printStackTrace()
    }
  }

  def parse_tAssociate(element: Element) {
    var children = element.getElementsByTagName("tuple")
    for (j <- 0 until children.getLength) {
      val child = children.item(j)
      if (child.hasChildNodes()) {
        val singleTuple = child.asInstanceOf[Element]
        val code = getSingleAtom(singleTuple, 0)
        var name = getSingleAtom(singleTuple, 1)
        val hasCode = this.dataProvider.hasPairCode(code)
        if (hasCode) {
          val root = getRootTable(name)
          name = root
          this.dataProvider.removePairByCode(code)
        }
        this.dataProvider.addPair(code, name)
      }
    }
    children = element.getElementsByTagName("tuple")
    for (j <- 0 until children.getLength) {
      val child = children.item(j)
      if (child.hasChildNodes()) {
        val singleTuple = child.asInstanceOf[Element]
        val code = getSingleAtom(singleTuple, 0)
        val name = getSingleAtom(singleTuple, 1)
        val tableName = this.dataProvider.getSecondByFirst(code)
        this.reverseTAssociate.add(new CodeNamePair(name, tableName))
      }
    }
  }

  def parsePK(element: Element) {
    val children = element.getElementsByTagName("tuple")
    for (j <- 0 until children.getLength) {
      val child = children.item(j)
      if (child.hasChildNodes()) {
        val singleTuple = child.asInstanceOf[Element]
        val table = getSingleAtom(singleTuple, 0)
        val key = getSingleAtom(singleTuple, 1)
        this.dataProvider.addItem(table, "primaryKey", key)
        this.primaryKeys.add(new CodeNamePair(table, key))
      }
    }
  }

  def parseFK(element: Element) {
    val children = element.getElementsByTagName("tuple")
    for (j <- 0 until children.getLength) {
      val child = children.item(j)
      if (child.hasChildNodes()) {
        val singleTuple = child.asInstanceOf[Element]
        val table = getSingleAtom(singleTuple, 0)
        val key = getSingleAtom(singleTuple, 1)
        this.dataProvider.addItem(table, "foreignKey", key)
        this.foreignKeys.add(new CodeNamePair(table, key))
      }
    }
  }

  def parseFields(element: Element) {
    val children = element.getElementsByTagName("tuple")
    for (j <- 0 until children.getLength) {
      val child = children.item(j)
      if (child.hasChildNodes()) {
        val singleTuple = child.asInstanceOf[Element]
        val table = getSingleAtom(singleTuple, 0)
        val field = getSingleAtom(singleTuple, 1)
        this.dataProvider.addItem(table, "fields", field)
        this.fields.add(new CodeNamePair(table, field))
        this.allFields.add(field)
        this.fieldsTable.add(new CodeNamePair(field, table))
      }
    }
  }

  def parse_fAssociate(element: Element) {
    val children = element.getElementsByTagName("tuple")
    for (j <- 0 until children.getLength) {
      val child = children.item(j)
      if (child.hasChildNodes()) {
        val singleTuple = child.asInstanceOf[Element]
        val code = getSingleAtom(singleTuple, 0)
        val name = getSingleAtom(singleTuple, 1)
        this.dataProvider.addPair(code, name)
      }
    }
  }

  def parseId(element: Element) {
    val children = element.getElementsByTagName("tuple")
    for (j <- 0 until children.getLength) {
      val child = children.item(j)
      if (child.hasChildNodes()) {
        val singleTuple = child.asInstanceOf[Element]
        val table = getSingleAtom(singleTuple, 0)
        val Id = getSingleAtom(singleTuple, 1)
        this.dataProvider.addItem(table, "Id", Id)
      }
    }
  }

  def parseParent(element: Element) {
    val children = element.getElementsByTagName("tuple")
    for (j <- 0 until children.getLength) {
      val child = children.item(j)
      if (child.hasChildNodes()) {
        val singleTuple = child.asInstanceOf[Element]
        val childTable = getSingleAtom(singleTuple, 0)
        val parentTable = getSingleAtom(singleTuple, 1)
        this.dataProvider.addParent(childTable, parentTable)
      }
    }
  }

  def parseSrc(element: Element) {
    val children = element.getElementsByTagName("tuple")
    for (j <- 0 until children.getLength) {
      val child = children.item(j)
      if (child.hasChildNodes()) {
        val singleTuple = child.asInstanceOf[Element]
        val table = getSingleAtom(singleTuple, 0)
        val srcTable = getSingleAtom(singleTuple, 1)
        this.dataProvider.addItem(table, "src", srcTable)
        if (this.associations.containsKey(table)) {
          setAssociation(table, "src", srcTable)
        } else {
          this.associations.put(table, new CodeNamePair(srcTable, ""))
        }
      }
    }
  }

  def parseDst(element: Element) {
    val children = element.getElementsByTagName("tuple")
    for (j <- 0 until children.getLength) {
      val child = children.item(j)
      if (child.hasChildNodes()) {
        val singleTuple = child.asInstanceOf[Element]
        val table = getSingleAtom(singleTuple, 0)
        val dstTable = getSingleAtom(singleTuple, 1)
        this.dataProvider.addItem(table, "dst", dstTable)
        if (this.associations.containsKey(table)) {
          setAssociation(table, "dst", dstTable)
        } else {
          this.associations.put(table, new CodeNamePair("", dstTable))
        }
      }
    }
  }

  def parseSrcMultiplicity(element: Element) {
    val children = element.getElementsByTagName("tuple")
    for (j <- 0 until children.getLength) {
      val child = children.item(j)
      if (child.hasChildNodes()) {
        val singleTuple = child.asInstanceOf[Element]
        val table = getSingleAtom(singleTuple, 0)
        val parentTable = getSingleAtom(singleTuple, 1)
        this.dataProvider.addItem(table, "srcMul", parentTable)
      }
    }
  }

  def parseDstMultiplicity(element: Element) {
    val children = element.getElementsByTagName("tuple")
    for (j <- 0 until children.getLength) {
      val child = children.item(j)
      if (child.hasChildNodes()) {
        val singleTuple = child.asInstanceOf[Element]
        val table = getSingleAtom(singleTuple, 0)
        val parentTable = getSingleAtom(singleTuple, 1)
        this.dataProvider.addItem(table, "dstMul", parentTable)
      }
    }
  }

  def parseAttrSet(element: Element) {
    val children = element.getElementsByTagName("tuple")
    for (j <- 0 until children.getLength) {
      val child = children.item(j)
      if (child.hasChildNodes()) {
        val singleTuple = child.asInstanceOf[Element]
        val table = getSingleAtom(singleTuple, 0)
        val attr = getSingleAtom(singleTuple, 1)
        this.dataProvider.addItem(table, "attr", attr)
      }
    }
  }

  def getSingleAtom(singleTuple: Element, i: Int): String = {
    val atoms = singleTuple.getElementsByTagName("atom")
    val tableCode = atoms.item(i).asInstanceOf[Element]
    var code = tableCode.getAttribute("label")
    val tmp = code.split("/")
    code = tmp(tmp.length - 1)
    code
  }

  // chong: maybe elem._2.setFirst() cannot set the value to associations map
  def setAssociation(assName: String, target: String, value: String) {
    // iterate java hashmap
    var it = this.associations.iterator
    while (it.hasNext) {
      var elem = it.next
      var key = elem._1
      if (key.equalsIgnoreCase(assName)) {
        if (target.equalsIgnoreCase("src")) {
          elem._2.setFirst(value)
        } else if (target.equalsIgnoreCase("dst")) {
          elem._2.setSecond(value)
        }
      }
    }
  }

  def getRootTable(tableName: String): String = {
    for (i <- 0 to this.sigs.size()) {
      var sig = this.sigs.get(i)
      if (sig.getSigName().equalsIgnoreCase(tableName)) {
        if (!sig.isHasParent()) {
          return sig.getSigName()
        } else {
          return getRootTable(sig.getParent())
        }
      }
    }
    null
  }
  
  def getDataSchemas(): HashMap[String, ArrayList[CodeNamePair]] = {
    this.dataProvider.getTables()
  }
}
