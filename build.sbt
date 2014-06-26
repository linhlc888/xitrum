organization := "tv.cntt"

name := "xitrum"

version := "2.16-SNAPSHOT"

scalaVersion := "2.11.1"
//scalaVersion := "2.10.4"

crossScalaVersions := Seq("2.11.1", "2.10.4")

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")

// http://www.scala-sbt.org/release/docs/Detailed-Topics/Java-Sources
// Avoid problem when this lib is built with Java 7 but the projects that use it
// are run with Java 6
// java.lang.UnsupportedClassVersionError: Unsupported major.minor version 51.0
javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

// Source code for Scala 2.10 and 2.11 are a little different ------------------
// See src/main/scala-2.10 and src/main/scala-2.11.

unmanagedSourceDirectories in Compile += baseDirectory.value / "src" / "main" / s"scala-${scalaBinaryVersion.value}"

//------------------------------------------------------------------------------

// Most Scala projects are published to Sonatype, but Sonatype is not default
// and it takes several hours to sync from Sonatype to Maven Central
resolvers += "SonatypeReleases" at "http://oss.sonatype.org/content/repositories/releases/"

// Projects using Xitrum must provide a concrete implentation of SLF4J (Logback etc.)
libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.7" % "provided"

// Netty is the core of Xitrum's HTTP(S) feature
libraryDependencies += "io.netty" % "netty" % "3.9.2.Final"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.3"

libraryDependencies += "com.typesafe.akka" %% "akka-cluster" % "2.3.3"

libraryDependencies += "com.typesafe.akka" %% "akka-contrib" % "2.3.3"

// For clustering SockJS
libraryDependencies += "tv.cntt" %% "glokka" % "2.0"

// Redirect Akka log to SLF4J
libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % "2.3.3"

// For scanning routes
libraryDependencies += "tv.cntt" %% "sclasner" % "1.6"

// For (de)serializing
libraryDependencies += "tv.cntt" %% "chill-scala" % "1.1"

// For jsEscape
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.3.2"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.2.9"

// For i18n
libraryDependencies += "tv.cntt" %% "scaposer" % "1.3"

// For compiling CoffeeScript to JavaScript
libraryDependencies += "tv.cntt" % "rhinocoffeescript" % "1.7.1"

//------------------------------------------------------------------------------
// JSON4S uses scalap 2.10.0 (2.11.0), which in turn uses scala-compiler 2.10.0 (2.11.0),
// which in turn uses scala-reflect 2.11.0. We need to force "scalaVersion" above,
// because Scala annotations (used by routes and Swagger) compiled by a newer version
// can't be read by an older version.
//
// Also, we must release a new version of Xitrum every time a new version of
// Scala is released.

libraryDependencies <+= scalaVersion { sv => "org.scala-lang" % "scalap" % sv }

// For test --------------------------------------------------------------------

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.0" % "test"

// An implementation of SLF4J is needed for log in tests to be output
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.2" % "test"

// Put config directory in classpath for easier development (sbt console etc.)
unmanagedBase in Runtime <<= baseDirectory { base => base / "src/test/resources" }

//------------------------------------------------------------------------------

// Avoid messy Scaladoc by excluding things that are not intended to be used
// directly by normal Xitrum users.
scalacOptions in (Compile, doc) ++= Seq("-skip-packages", "xitrum.sockjs")

// Skip API doc generation to speedup "publish-local" while developing.
// Comment out this line when publishing to Sonatype.
publishArtifact in (Compile, packageDoc) := false
