package fxcalculator.logic.expressions

trait Assignment extends Expression:
  def name: String
  def declaration: String

object Assignment:
  def functionDeclaration(name: String, argNames: Seq[String]): String = s"$name(${argNames.mkString(", ")})"
