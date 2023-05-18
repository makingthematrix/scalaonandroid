package fxcalculator.logic.expressions

import fxcalculator.logic.{Dictionary, ParsedFunction, Parser, Preprocessor}
import fxcalculator.logic.ParsedFunction.LineSide
import fxcalculator.logic.Dictionary.isValidName

import scala.util.chaining.*

import fxcalculator.utils.Logger.*

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

final case class FunctionAssignment(override val name: String, argNames: Seq[String], definition: String, expr: Expression) extends Assignment:
  override protected def evaluate(dict: Dictionary): Either[Error, Double] = expr.run(dict)
  override def declaration: String = Assignment.functionDeclaration(name, argNames)
  override def textForm: String = s"$declaration = $definition"

object FunctionAssignment extends Parseable[FunctionAssignment]:
  override def parse(parser: Parser, line: String): ParsedExpr[FunctionAssignment] =
    if !line.contains("=") then
      ParsedExpr.empty
    else
      ParsedFunction.parse(line.takeWhile(_ != '='), LineSide.Left).flatMap {
        case Left(error) =>
          ParsedExpr.error(error)
        case Right(ParsedFunction(name, _)) if parser.dictionary.contains(name) =>
          ParsedExpr.error(s"The function already exists: $name")
        case Right(ParsedFunction(name, arguments)) =>
          parseAssignment(parser, name, arguments, line)
      }

  private def parseAssignment(parser: Parser, name: String, arguments: Seq[String], line: String): ParsedExpr[FunctionAssignment] =
    val definition = line.substring(line.indexOf('=') + 1)
    parser
      .copy(arguments.map(arg => arg -> Variable(arg)).toMap)
      .parse(definition)
      .happyPath { expression =>
        val assignment = FunctionAssignment(name, arguments, definition, expression)
        parser.dictionary.add(assignment)
        ParsedExpr(assignment)
      }
      .errorIfEmpty(s"Unable to parse: $definition")
