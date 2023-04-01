package fxcalculator.functions

final case class FunctionEntry(name: String, declaration: String, definition: String, isCustom: Boolean):
  def textForm: String = if definition.nonEmpty then s"$declaration = $definition" else declaration

object FunctionEntry:
  import fxcalculator.logic.expressions.{ConstantAssignment, FunctionAssignment, NativeFunction}
  
  def apply(assignment: ConstantAssignment): FunctionEntry = 
    FunctionEntry(assignment.name, assignment.declaration, assignment.constant.number.toString, assignment.isCustom)
  def apply(function: FunctionAssignment): FunctionEntry = 
    FunctionEntry(function.name, function.declaration, function.definition, true)
  def apply(native: NativeFunction): FunctionEntry =
    FunctionEntry(native.name, native.declaration, "", false)
