package fxcalculator.utils

import com.gluonhq.attach.storage.StorageService
import com.sun.javafx.logging.Logger
import fxcalculator.logic.expressions.{ConstantAssignment, FunctionAssignment}
import fxcalculator.logic.{AssignmentEntry, CustomAssignments, Dictionary, Parser}
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

  def dump(customAssignments: CustomAssignments): Either[String, Unit] = withFilePath { path =>
    info(s"path: $path")
    info(s"lines: ${customAssignments.lines}")
    val jsonStr = write(customAssignments.lines)
    import StandardOpenOption.*
    info(s"writing down: $jsonStr")
    Files.writeString(path, jsonStr, CREATE, WRITE, TRUNCATE_EXISTING)
  }

  def readIn(parser: Parser): Either[String, Unit] = withFilePath { path =>
    info(s"path: $path")
    if Files.exists(path) && Files.size(path) > 0 then
      val str = Files.readString(path)
      info(str)
      val lines: Seq[String] = read[Seq[String]](path.toFile)
      info(s"reading in: $lines")
      lines.foreach { parser.parse }
      parser.customAssignments.add(lines)
  }

  def reset(): Unit = withFilePath { Files.deleteIfExists }
