import scala.collection.mutable.ListBuffer

ThisBuild / scalaVersion := "2.13.3"
val list = ListBuffer[ProjectReference]()

val subFolders = file(".").listFiles.filter(_.isDirectory).filter(isNotIgnored).foreach { f =>
  f.listFiles.filter(_.isDirectory).filter(isTaskDir).foreach { folder =>
    list += ProjectRef(folder, folder.getName.replace(" ", "-"))
  }
}

lazy val root = Project(id = "Scala-Tutorial", base = file(".")).aggregate(list: _*)

def isTaskDir(dir: File): Boolean = new File(dir, "src").exists()
def isNotIgnored(dir: File): Boolean = !Seq(".idea", ".coursecreator", "project", "target").contains(dir.getName)