package calculator.eval.expressions

import calculator.eval.{Parser, Dictionary}

/**
 * Expression and Parseable traits
 *
 * Processing of an expression works in two steps:
 * 1. Parsing. This is done by the `parse` method of the expression type `object`. The trait `Parseable` is here
 *    to show how the signature of that method should look like. The method takes a reference to the parser (in case
 *    there were nested expressions that need to be parsed first) and the line of text. It returns a `ParsedExpr[T]`
 *    where `T` is the type implementing `Expression`.
 * 2. Evaluating. If the result of the parsing is a valid expression, and we have an entity of a class implementing
 *    `Expression`, it can be now evaluated with the `evaluate` method declared in the `Expression` trait.
 *    This method takes the dictionary (from the parser) because in evaluation it may need to use variables or functions.
 *    It will return either an error or a valid result of the type `Double`.
 *
 * Because of the floating point imprecision, sometimes even if we have a valid result, it's not the result we expected
 * - we can get a very small number instead of zero. I decided to deal with it by calculating the result of
 * "(1/3)*3 - 1/3 - 1/3 - 1/3", which on my computer is around 1.1E-16, and then I treat every evaluation result less 
 * or equal to this magic number as if it was zero.
 */

trait Parseable[T <: Expression]:
  def parse(parser: Parser, line: String): ParsedExpr[T]

trait Expression:
  protected def evaluate(dict: Dictionary): Either[Error, Double]
  final def run(dict: Dictionary): Either[Error, Double] = evaluate(dict).map(Expression.round(_))

object Expression:
  private val DIVISION_PRECISION: Double = ((1.0/3.0)*3.0)-(1.0/3.0)-(1.0/3.0)-(1.0/3.0)

  inline def isZero(number: Double): Boolean = scala.math.abs(number) <= DIVISION_PRECISION
  inline def round(number: Double): Double = if isZero(number) then 0.0 else number
