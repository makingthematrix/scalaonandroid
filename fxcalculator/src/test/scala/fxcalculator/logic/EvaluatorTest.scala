package fxcalculator.logic

import fxcalculator.ParserCreator
import munit.{ComparisonFailException, Location}

import scala.util.chaining.*
import scala.math.*

class EvaluatorTest extends FxCalculatorSuite:
  given parser: Parser = ParserCreator.createParser(withNativeFunctions = true, withConstants = true)

  test("dummy test") {
    eval2("sin(Pi)+1", 1.0)
  }

  test("rounding a positive number down") {
    val res = eval2(
      s"""
         |earthRadius = 6378.1
         |bridgeLength = 0.575
         |circle(x) = sqrt(1 - x * x)
         |circle(bridgeLength/earthRadius) * earthRadius
         |""".stripMargin,
      6378.099974081231
    )
    val resStr = Evaluator.round(res)
    assertEquals(resStr, "6378.099974")
  }
