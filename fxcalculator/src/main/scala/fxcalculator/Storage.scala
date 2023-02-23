package fxcalculator

import com.gluonhq.attach.storage.StorageService
import fxcalculator.logic.Dictionary
import fxcalculator.logic.expressions.{FunctionAssignment, Variable}

import java.io.File
import java.nio.file.{Files, Path, Paths, StandardOpenOption}
import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.jdk.CollectionConverters.IterableHasAsJava
import scala.jdk.OptionConverters.*
import scala.util.Try

object Storage:
  private lazy val dataDirectory: Option[Path] =
    StorageService.create().toScala.flatMap {
      _.getPrivateStorage.toScala.map(p => Paths.get(p.getPath))
    }

  private lazy val functionsFilePath: Option[Path] = dataDirectory.map(_.resolve("functions.txt"))

  private def withFilePath[T](defValue: T)(f: Path => T): T = functionsFilePath match
    case Some(filePath) => f(filePath)
    case None           => defValue

  private def withFilePath(f: Path => Any): Boolean = functionsFilePath match
    case Some(filePath) =>
      f(filePath)
      true
    case None =>
      false

  def read: Seq[String] = withFilePath[Seq[String]](Nil){
    case path if Files.exists(path) => Files.readAllLines(path).asScala.toSeq
    case _ => Nil
  }

  def write(functions: Seq[String]): Boolean = withFilePath {
    path => Files.write(path, functions.asJava, StandardOpenOption.CREATE)
  }

  def append(functionStr: String): Boolean = withFilePath {
    path => Files.write(path, Seq(functionStr).asJava, StandardOpenOption.APPEND)
  }

  def write(dictionary: Dictionary): Boolean = withFilePath { path =>
    val funcs = dictionary.list(classOf[FunctionAssignment])
    val vars = dictionary.list(classOf[Variable])
  }
