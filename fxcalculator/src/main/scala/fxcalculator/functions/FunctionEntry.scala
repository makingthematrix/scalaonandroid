package fxcalculator.functions

sealed trait FunctionEntry:
  def declaration: String
  def definition: String
  def textForm: String = s"$declaration = $definition"

final case class NativeEntry(override val declaration: String) extends FunctionEntry:
  override val definition: String = "[Native]"

final case class CustomEntry(override val declaration: String, override val definition: String) extends FunctionEntry

object FunctionEntry:
  import fxcalculator.logic.expressions.{Assignment, FunctionAssignment, NativeFunction}
  
  def apply(assignment: Assignment): FunctionEntry       = CustomEntry(assignment.declaration, assignment.constant.number.toString)
  def apply(function: FunctionAssignment): FunctionEntry = CustomEntry(function.declaration, function.definition)
  def apply(native: NativeFunction): FunctionEntry       = NativeEntry(native.declaration)