package fxcalculator.logic

import fxcalculator.ParserCreator
import munit.{ComparisonFailException, Location}

import scala.util.chaining.*

class EvaluatorTest extends FxCalculatorSuite:
  test("dummy test") {
    given parser: Parser = ParserCreator.createParser(withNativeFunctions = true, withConstants = true)
    eval2("sin(Pi)+1", 1.0)
  }
