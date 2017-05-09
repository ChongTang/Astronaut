package edu.virginia.cs

import java.util._

import com.typesafe.config._
import collection.JavaConversions._

object AppConfig {
  private final var params: Config = ConfigFactory.load("application")

  private final var debug: Boolean = params.getBoolean("dev.debug")
  private final var mysqlUser: String = params.getString("mysql.user")
  private final var mysqlPassword: String = params.getString("mysql.password")
//  private final var specificationPath: String = params.getString("app.specificationPath");
//  private final var implsPath: String = params.getString("app.implsPath");
//  private final var testCasesPath: String = params.getString("app.testCasesPath");
  private final var intScopeForImpl: Integer = params.getInt("alloy.intScopeForImpl")
  private final var intScopeForTestCases: Integer = params.getInt("alloy.intScopeForTestCases")
  private final var maxSolForImpl: Integer = params.getInt("alloy.maxSolForImpl")
  private final var maxSolForTest: Integer = params.getInt("alloy.maxSolForTest")
  private final var A4ReportSymmetry: Integer = params.getInt("alloy.A4Report.symmetry")
  private final var A4ReportSkolemDepth: Integer = params.getInt("alloy.A4Report.skolemDepth")
  private final var isRandom: Integer = params.getInt("alloy.tlGenerator")
  private final var randomRange: Integer = params.getInt("alloy.randomRange")
  private final var subRange: Integer = params.getInt("alloy.subRange")
  private final var specList: List[String] = params.getStringList("app.specs").toList
  private final val solver = params.getString("app.solver")
  private final var storeAllSolutions: Boolean = params.getBoolean("app.storeAllSolution")
  private final val hdfsURL = params.getString("hadoop.server")
  private final val hdfsFile = params.getString("hadoop.file")
  private final val sparkMaster = params.getString("spark.master")
  private final val resultFile = params.getString("resultFile")
  private final var sparkSlaves: List[String] = params.getStringList("spark.slaves").toList
  private final var testDB: String = params.getString("app.testDB")
  private final val postgresUser: String = params.getString("postgres.user")
  private final val postgresPassword: String = params.getString("postgres.password")

  def getPostgresUser(): String = {
    this.postgresUser
  }

  def getPostgresPassword(): String = {
    this.postgresPassword
  }

  def getTestDB(): String = {
    this.testDB
  }

  def setTestDB(testDB: String) {
    this.testDB = testDB
  }

  def setSpecList(list:List[String]) {
    this.specList = list
  }

  def getResultFile(): String = {
    this.resultFile
  }

  def getHdfsURL(): String = {
    this.hdfsURL
  }

  def getHdfsFile(): String = {
    this.hdfsFile
  }

  def getSparkMaster(): String = {
    this.sparkMaster
  }

  def getSparkSlaves():List[String] = {
    this.sparkSlaves
  }
  
  def getStoreAllSolutions(): Boolean = {
    this.storeAllSolutions
  }
  
  def getSolver(): String = {
    this.solver
  }
  
  def getSpecs():List[String] = {
    this.specList 
  }

  def getDebug(): Boolean = {
    this.debug
  }

  def getMySQLUser(): String = {
    this.mysqlUser
  }

  def getMysqlPassword(): String = {
    this.mysqlPassword
  }

//  def getSpecificationPath(): String = {
//    this.specificationPath
//  }

//  def getImplsPath(): String = {
//    this.implsPath
//  }

//  def getTestCasesPath(): String = {
//    this.testCasesPath
//  }

  def getIntScopeForImpl(): Integer = {
    this.intScopeForImpl
  }

  def getIntScopeForTestCases(): Integer = {
    this.intScopeForTestCases
  }

  def getMaxSolForImpl(): Integer = {
    this.maxSolForImpl
  }

  def getMaxSolForTest(): Integer = {
    this.maxSolForTest
  }

  def getA4ReportSymmetry(): Integer = {
    this.A4ReportSymmetry
  }

  def getA4ReportSkolemDepth(): Integer = {
    this.A4ReportSkolemDepth
  }

  def getIsRandom(): Integer = {
    this.isRandom
  }

  def getRandomRange(): Integer = {
    this.randomRange 
  }
  
  def getSubRange(): Integer = {
    this.subRange 
  }
}
