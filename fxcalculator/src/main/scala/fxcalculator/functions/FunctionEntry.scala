package fxcalculator.functions

final case class FunctionEntry(declaration: String, definition: String, isCustom: Boolean):
  def textForm: String = if definition.nonEmpty then s"$declaration = $definition" else declaration

object FunctionEntry:
  import fxcalculator.logic.expressions.{Assignment, FunctionAssignment, NativeFunction}
  
  def apply(assignment: Assignment): FunctionEntry       = FunctionEntry(assignment.declaration, assignment.constant.number.toString, assignment.isCustom)
  def apply(function: FunctionAssignment): FunctionEntry = FunctionEntry(function.declaration, function.definition, true)
  def apply(native: NativeFunction): FunctionEntry       = FunctionEntry(native.declaration, "", false)