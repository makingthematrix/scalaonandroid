package fxcalculator.logic

import munit.Location
import fxcalculator.logic.Preprocessor.Flags
import scala.util.chaining.*

class PreprocessorTest extends munit.FunSuite:
  implicit val location: Location = Location.empty

  private def setup(flags: Flags = Flags.AllFlagsOn): Preprocessor =
    val parser: Parser = new ParserImpl(Dictionary(), CustomAssignments(), None)
    new PreprocessorImpl(Some(parser), flags).tap { parser.setup }

  private def evalParens(line: String, prefix: String = "", suffix: String = "")(implicit pre: Preprocessor = setup()): Unit =
    pre.process(line) match
      case Left(error) =>
        fail(s"Parsing error: ${error.msg}")
      case Right(result) =>
        assert(result.startsWith(prefix))
        assert(result.endsWith(suffix))
        if !result.contains('=') then
          assert(!result.contains('('))
          assert(!result.contains(')'))
        else
          val rightPart = result.substring(result.indexOf('='))
          assert(!rightPart.contains('('))
          assert(!rightPart.contains(')'))
        assert(result.length > prefix.length + suffix.length)

  private def shouldFailParens(line: String)(implicit pre: Preprocessor = setup()): Unit =
    pre.process(line) match
      case Left(_)  =>
      case Right(_) => fail(s"Unfortunately this is working just fine: $line")

  test("Do nothing if the line does not contain whitespaces") {
    val pre =  setup()
    val line = "abcdef"
    assertEquals(pre.process(line), Right(line))
  }

  test("Remove whitespaces from the line") {
    val pre = setup()
    assertEquals(pre.process("abc def"), Right("abcdef"))
    assertEquals(pre.process(" abc def"), Right("abcdef"))
    assertEquals(pre.process("abc def "), Right("abcdef"))
    assertEquals(pre.process(" ab cd ef "), Right("abcdef"))
  }

  test("Replace parentheses with a special value") {
    implicit val pre: Preprocessor = setup()
    evalParens("1+(2+3)+4", "1+", "+4")
    evalParens("(1+2)")
    evalParens("(1+2)+3", "", "+3")
    evalParens("1+(2+3)", "1+", "")
  }

  test("Parentheses with assignments") {
    implicit val pre: Preprocessor = setup()
    evalParens("a = 1 + (2 + 3) + 4", "a=1+", "+4")
    evalParens("b = (1 + 2)")
    evalParens("c = (1 + 2) + 3", "c=", "+3")
    evalParens("d = 1+ (2 + 3)", "d=1+", "")
    evalParens("foo(x) = 1 + (2 + 3)", "foo(x)=1+", "")
  }

  test("Handle more than one set of parentheses") {
    implicit val pre: Preprocessor = setup()
    evalParens("1+(2+3)+(4+5)+6", "1+", "+6")
    evalParens("(1+2)+3+(4+5)")
    evalParens("(1+2)+(4+5)")
    evalParens("1+(2+3)+(4+5)", "1+")
    evalParens("(2+3)+(4+5)+6", "", "+6")
    evalParens("1+(2+3)+(4+5)+(6+7)+8", "1+", "+8")
  }

  test("Handle nested parentheses") {
    implicit val pre: Preprocessor = setup()
    evalParens("1+(2+(3+4)+5)+6", "1+", "+6")
    evalParens("(1+(2+(3+4)+5))+6", "", "+6")
    evalParens("1+((2+(3+4)+5)+6)", "1+", "")
  }

  test("Handle unclosed paretheses") {
    implicit val pre: Preprocessor = setup()
    shouldFailParens("(")
    shouldFailParens("((")
    shouldFailParens("(1+2)+(")
    shouldFailParens("(1+(2+(3+4)+5)+6")
    shouldFailParens("1+((2+(3+4)+5)+6")
    shouldFailParens("(1+2)3")
  }

  test("Ignore function parentheses") {
    val pre = setup(Flags(wrapFunctionArguments = false))

    assertEquals(pre.process("foo(1)"), Right("foo(1)"))
    assertEquals(pre.process("foo(1)+1"), Right("foo(1)+1"))
    assertEquals(pre.process("2+foo(3,4)"), Right("2+foo(3,4)"))

    pre.process("2+foo((3+1),4)") match
      case Right(result) =>
        assert(result.startsWith("2+foo("))
        assert(result.endsWith(",4)"))
        assert(!result.contains("(3+1)"))
      case Left(error) =>
        fail(error.msg)

    pre.process("foo((1),(2))") match
      case Right(result) =>
        assert(result.contains("foo("))
        val index = result.indexOf("foo(") + 5
        assert(result.substring(index).contains(')'))
        assert(!result.contains("(1)"))
        assert(!result.contains("(2)"))
      case Left(error) =>
        fail(error.msg)

    pre.process("(1+2)+foo((3+1),(4+5))+(6+7)") match
      case Right(result) =>
        assert(result.contains("+foo("))
        val index = result.indexOf("+foo(") + 5
        assert(result.substring(index).contains(')'))
        assert(!result.contains("(3+1)"))
        assert(!result.contains("(4+5)"))
      case Left(error) =>
        fail(error.msg)
  }

  test("Wrap parentheses around function arguments") {
    val pre = setup(Flags(removeParens = false))
    assertEquals(pre.process("foo(4)"), Right("foo(4)"))
    assertEquals(pre.process("foo(4+5)"), Right("foo((4+5))"))
    assertEquals(pre.process("foo(4,5)"), Right("foo(4,5)"))
    assertEquals(pre.process("foo(1+2,3+4)"), Right("foo((1+2),(3+4))"))
    assertEquals(pre.process("foo(4)+bar(5)"), Right("foo(4)+bar(5)"))
    assertEquals(pre.process("foo(1+2,3+4)+bar(1+2,3+4)"), Right("foo((1+2),(3+4))+bar((1+2),(3+4))"))
    assertEquals(pre.process("(foo(1+2,3+4)+2)+bar(1+2,3+4)"), Right("(foo((1+2),(3+4))+2)+bar((1+2),(3+4))"))
    assertEquals(pre.process("foo(1,bar(2,3),4)"), Right("foo(1,(bar(2,3)),4)"))
  }
