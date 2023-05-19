package fxcalculator.utils

import com.gluonhq.attach.storage.StorageService
import fxcalculator.utils.Logger.*
import upickle.default.*

import java.io.File
import java.nio.file.{Files, Path, Paths, StandardOpenOption}
import scala.jdk.CollectionConverters.{CollectionHasAsScala, IterableHasAsJava}
import scala.jdk.OptionConverters.*
import scala.util.{Failure, Success, Try}

object Storage:
  private lazy val storageFilePath: Option[Path] =
    Try {
      StorageService
        .create().toScala
        .flatMap(_.getPrivateStorage.toScala)
        .map { dir =>
          if !dir.exists then
            dir.mkdirs()
          if !dir.canRead then dir.setReadable(true)
          if !dir.canWrite then dir.setWritable(true)
          val filePath = dir.toPath.resolve("functions.json")
          val file = filePath.toFile
          if !file.exists then
            file.createNewFile()
          if !file.canRead then file.setReadable(true)
          if !file.canWrite then file.setWritable(true)
          info(s"file path: $filePath, exists: ${Files.exists(filePath)}, size: ${Files.size(filePath)}")
          filePath
        }
    } match
      case Success(path) =>
        path
      case Failure(ex) =>
        error(ex)
        None

  private def withFilePath[T](f: Path => T): Either[String, T] = storageFilePath match
    case Some(filePath) =>
      Try { f(filePath) } match
        case Success(res) =>
          Right(res)
        case Failure(ex)  =>
          error(ex)
          Left(ex.getMessage)
    case None =>
      error(s"Unable to resolve the data file path for $storageFilePath")
      Left(s"Unable to resolve the data file path for $storageFilePath")

  def dump(lines: Seq[String]): Either[String, Unit] = withFilePath { path =>
    val jsonStr = write(lines)
    import StandardOpenOption.*
    Files.writeString(path, jsonStr, CREATE, WRITE, TRUNCATE_EXISTING)
  }

  def readIn(): Either[String, Seq[String]] = withFilePath { path =>
    if Files.exists(path) && Files.size(path) > 0 then
      read[Seq[String]](path.toFile)
    else
      Seq.empty
  }

  def reset(): Unit = withFilePath { Files.deleteIfExists }
