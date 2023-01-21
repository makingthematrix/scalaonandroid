package calculator.logic

import munit.Location

class ParsedFunctionTest extends munit.FunSuite:
  implicit val location: Location = Location.empty

  test("Parse function line") {
    import ParsedFunction.{LineSide, parse}

    assertEquals(parse("foo(x)", LineSide.Left), Some(Right(ParsedFunction(name = "foo", arguments = Seq("x")))))
    assertEquals(parse("foo(x,y)", LineSide.Left), Some(Right(ParsedFunction(name = "foo", arguments = Seq("x", "y")))))
    assertEquals(parse("foo(1)", LineSide.Right), Some(Right(ParsedFunction(name = "foo", arguments = Seq("1")))))
    assert(parse("foo(1)", LineSide.Left).exists(_.isLeft)) // error; constants are not allowed on the left side
    assertEquals(parse("foo()", LineSide.Left), Some(Right(ParsedFunction(name = "foo", arguments = Seq.empty))))
    assertEquals(parse("foo", LineSide.Right), None) // not a function
    assertEquals(parse("foo", LineSide.Left), None) // not a function
    assertEquals(parse("1+2", LineSide.Right), None) // not a function
    assert(parse("foo(x))", LineSide.Left).exists(_.isLeft)) // error; parentheses don't match
    assert(parse("foo(x", LineSide.Left).exists(_.isLeft)) // error; parentheses don't match
    assert(parse("1_2(x)", LineSide.Left).exists(_.isLeft)) // error; invalid name
  }

  test("Split by commas") {
    import ParsedFunction.splitByCommas

    assertEquals(splitByCommas("a,b,c"), List("a", "b", "c"))
    assertEquals(splitByCommas("a,,c"), List("a", "", "c"))
    assertEquals(splitByCommas("a"), List("a"))
    assertEquals(splitByCommas(""), Nil)
    assertEquals(splitByCommas("a,foo(b,c),d"), List("a", "foo(b,c)", "d"))
    assertEquals(splitByCommas("foo(a,b,c),d"), List("foo(a,b,c)", "d"))
    assertEquals(splitByCommas("a,foo(b,c,d)"), List("a", "foo(b,c,d)"))
  }
