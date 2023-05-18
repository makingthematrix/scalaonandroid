package fxcalculator.logic

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

final class CustomAssignments (private val buffer: mutable.ArrayBuffer[(String, String)]):
  def add(line: String): Boolean =
    if !line.contains('=') then false
    else
      val arr = line.split('=')
      buffer.addOne((arr(0).trim, arr(1).trim))
      true

  def add(lines: Seq[String]): Unit = lines.foreach(add)
  
  def delete(name: String): Boolean =
    val decl = name.trim
    if !buffer.exists(_._1.startsWith(decl)) then false
    else
      buffer.remove(buffer.indexWhere(_._1.startsWith(decl)))
      true

  def get(name: String): Option[(String, String)] =
    val decl = name.trim
    buffer.collectFirst {
      case (declaration, definition) if declaration.startsWith(decl) => (declaration, definition)
    }

  def copy(): CustomAssignments = new CustomAssignments(buffer.clone())  

  def lines: Seq[String] = buffer.map { case (declaration, definition) => s"$declaration = $definition" }.toSeq

  def asMap: Map[String, String] = buffer.toMap

  def reset(): Unit = buffer.clear()
  
  def size: Int = buffer.size
  
  def isEmpty: Boolean = buffer.isEmpty
  
object CustomAssignments:
  def apply(): CustomAssignments = new CustomAssignments(new ArrayBuffer[(String, String)]())