name := "Astronaut"

version := "1.0"

scalaVersion := "2.10.5"

libraryDependencies += "org.jdom" % "jdom" % "2.0.2"

libraryDependencies += "mysql" % "mysql-connector-java" % "6.0.2"

libraryDependencies += "com.typesafe" % "config" % "1.3.0"

libraryDependencies += "org.apache.spark" % "spark-core_2.10" % "1.6.1" % "provided"

assemblyJarName in assembly := "astronaut.jar"

mainClass in assembly := Some("edu.virginia.cs.Main")