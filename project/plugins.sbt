// Run sbt eclipse to create Eclipse project file
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.2.4")

// Run "sbt mimaReportBinaryIssues" to check for binary compatibility
// http://www.typesafe.com/community/core-tools/migration-manager
addSbtPlugin("com.typesafe" % "sbt-mima-plugin" % "0.8.1")
