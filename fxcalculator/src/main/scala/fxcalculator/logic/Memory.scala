package fxcalculator.logic

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

import fxcalculator.utils.Storage

final class Memory(useStorage: Boolean, private val buffer: mutable.ArrayBuffer[(String, String)]):
  def add(line: String): Unit =
    if justAdd(line) then dump()

  def add(lines: Seq[String]): Unit =
    lines.foreach(justAdd)
    dump()

  private def justAdd(line: String): Boolean =
    if !line.contains('=') then false
    else
      val arr = line.split('=')
      buffer.addOne((arr(0).trim, arr(1).trim))
      true

  def delete(name: String): Boolean =
    val decl = name.trim
    if !buffer.exists(_._1.startsWith(decl)) then false
    else
      buffer.remove(buffer.indexWhere(_._1.startsWith(decl)))
      dump()
      true

  def get(name: String): Option[(String, String)] =
    val decl = name.trim
    buffer.collectFirst {
      case (declaration, definition) if declaration.startsWith(decl) => (declaration, definition)
    }

  def copy(): Memory = new Memory(useStorage, buffer.clone())

  def lines: Seq[String] = buffer.map { case (declaration, definition) => s"$declaration = $definition" }.toSeq

  def reset(): Unit =
    buffer.clear()
    if useStorage then Storage.reset()

  def size: Int = buffer.size

  def isEmpty: Boolean = buffer.isEmpty

  def dump(): Either[String, Unit] =
    if useStorage then Storage.dump(lines) else Right(())

  def readIn(): Either[String, Seq[String]] =
    if useStorage then
      buffer.clear()
      Storage.readIn().map { lns =>
        lns.foreach(justAdd)
        lns
      }
    else
      Right(lines)

object Memory:
  def apply(useStorage: Boolean = false): Memory = new Memory(useStorage, new ArrayBuffer[(String, String)]())