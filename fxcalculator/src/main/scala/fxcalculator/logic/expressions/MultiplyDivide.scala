package fxcalculator.logic.expressions

import Error.EvaluationError
import fxcalculator.logic.{Dictionary, Parser}
import Expression.isZero

/**
 * MultiplyDivide
 * 
 * Analogically to addition and substraction, multiplication and division are also grouped together in one expression
 * type. `MultiplyDivide` works similarly to `AddSubstract`. There are however two differences (except the obvious one 
 * that these are different mathematical operations):
 * 1. No logic for checking if the '-' character we found is a binary minus.
 * 2. Division by zero should return an evaluation error, and sometimes it might be tricky to find out if we divide
 *    by zero - please look to `Expression` for a comment about floating-point precision handling.
 * 
 * @param left The expression parsed from the text left to the operator
 * @param right The expression parsed from the text right to the operator
 * @param isDivision true if the operator is '/', false if the operator is '+'
 */

final case class MultiplyDivide(left: Expression, right: Expression, isDivision: Boolean = false) extends Expression:
  override protected def evaluate(dict: Dictionary): Either[Error, Double] =
    val innerResults =
      for
        lResult <- left.run(dict)
        rResult <- right.run(dict)
      yield (lResult, rResult)
    innerResults.flatMap {
      case (l, r) if isDivision && isZero(r) => Left(EvaluationError(s"Division by zero: $l / $r"))
      case (l, r) if isDivision              => Right(l / r)
      case (l, r)                            => Right(l * r)
    }

object MultiplyDivide extends Parseable[MultiplyDivide]:
  override def parse(parser: Parser, line: String): ParsedExpr[MultiplyDivide] =
    val mulIndex = line.lastIndexOf("*")
    val divIndex = line.lastIndexOf("/")
    val (index, isDivision) = if mulIndex > divIndex then (mulIndex, false) else (divIndex, true)
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
        case (Right(l), Right(r)) => Right(MultiplyDivide(l, r, isDivision))
      }
