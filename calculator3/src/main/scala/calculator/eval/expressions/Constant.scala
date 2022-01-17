package calculator.eval.expressions

import calculator.eval.{Parser, Dictionary}

/**
 * Constant
 *
 * The most basic of expression types, it simply tries to parse the line into a Double.
 * Also used in eagerly evaluated variable assignments.
 *
 * @param number the parsed floating-point number
 */
final case class Constant(number: Double) extends Expression:
  override protected def evaluate(dict: Dictionary): Either[Error, Double] = Right(number)

object Constant extends Parseable[Constant]:
  override def parse(parser: Parser, line: String): ParsedExpr[Constant] =
    line
      .toDoubleOption
      .map(number => Right(Constant(number)))
