package fxcalculator.logic.expressions

import fxcalculator.logic.{Dictionary, Parser}

import scala.util.chaining.scalaUtilChainingOps

/**
 * ConstantAssignment
 *
 * There are two "pairs" of expression types in the project. One is `Assignment` and `Variable`, the other
 * "FunctionAssignment" and "Function". In both cases, the assignment types are used to parse and add to 
 * the dictionary a new expression under some name. Later, this expression can be retrieved as a variable or
 * a function and evaluated.
 * 
 * Variable assignments (but not function assignments) can be reassigned and they are evaluated eagerly - the right
 * side of the assignment is evaluated already during the parsing and replaced by a constant.
 * The two are connected: If I allowed for reassignments but only parsed the right side expression but not evaluate it,
 * it would be possible to make an infinite recursion. Consider this:
 * a = 1
 * b = a + 1
 * a = b + 1
 * With the current code, `b` is eagerly evaluated to `2`, and `a` is then reassigned to `3`. Without eager evaluation,
 * both variables would reference each other and their evaluation would never stop.
 *
 * @param name Name of the variable
 * @param constant The right side expression evaluated just after parsing and made into a constant
 */
final case class ConstantAssignment(override val name: String, constant: Constant, isCustom: Boolean = true) extends Assignment:
  override protected def evaluate(dict: Dictionary): Either[Error, Double] = constant.run(dict)
  override def declaration: String = name 
  override def textForm: String = s"$name = ${constant.number}"

object ConstantAssignment extends Parseable[ConstantAssignment]:
  override def parse(parser: Parser, line: String): ParsedExpr[ConstantAssignment] =
    if !line.contains("=") then
      ParsedExpr.empty
    else
      parseAssignment(parser, line)

  private def parseAssignment(parser: Parser, line: String): ParsedExpr[ConstantAssignment] =
    val assignIndex = line.indexOf('=')
    val name = line.substring(0, assignIndex)
    if !Dictionary.isValidName(name) then
      ParsedExpr.error(s"Invalid variable name: $name")
    else if !parser.dictionary.canAssign(name) then
      ParsedExpr.error(s"Unable to assign to: $name")
    else
      val expressionStr = line.substring(assignIndex + 1)
      parser
        .parse(expressionStr)
        .happyPath { expression =>
          Some {
            expression.run(parser.dictionary).map { number =>
              ConstantAssignment(name, Constant(number)).tap(assignment => parser.dictionary.add(assignment))
            }
          }
        }
        .errorIfEmpty(s"Unable to parse: $expressionStr")
