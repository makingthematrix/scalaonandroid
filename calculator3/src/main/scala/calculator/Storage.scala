package calculator

import com.gluonhq.attach.storage.StorageService

import java.io.File
import java.nio.file.{Files, Path, Paths}
import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.jdk.OptionConverters.*
import scala.util.Try

object Storage:
  lazy val dataDirectory: Option[Path] =
    StorageService.create().toScala.flatMap {
      _.getPrivateStorage.toScala.map(p => Paths.get(p.getPath))
    }
  
  def read: Seq[String] =
    dataDirectory.flatMap { dir =>
      val filePath = dir.resolve("functions.txt")
      Try {
        if !Files.exists(filePath) then Files.createFile(filePath) else filePath
      }.toOption
    }
    .fold(Seq.empty[String]) { path =>
      Files.readAllLines(path).asScala.toSeq 
    }

