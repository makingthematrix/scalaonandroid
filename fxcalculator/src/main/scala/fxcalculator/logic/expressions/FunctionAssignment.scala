package fxcalculator.logic.expressions

import fxcalculator.logic.{Dictionary, ParsedFunction, Parser, Preprocessor}
import fxcalculator.logic.ParsedFunction.LineSide
import fxcalculator.logic.Dictionary.isValidName

import scala.util.chaining.*

/**
 * Function assignment
 *
 * Contrary to a variable assignment, a function assignment cannot be reassigned, and it is evaluated on demand.
 * A valid function assignment has the form `name(arg1, arg2, ...) = expression`. If the name is not followed by
 * parentheses, I assume it's not a function (but maybe it's a variable). If it is a valid function assignment,
 * the expression on the right of `=` is parsed (but not evaluated), wrapped in the `FunctionAssignment` expression
 * type together with the function name and the list of argument names and put in the dictionary, again under
 * the function name. Later it can be retrieved from there by a `Function` expression type and evaluated.
 *
 * There is no overloading. There can be only one function with a given name in the dictionary.
 *
 * @param name A valid function name, according to the dictionary rules
 * @param argNames A sequence of valid arguments names, according to the dictionary rules (might be empty)
 * @param expr The expression associated with the function name and its arguments
 */

final case class FunctionAssignment(name: String, argNames: Seq[String], expr: Expression) extends Expression:
  override protected def evaluate(dict: Dictionary): Either[Error, Double] = expr.run(dict)

object FunctionAssignment extends Parseable[FunctionAssignment]:
  override def parse(parser: Parser, line: String): ParsedExpr[FunctionAssignment] =
    if !line.contains("=") then
      ParsedExpr.empty
    else
      val assignIndex   = line.indexOf('=')
      val assignmentStr = line.substring(0, assignIndex)
      ParsedFunction.parse(assignmentStr, LineSide.Left).flatMap {
        case Left(error) =>
          ParsedExpr.error(error)
        case Right(ParsedFunction(name, _)) if parser.dictionary.contains(name) =>
          ParsedExpr.error(s"The function already exists: $name")
        case Right(ParsedFunction(name, arguments)) =>
          val expressionStr = line.substring(assignIndex + 1)
          parseAssignment(parser, name, arguments, expressionStr)
      }

  private def parseAssignment(parser: Parser, name: String, arguments: Seq[String], expressionStr: String): ParsedExpr[FunctionAssignment] =
    parser
      .copy(arguments.map(arg => arg -> Variable(arg)).toMap)
      .parse(expressionStr)
      .happyPath { expression =>
        FunctionAssignment(name, arguments, expression).pipe { assignment =>
          parser.dictionary.add(name, assignment)
          ParsedExpr(assignment)
        }
      }
      .errorIfEmpty(s"Unable to parse: $expressionStr")
