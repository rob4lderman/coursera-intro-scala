// -rx- name <<= submitProjectName(pname => "progfun-"+ pname)
name := "patmat"

version := "1.0.0"

scalaVersion := "2.10.2"

scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"

libraryDependencies += "junit" % "junit" % "4.10" % "test"

// -rx- // This setting defines the project to which a solution is submitted. When creating a
// -rx- // handout, the 'createHandout' task will make sure that its value is correct.
// -rx- submitProjectName := "patmat"
// -rx- 
// -rx- // See documentation in ProgFunBuild.scala
// -rx- projectDetailsMap := {
// -rx- val currentCourseId = "progfun-003"
// -rx- Map(
// -rx-   "example" ->  ProjectDetails(
// -rx-                   packageName = "example",
// -rx-                   assignmentPartId = "fTzFogNl",
// -rx-                   maxScore = 10d,
// -rx-                   styleScoreRatio = 0.2,
// -rx-                   courseId=currentCourseId),
// -rx-   "recfun" ->     ProjectDetails(
// -rx-                   packageName = "recfun",
// -rx-                   assignmentPartId = "3Rarn9Ki",
// -rx-                   maxScore = 10d,
// -rx-                   styleScoreRatio = 0.2,
// -rx-                   courseId=currentCourseId),
// -rx-   "funsets" ->    ProjectDetails(
// -rx-                   packageName = "funsets",
// -rx-                   assignmentPartId = "fBXOL6Rd",
// -rx-                   maxScore = 10d,
// -rx-                   styleScoreRatio = 0.2,
// -rx-                   courseId=currentCourseId),
// -rx-   "objsets" ->    ProjectDetails(
// -rx-                   packageName = "objsets",
// -rx-                   assignmentPartId = "05dMMEz7",
// -rx-                   maxScore = 10d,
// -rx-                   styleScoreRatio = 0.2,
// -rx-                   courseId=currentCourseId),
// -rx-   "patmat" ->     ProjectDetails(
// -rx-                   packageName = "patmat",
// -rx-                   assignmentPartId = "4gPmpcif",
// -rx-                   maxScore = 10d,
// -rx-                   styleScoreRatio = 0.2,
// -rx-                   courseId=currentCourseId),
// -rx-   "forcomp" ->    ProjectDetails(
// -rx-                   packageName = "forcomp",
// -rx-                   assignmentPartId = "fG2oZGIO",
// -rx-                   maxScore = 10d,
// -rx-                   styleScoreRatio = 0.2,
// -rx-                   courseId=currentCourseId),
// -rx-   "streams" ->    ProjectDetails(
// -rx-                   packageName = "streams",
// -rx-                   assignmentPartId = "DWKgCFCi",
// -rx-                   maxScore = 10d,
// -rx-                   styleScoreRatio = 0.2,
// -rx-                   courseId=currentCourseId)//,
// -rx-   // "simulations" -> ProjectDetails(
// -rx-   //                  packageName = "simulations",
// -rx-   //                  assignmentPartId = "iYs4GARk",
// -rx-   //                  maxScore = 10d,
// -rx-   //                  styleScoreRatio = 0.2,
// -rx-   //                  courseId="progfun2-001"),
// -rx-   // "interpreter" -> ProjectDetails(
// -rx-   //                  packageName = "interpreter",
// -rx-   //                  assignmentPartId = "1SZhe1Ut",
// -rx-   //                  maxScore = 10d,
// -rx-   //                  styleScoreRatio = 0.2,
// -rx-   //                  courseId="progfun2-001")
// -rx- )
// -rx- }
// -rx- 
// -rx- // Files that we hand out to the students
// -rx- handoutFiles <<= (baseDirectory, projectDetailsMap, commonSourcePackages) map { (basedir, detailsMap, commonSrcs) =>
// -rx-   (projectName: String) => {
// -rx-     val details = detailsMap.getOrElse(projectName, sys.error("Unknown project name: "+ projectName))
// -rx-     val commonFiles = (PathFinder.empty /: commonSrcs)((files, pkg) =>
// -rx-       files +++ (basedir / "src" / "main" / "scala" / pkg ** "*.scala")
// -rx-     )
// -rx-     (basedir / "src" / "main" / "scala" / details.packageName ** "*.scala") +++
// -rx-     commonFiles +++
// -rx-     (basedir / "src" / "main" / "resources" / details.packageName ** "*") +++
// -rx-     (basedir / "src" / "test" / "scala" / details.packageName ** "*.scala") +++
// -rx-     (basedir / "build.sbt") +++
// -rx-     (basedir / "project" / "build.properties") +++
// -rx-     (basedir / "project" ** ("*.scala" || "*.sbt")) +++
// -rx-     (basedir / "project" / "scalastyle_config.xml") +++
// -rx-     (basedir / "lib_managed" ** "*.jar") +++
// -rx-     (basedir * (".classpath" || ".project")) +++
// -rx-     (basedir / ".settings" / "org.scala-ide.sdt.core.prefs")
// -rx-   }
// -rx- }
// -rx- 
// -rx- // This setting allows to restrict the source files that are compiled and tested
// -rx- // to one specific project. It should be either the empty string, in which case all
// -rx- // projects are included, or one of the project names from the projectDetailsMap.
// -rx- currentProject := ""
// -rx- 
// -rx- // Packages in src/main/scala that are used in every project. Included in every
// -rx- // handout, submission.
// -rx- commonSourcePackages += "common"
// -rx- 
// -rx- // Packages in src/test/scala that are used for grading projects. Always included
// -rx- // compiling tests, grading a project.
// -rx- gradingTestPackages += "grading"
