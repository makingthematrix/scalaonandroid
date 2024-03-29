package fxcalculator.logic

import fxcalculator.logic.expressions.Assignment
import munit.Location

abstract class FxCalculatorSuite extends munit.FunSuite:
  given location: Location = Location.empty
  
  def eval(str: String, expected: Double, delta: Double = 0.001)(using parser: Parser = Parser()): Double =
    parser.parse(str) match
      case None =>
        failComparison("Parsed as 'none'", str, expected)
        Double.NaN
      case Some(Left(error)) =>
        failComparison(s"Error: ${error.msg}", str, expected)
        Double.NaN
      case Some(Right(expr)) =>
        expr.run(parser.dictionary) match
          case Right(result) => 
            assertEqualsDouble(result, expected, delta)
            result
          case Left(error) => 
            failComparison(s"Error: ${error.msg}", str, expected)
            Double.NaN

  def eval2(str: String, expected: Double, delta: Double = 0.001)(using parser: Parser = Parser()): Double =
    Evaluator.evaluate(parser, str).result match
      case result: Double  => 
        assertEqualsDouble(result, expected, delta)
        result
      case error: Error    => 
        failComparison(s"Error: $error", str, expected)
        Double.NaN
      case ass: Assignment => 
        failComparison(s"Expected a result, got an assignment: $ass", str, expected)
        Double.NaN
      case other =>
        fail(s"Unable to evaluate: $other")
        
  def parse(str: String)(using parser: Parser = Parser()): Unit =
    parser.parse(str) match
      case None => fail(s"Parsed as 'none': $str")
      case Some(Left(error)) => fail(s"Error: ${error.msg} at line $str")
      case Some(Right(_)) =>

  def shouldReturnParsingError(line: String)(using parser: Parser = Parser()): Unit =
    parser.parse(line) match
      case None => fail(s"Parsed as 'none': $line")
      case Some(Left(_)) =>
      case Some(Right(expr)) => fail(s"Parsed with success: $line -> $expr")
