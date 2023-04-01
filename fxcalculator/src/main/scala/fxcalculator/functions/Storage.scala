package fxcalculator.functions

import com.gluonhq.attach.storage.StorageService
import com.sun.javafx.logging.Logger
import fxcalculator.Logger.*
import fxcalculator.logic.expressions.{ConstantAssignment, FunctionAssignment}
import fxcalculator.logic.{Dictionary, Parser}

import java.io.File
import java.nio.file.{Files, Path, Paths, StandardOpenOption}
import scala.jdk.CollectionConverters.{CollectionHasAsScala, IterableHasAsJava}
import scala.jdk.OptionConverters.*
import scala.util.{Failure, Success, Try}

import upickle.default.*

object Storage:
  given rw: ReadWriter[FunctionEntry] = macroRW

  private lazy val storageFilePath: Option[Path] =
    Try {
      StorageService.create().toScala.flatMap {
        _.getPrivateStorage.toScala.map(_.toPath.resolve("functions.json"))
      }
    } match
      case Success(path) =>
        path
      case Failure(ex) =>
        error(ex.getMessage)
        None

  private def withFilePath[T](f: Path => T): Either[String, T] = storageFilePath match
    case Some(filePath) =>
      Try {
        if !Files.exists(filePath) then Files.createFile(filePath)
        f(filePath)
      } match
        case Success(res) => Right(res)
        case Failure(ex)  => Left(ex.getMessage)
    case None =>
      error("Unable to resolve the data file path")
      Left("Unable to resolve the data file path")

  def dump(dictionary: Dictionary): Either[String, Unit] = withFilePath { path =>
    given rw: ReadWriter[FunctionEntry] = macroRW
    val list = dictionary.chronologicalList.collect {
      case c: ConstantAssignment if c.isCustom => FunctionEntry(c)
      case f: FunctionAssignment => FunctionEntry(f)
    }

    val jsonStr = write(list)
    Files.writeString(path, jsonStr, StandardOpenOption.CREATE)
  }

  def readIn(parser: Parser): Either[String, Unit] = withFilePath { path =>
    if Files.exists(path) then
      val entries: Seq[FunctionEntry] = read[Seq[FunctionEntry]](path.toFile)
      entries.foreach { entry => parser.parse(entry.textForm) }
  }
