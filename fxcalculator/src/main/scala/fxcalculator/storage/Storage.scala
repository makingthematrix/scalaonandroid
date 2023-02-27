package fxcalculator.storage

import com.gluonhq.attach.storage.StorageService
import com.sun.javafx.logging.Logger
import fxcalculator.Logger.*
import fxcalculator.logic.expressions.{Assignment, FunctionAssignment}
import fxcalculator.logic.{Dictionary, Parser}

import java.io.File
import java.nio.file.{Files, Path, Paths, StandardOpenOption}
import scala.jdk.CollectionConverters.{CollectionHasAsScala, IterableHasAsJava}
import scala.jdk.OptionConverters.*
import scala.util.{Failure, Success, Try}

object Storage:
  private lazy val storageFilePath: Option[Path] =
    Try {
      StorageService.create().toScala.flatMap {
        _.getPrivateStorage.toScala.map(_.toPath.resolve("storage.txt"))
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

  def append(textForm: String): Either[String, Unit] = withFilePath { path =>
    Files.write(path, Seq(textForm).asJava, StandardOpenOption.APPEND)
  }

  def dump(parser: Parser): Either[String, Unit] = withFilePath { path =>
    val dictionary = parser.dictionary
    val funcs = dictionary.list(classOf[FunctionAssignment])
    val vars = dictionary.list(classOf[Assignment])
    val textForms = funcs.map(_.textForm) ++ vars.map(_.textForm)
    Files.write(path, textForms.asJava, StandardOpenOption.CREATE)
  }

  def readIn(parser: Parser): Either[String, Unit] = withFilePath { path =>
    Files.readAllLines(path).asScala.foreach(parser.parse)
  }
