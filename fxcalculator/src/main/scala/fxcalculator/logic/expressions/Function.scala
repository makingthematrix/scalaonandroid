package fxcalculator.logic.expressions

import fxcalculator.logic.{Dictionary, ParsedFunction, Parser, Preprocessor}
import fxcalculator.logic.expressions.Error.EvaluationError
import fxcalculator.logic.ParsedFunction.LineSide

import scala.util.{Failure, Success, Try}

/**
 * Function
 *
 * The name "Function" is a bit of a misnomer. In the context of this project `Function` relates to a function call -
 * an expression or a part of an expression that consists of a function name and a sequence of expression of a lower
 * order which stand for function arguments. The number of expressions must be the same as the number of arguments
 * names in the associated function assignment in the dictionary.
 *
 * Although in the function assignment we require parentheses to differentiate between function and variable assignment,
 * on the expression side it's allowed to use a function without the parentheses if its list of arguments is empty.
 * It works as in Scala - it's still a function, it cannot be reassigned, and it's evaluated on demand, not eagerly,
 * as in case of variable assignments. In fact, my first idea in this project was not to have reassignments and
 * evaluate variables (or rather "values") on demand. This is a remnant of that idea, so to say.
 *
 * @param name A valid function name, according to the dictionary rules
 * @param args A sequence of expressions which should match arguments in the associated function assignment in the
 *             dictionary. It might be empty.
 */

final case class Function(name: String, args: Seq[Expression]) extends Expression:
  override protected def evaluate(dict: Dictionary): Either[Error, Double] =
    dict.get(name) match
      case Some(f: NativeFunction) if f.argNames.length == args.length =>
        evaluateArgs(dict).flatMap { evaluatedArgs =>
          Try(f.f(evaluatedArgs)) match
            case Failure(error) => Left(EvaluationError(error.getMessage))
            case Success(result) if result.isNaN => Left(EvaluationError("Not a number"))
            case Success(result) if result.isPosInfinity => Left(EvaluationError("+Infinity"))
            case Success(result) if result.isNegInfinity => Left(EvaluationError("-Infinity"))
            case Success(result) => Right(Expression.round(result))
        }
      case Some(f: FunctionAssignment) if f.argNames.length == args.length =>
        evaluateArgs(dict).flatMap { evaluatedArgs =>
          val validArgs = evaluatedArgs.map { Constant(_) }
          val argMap = f.argNames.zip(validArgs).toMap
          val newDict = dict.copy(argMap)
          f.run(newDict)
        }
      case _ =>
        Left(EvaluationError(s"Function not found: $name with ${args.length} arguments"))
  
  private def evaluateArgs(dict: Dictionary): Either[Error, Seq[Double]] =
    val evaluatedArgs = args.map(_.run(dict))
    val evaluationErrors = evaluatedArgs.collect { case Left(error) => error }
    if evaluationErrors.nonEmpty then
      Left(EvaluationError(evaluationErrors.mkString("; ")))
    else
      Right(evaluatedArgs.collect { case Right(number) => number })


object Function extends Parseable[Function]:
  override def parse(parser: Parser, line: String): ParsedExpr[Function] =
    ParsedFunction.parse(line, LineSide.Right).flatMap {
      case Left(error) =>
        ParsedExpr.error(error)
      case Right(ParsedFunction(name, _)) if !parser.dictionary.contains(name) =>
        ParsedExpr.error(s"Function not found: $name")
      case Right(ParsedFunction(name, arguments)) =>
        parseFunction(parser, name, arguments)
    }

  private def parseFunction(parser: Parser, name: String, args: Seq[String]): ParsedExpr[Function] =
    val parsedArgs = args.map { arg => arg -> parser.parse(arg) }
    val parseErrors = parsedArgs.collect {
      case (argName, None)              => s"Unable to parse argument $argName"
      case (argName, Some(Left(error))) => s"Unable to parse argument $argName: ${error.msg}"
    }
    if parseErrors.nonEmpty then
      ParsedExpr.error(parseErrors.mkString("; "))
    else
      val validArgs = parsedArgs.collect { case (_, Some(Right(expr))) => expr }
      ParsedExpr(Function(name, validArgs))
