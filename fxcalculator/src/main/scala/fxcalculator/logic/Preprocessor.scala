package fxcalculator.logic

/**
 * Preprocessor
 *
 * The preprocessor is used to run checks and to simplify the text form of the expression before the parser will try
 * to turn it into an AST. The simplest example of its work is that it removes whitespaces - while parsing we often
 * have to look for the closest previous or next characters and not having to worry about skipping over whitespaces can
 * simplify the logic quite a bit.
 * In the case of this project, the preprocessor has also two other tasks: making sure that the arguments of the function
 * can be handled properly (e.g. parsing `foo(a * (b + c), d + e)` should properly parse the expressions in the arguments
 * and then pass those parsed expressions as arguments of the function), and turning expressions inside parentheses into
 * special, temporary variables (e.g. `1 + (2 + 3) * 4` will be turned into `1+$1*4` where `$1` holds the parsed
 * expression of `2+3`). In fact, the first task is done by wrapping function arguments in parentheses, so then they 
 * can be turned into special variables by the second task. 
 *
 * Each preprocessor task can be turned on or off by a flag. I use the flags mainly for unit tests, but in a bit more
 * complicated version of this project I might, for example, turn off removing whitespaces for any inner call to
 * the preprocessor. Removing whitespaces works once for the whole line of text. If we have nested expressions in it
 * and the preprocessor is going to be called on them, we know for sure that whitespaces were already removed.
 *
 * About recursion:
 * Methods in this class (and in ParsedFunction) use recursion to work on a line of text and, for example, perform
 * the same given task on a part of the text enclosed in parentheses. I made some of them tail-recursive but decided
 * not to do it with some others since the code here works with one line of text at the time - usually a quite short
 * line - so using non-tail-recursive methods is safe and, on the other hard, their code looks more readable.
 */

import fxcalculator.logic.Preprocessor.Flags
import fxcalculator.logic.expressions.{Error, Variable}
import fxcalculator.logic.expressions.Error.PreprocessorError
import fxcalculator.logic.Parser.isOperator
import fxcalculator.logic.Dictionary.isValidName
import fxcalculator.logic.ParsedFunction.LineSide

import scala.annotation.tailrec
import scala.util.chaining.*

trait Preprocessor {
  def setup(parser: Parser): Unit
  def process(line: String): Either[Error, String]
}

final class PreprocessorImpl(private var parser: Option[Parser],
                             private val flags: Flags) extends Preprocessor:
  import Preprocessor.*

  override def setup(parser: Parser): Unit =
    this.parser = Some(parser)

  override def process(line: String): Either[Error, String] =
    for
      validParser   <- parser.map(Right(_)).getOrElse(Left(PreprocessorError(s"Parser not set")))
      line          <- if flags.removeWhitespaces then removeWhitespaces(line) else Right(line)
      assignIndex   =  line.indexOf('=')
      (left, right) =  if assignIndex > 0 then (line.substring(0, assignIndex), line.substring(assignIndex + 1)) else ("", line)
      right         <- if flags.wrapFunctionArguments then wrapFunctionArguments(right) else Right(right)
      right         <- if flags.removeParens then removeParens(validParser, left, right) else Right(right)
    yield
      if left.isEmpty then right else s"$left=$right"

