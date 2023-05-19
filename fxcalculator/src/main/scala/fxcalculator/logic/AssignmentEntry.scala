package fxcalculator.logic

final case class AssignmentEntry(name: String, declaration: String, definition: String, isCustom: Boolean):
  def textForm: String = if definition.nonEmpty then s"$declaration = $definition" else declaration

object AssignmentEntry:
  import fxcalculator.logic.expressions.{ConstantAssignment, FunctionAssignment, NativeFunction}
  
  def apply(assignment: ConstantAssignment): AssignmentEntry =
    AssignmentEntry(assignment.name, assignment.declaration, assignment.constant.number.toString, assignment.isCustom)
  def apply(function: FunctionAssignment): AssignmentEntry =
    AssignmentEntry(function.name, function.declaration, function.definition, true)
  def apply(function: FunctionAssignment, declaration: String, definition: String): AssignmentEntry =
    AssignmentEntry(function.name, declaration, definition, true)
  def apply(native: NativeFunction): AssignmentEntry =
    AssignmentEntry(native.name, native.declaration, "", false)
