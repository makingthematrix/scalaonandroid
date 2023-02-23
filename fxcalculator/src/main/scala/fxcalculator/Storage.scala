package fxcalculator

import com.gluonhq.attach.storage.StorageService
import fxcalculator.logic.{Dictionary, Parser}
import fxcalculator.logic.expressions.{Assignment, FunctionAssignment}

import java.io.File
import java.nio.file.{Files, Path, Paths, StandardOpenOption}
import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.jdk.CollectionConverters.IterableHasAsJava
import scala.jdk.OptionConverters.*
import scala.util.{Failure, Success, Try}

object Storage:
  private lazy val dataDirectory: Option[Path] =
    StorageService.create().toScala.flatMap {
      _.getPrivateStorage.toScala.map(p => Paths.get(p.getPath))
    }

  private lazy val functionsFilePath: Option[Path] = dataDirectory.map(_.resolve("functions.txt"))

  private def withFilePath[T](f: Path => T): Either[String, T] = functionsFilePath match
    case Some(filePath) =>
      Try(f(filePath)) match
        case Success(res) => Right(res)
        case Failure(exception) => Left(exception.getMessage)
    case None =>
      Left("Unable to resolve the data file path")

  def append(functionStr: String): Either[String, Unit] = withFilePath { path =>
    Files.write(path, Seq(functionStr).asJava, StandardOpenOption.APPEND)
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
