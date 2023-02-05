package fxcalculator.logic.expressions

import fxcalculator.logic.{Dictionary, Parser}
import fxcalculator.logic.Parser.isOperator

import scala.annotation.tailrec

/**
 * AddSubstract
 *
 * Addition and substraction are grouped together under one expression type. If the expression has more than just one
 * "+" or "-" operator, they are parsed from right to left to preserve the proper order of operations. So "1 - 2 + 3"
 * will be parsed as "(1 - 2) + 3" - the "+" will be parsed first, and then "1 - 2" will be treated as a nested expression.
 *
 * One special operation needed here is that in search for the "-" operator the program needs to distinguish between
 * the binary and the unary "-". This is done by looking at the previous character. If it's an operator, we use 
 * recursion (tailrec, this time) to search for another "-" more to the left of the one we have just found.
 * 
 * @param left The expression parsed from the text left to the operator
 * @param right The expression parsed from the text right to the operator
 * @param isSubstraction true if the operator is '-', false if the operator is '+'
 */

final case class AddSubstract(left: Expression, right: Expression, isSubstraction: Boolean = false) extends Expression:
  override protected def evaluate(dict: Dictionary): Either[Error, Double] =
    for
      lResult <- left.run(dict)
      rResult <- right.run(dict)
    yield
      if isSubstraction then 
        lResult - rResult 
      else 
        lResult + rResult

object AddSubstract extends Parseable[AddSubstract]:
  override def parse(parser: Parser, line: String): ParsedExpr[AddSubstract] =
    val plusIndex  = line.lastIndexOf("+")
    val minusIndex = lastBinaryMinus(line)
    val (index, isSubstraction) = if plusIndex > minusIndex then (plusIndex, false) else (minusIndex, true)
    if index <= 0 || index >= line.length - 1 then
      ParsedExpr.empty
    else
      val innerExpressions =
        for
          lExpr <- parser.parse(line.substring(0, index))
          rExpr <- if (lExpr.isRight) parser.parse(line.substring(index + 1)) else ParsedExpr.unused
        yield (lExpr, rExpr)
      innerExpressions.map {
        case (Left(error), _)     => Left(error)
        case (_, Left(error))     => Left(error)
        case (Right(l), Right(r)) => Right(AddSubstract(l, r, isSubstraction))
      }
    
  @tailrec
  private def lastBinaryMinus(line: String): Int =
    line.lastIndexOf("-") match
      case index if index <= 0                          => -1
      case index if !isOperator(line.charAt(index - 1)) => index
      case index                                        => lastBinaryMinus(line.substring(0, index))
