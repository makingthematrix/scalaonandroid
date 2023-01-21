package calculator.logic

/**
 * Parser
 *
 * Arguably the most important entity in the project, the parser turns a line from the REPL into
 * an Abstract Syntax Tree of expressions. Down below you will find a sequence of "stages" used for that -
 * the parser gives the line to each in order, looking for the first stage that returns with a valid expression
 * or an error, but not None which indicates "Not my expression type". Each stage can in turn call the parser to
 * make sense of its inner expressions, e.g. the addition will consist of two sub-expressions, which also need
 * to be parsed, wrapped together by the Addition expression. Sub-expressions may contain their own sub-expressions,
 * and so on, until somewhere at the bottom we get to constants and variables, which don't need more parsing.
 * This way, through recursive calls between stages, i.e. expression types, and the parser, the whole AST is created.
 * The AST may then be immediately evaluated or saved for later evaluation in the dictionary, under an assigned name.
 *
 * About the parser being the most important entity:
 * There are two more classes wrapping the logic in the project - `Dictionary`` and `Preprocessor`. While it seems
 * to work well that the dictionary is owned by the parser, the preprocessor is more on an equal footing with the parser.
 * I thin it's good, at least for unit tests, to have them loosely coupled. I tried a few solutions and in the end
 * decided that it should be possible to create them independent from each other, and then, to make them work, they
 * will be connected through `setup` methods where I pass to them references to each other.
 * There is an utility `apply` method in `Parser` which makes it simpler, so `Parser` can be still seen as a bit more
 * important of the two.
 *
 * About using a trait and an implementation class:
 * It hardly matters for such a small project but I use this pattern a lot in my work so I thought I would do it here a
 * s well just to show how I like to organize the code. For entities wrapping logic, I like to have a trait and
 * an implementation class, and then an `apply` method which makes it pretty invisible from the outside that it is not
 * just one regular class. The separation of the trait and the implementation lets me better see what parts of
 * the functionality should be hidden, it lets me write mocks easier in unit tests, and from time to time it actually
 * makes sense to have, e.g., two different implementations of one trait. Note that I didn't do it for `Dictionary`.
 * I consider `Dictionary` to be just a thin wrapper around `Map[String, Expression]`.
 */

import calculator.logic.Preprocessor.Flags
import calculator.logic.expressions.Error.ParsingError
import calculator.logic.expressions.*

import scala.util.chaining.*

trait Parser {
  val dictionary: Dictionary
  def setup(preprocessor: Preprocessor): Unit
  def copy(updates: Map[String, Expression]): Parser
  def parse(line: String): ParsedExpr[Expression]
}

final class ParserImpl(override val dictionary: Dictionary, 
                       private var preprocessor: Option[Preprocessor]) extends Parser:
  self =>
  import Parser.*
  
  override def setup(preprocessor: Preprocessor): Unit =
    this.preprocessor = Some(preprocessor)

  override def copy(updates: Map[String, Expression]): Parser =
    Parser(dictionary.copy(updates))

  override def parse(line: String): ParsedExpr[Expression] =
    preprocess(line) match
      case Left(error) =>
        ParsedExpr.error(error)
      case Right(processed) =>
        // about early returns in Scala: https://makingthematrix.wordpress.com/2021/03/09/many-happy-early-returns/
        object Parsed:
          def unapply[T <: Expression](stage: Parseable[T]): ParsedExpr[T] = stage.parse(self, processed)
        stages.collectFirst { case Parsed(expr) => expr }

  private def preprocess(line: String): Either[Error, String] =
    preprocessor match
      case Some(pre) => pre.process(line)
      case None      => Left(ParsingError("The preprocessor is not set up"))

object Parser:
  def apply(dictionary: Dictionary = Dictionary(), flags: Flags = Flags.AllFlagsOn): Parser =
    new ParserImpl(dictionary, None).tap { parser =>
      val preprocessor = Preprocessor(flags = flags)
      parser.setup(preprocessor)
      preprocessor.setup(parser)
    }

  def isOperator(char: Char, additionalAllowed: Char*): Boolean = 
    operators.contains(char) || additionalAllowed.contains(char)

  val operators: Set[Char] = Set('+', '-', '*', '/', '^', ',', '=')

  // we rely on the order here
  val stages: Seq[Parseable[_ <: Expression]] =
    Seq(
      FunctionAssignment,
      Assignment,
      AddSubstract,
      MultiplyDivide,
      Power,
      UnaryMinus,
      Function,
      Variable,
      Constant,
      Failure
    )
