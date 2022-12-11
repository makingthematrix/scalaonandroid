package calculator.replcalc.expressions

import Error.EvaluationError
import calculator.replcalc.{Dictionary, Parser}
import Expression.isZero

import scala.math.{pow, round}

/**
 * Power
 *
 * Technically similar to addition and multiplication. It doesn't have a "twin" but it supports non-integer exponents.
 * The only thing preventing me from adding "root" as a twin of "power" is the lack of a suitable operator.
 * Complex numbers are not supported, but negative numbers to to the power of integer exponent are.
 * Zero to the power of zero raises an error. Any other number to the power of zero gives 1.0.
 *
 * @param left The expression parsed from the text left to the operator
 * @param right The expression parsed from the text right to the operator
 */
final case class Power(left: Expression, right: Expression) extends Expression:
  override protected def evaluate(dict: Dictionary): Either[Error, Double] =
    val innerResults = for
      lResult <- left.run(dict)
      rResult <- right.run(dict)
    yield
      (lResult, rResult)
    innerResults.flatMap {
      case (l, r) if isZero(l) && isZero(r)   => Left(EvaluationError("Zero to the power of zero"))
      case (_, r) if isZero(r)                => Right(1.0)
      case (l, r) if l < 0.0 && round(r) != r => Left(EvaluationError("Complex numbers not supported (yet)"))
      case (l, r)                             => Right(pow(l, r))
    }

object Power extends Parseable[Power]:
  override def parse(parser: Parser, line: String): ParsedExpr[Power] =
    val index = line.lastIndexOf("^")
    if index <= 0 || index >= line.length - 1 then
      ParsedExpr.empty
    else
      val innerExpressions =
        for
          lExpr <- parser.parse(line.substring(0, index))
          rExpr <- if lExpr.isRight then parser.parse(line.substring(index + 1)) else ParsedExpr.unused
        yield (lExpr, rExpr)
      innerExpressions.map {
        case (Left(error), _)     => Left(error)
        case (_, Left(error))     => Left(error)
        case (Right(l), Right(r)) => Right(Power(l, r))
      }