object Preprocessor:
  def apply(flags: Flags = Flags.AllFlagsOn): Preprocessor = new PreprocessorImpl(None, flags)

  final case class Flags(removeWhitespaces:     Boolean = true,
                         wrapFunctionArguments: Boolean = true,
                         removeParens:          Boolean = true)

  object Flags:
    val AllFlagsOn: Flags = Flags()

  def removeWhitespaces(line: String): Either[Error, String] =
    if line.exists(_.isWhitespace) then
      Right(line.filterNot(_.isWhitespace))
    else
      Right(line)

  def wrapFunctionArguments(line: String): Either[Error, String] =
    withParens(line, functionParens = true) { (opening, closing) =>
      val inside = line.substring(opening + 1, closing)
      val arguments =
        ParsedFunction.splitByCommas(inside).map {
          case arg if arg.forall(!isOperator(_)) && findParens(arg, functionParens = true).isEmpty =>
            Right(arg)
          case arg =>
            wrapFunctionArguments(arg).map(wrapped => s"($wrapped)")
        }
      val errors = arguments.collect { case Left(error) => error.msg }
      if errors.nonEmpty then
        Left(PreprocessorError(errors.mkString("; ")))
      else
        wrapFunctionArguments(line.substring(closing + 1)).map { post =>
          val pre     = line.substring(0, opening)
          val argList = arguments.collect { case Right(arg) => arg }.mkString(",")
          s"$pre($argList)$post"
        }
    }

  /**
   * There is no expression type for parentheses. Instead, parentheses are turned into special variables.
   * Each pair of parentheses is parsed as an individual, smaller expression, added to the dictionary under a special
   * name, and then it's replaced in the original like by that special name. For example "1 + (2 + 3) + 4" becomes
   * "1 + $1 + 4" where "$1" is the name of the variable. In the dictionary we now have an expression "2 + 3" under
   * this name. When evaluated, the variable "$1" will be replaced with the result of "2 + 3" and used to evaluate
   * the rest of the higher-level expression.
   *
   * Look here for a comment about how it works with function arguments: https://github.com/makingthematrix/replcalc/pull/47
   *
   * @param originalParser The main parser of the preprocessor. It's called "original" because the method may create
   *                       a copy of it and update it with function arguments.
   * @param left If the original line is a function assignment, the method needs to know its arguments, specified on 
   *             the left side, to be able to process the expression on the right side. 
   * @param right The expression in the text form
   * @return The expression with parentheses turned into special variables
   */
  def removeParens(originalParser: Parser, left: String, right: String): Either[Error, String] =
    def remove(parser: Parser, line: String): Either[Error, String] =
      withParens(line, functionParens = false) { (opening, closing) =>
        val pre  = line.substring(0, opening)
        val post = line.substring(closing + 1)
        if (pre.nonEmpty && !isOperator(pre.last, '(')) || (post.nonEmpty && !isOperator(post.head, ')')) then
          Left(PreprocessorError(s"Invalid characters around the function: $line"))
        else
          parser
            .parse(line.substring(opening + 1, closing))
            .map {
              case Left(error) =>
                Left(error)
              case Right(expr) =>
                val name = parser.dictionary.addSpecial(expr)
                remove(parser, s"$pre$name$post")
            }
            .getOrElse(Left(PreprocessorError(s"Unable to parse: $line")))
      }

    ParsedFunction.parse(left, LineSide.Left) match
      case None =>
        remove(originalParser, right)
      case Some(Left(error)) =>
        Left(error)
      case Some(Right(ParsedFunction(_, Nil))) =>
        remove(originalParser, right)
      case Some(Right(ParsedFunction(_, arguments))) =>
        val withArgs = originalParser.copy(arguments.map(arg => arg -> Variable(arg)).toMap)
        remove(withArgs, right).tap { _ =>
          (withArgs.dictionary.specials -- originalParser.dictionary.specials.keySet).foreach {
            case (name, expr) => originalParser.dictionary.add(name, expr, canBeSpecial = true)
          }
        }
  
  private def withParens(line: String, functionParens: Boolean)(body: (Int, Int) => Either[Error, String]): Either[Error, String] =
    findParens(line, functionParens) match
      case None                          => Right(line)
      case Some(Left(error))             => Left(error)
      case Some(Right(opening, closing)) => body(opening, closing)
      
  def findParens(line: String, functionParens: Boolean): Option[Either[Error, (Int, Int)]] =
    inline def isFunctionParens(line: String, atIndex: Int): Boolean = atIndex != 0 && !isOperator(line(atIndex - 1))
    
    val opening = line.indexOf('(')
    if opening == -1 then
      None
    else if (functionParens && isFunctionParens(line, opening)) || (!functionParens && !isFunctionParens(line, opening)) then
      findClosingParens(line.substring(opening))
        .map(offset => Right((opening, opening + offset)))
        .orElse(Some(Left(PreprocessorError(s"Unable to find the matching closing parenthesis: $line"))))
    else
      findParens(line.substring(opening + 1), functionParens)
        .map(nextParens =>
          nextParens.map {
            case (nextOpening, nextClosing) => (opening + 1 + nextOpening, opening + 1 + nextClosing)
          }
        )

  private def findClosingParens(expr: String): Option[Int] =
    if expr.isEmpty then
      None
    else
      val (index, counter) = expr.drop(1).foldLeft((0, 1)) {
        case ((index, 0), _)                         => (index, 0)
        case ((index, counter), '(')                 => (index + 1, counter + 1)
        case ((index, counter), ')') if counter == 0 => (index, counter - 1)
        case ((index, counter), ')')                 => (index + 1, counter - 1)
        case ((index, counter), _)                   => (index + 1, counter)
      }
      if counter == 0 then Some(index) else None
