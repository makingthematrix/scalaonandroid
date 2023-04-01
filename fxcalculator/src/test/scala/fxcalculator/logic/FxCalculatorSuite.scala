package fxcalculator.logic

import munit.Location

abstract class FxCalculatorSuite extends munit.FunSuite:
  given location: Location = Location.empty
  
  def eval(str: String, expected: Double, delta: Double = 0.001)(using parser: Parser = Parser()): Unit =
    parser.parse(str) match
      case None =>
        failComparison("Parsed as 'none'", str, expected)
      case Some(Left(error)) =>
        failComparison(s"Error: ${error.msg}", str, expected)
      case Some(Right(expr)) =>
        expr.run(parser.dictionary) match
          case Right(result) => assertEqualsDouble(result, expected, delta)
          case Left(error) => failComparison(s"Error: ${error.msg}", str, expected)

  def eval2(str: String, expected: Double, delta: Double = 0.001)(using parser: Parser = Parser()): Unit =
    Evaluator.evaluate(parser, str) match
      case Right(result) => assertEqualsDouble(result, expected, delta)
      case Left(error)   => failComparison(s"Error: $error", str, expected)
      case _             => failComparison("No result", str, expected)
        
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
