package calculator.logic.expressions

/**
 * ParsedExpr[T]
 *
 * There are three possible outcomes when an expression is parsed from a line of text: a valid expression, an error,
 * or `None` which indicates that the line can't be parsed in a given way and the parser should try another one.
 * I decided to model it using `Option[Either[Error, T]]`, where `T` is an expression type, but working with options
 * and eithers can be verbose, and I wanted to put this verbosity a bit to the side, so that the logic of the given
 * type expression can focus on the "happy path", i.e. what should be done if we have a valid expression.
 * So I created a type alias `ParsedExpr[T]` and a few utility methods that take care of errors and the "None" outcome.
 * It's not perfect - there are specific cases of errors which the expression type logic has to handle, and also
 * I sometimes take advantage of that I'm working with `Option` and/or `Either` (that's why it's not an opaque type).
 * But I experimented a bit with two others possibilities - one with an enum, the other with a case class - and I think
 * this one works the best of all three.
 */

import Error.ParsingError

type ParsedExpr[T <: Expression] = Option[Either[Error, T]]

extension [T <: Expression](parsedExpr: ParsedExpr[T])
  def happyPath[S <: Expression](f: T => ParsedExpr[S]): ParsedExpr[S] =
    parsedExpr.flatMap {
      case Left(error)       => ParsedExpr.error(error)
      case Right(expression) => f(expression)
    }

  def errorIfEmpty(error: Error): ParsedExpr[T] = parsedExpr.orElse(ParsedExpr.error(error))
  def errorIfEmpty(errorMsg: String): ParsedExpr[T] = parsedExpr.orElse(ParsedExpr.error(errorMsg))

object ParsedExpr:
  def apply[T <: Expression](expr: T): ParsedExpr[T] = Some(Right(expr))
  def error[T <: Expression](error: Error): ParsedExpr[T] = Some(Left(error))
  def error[T <: Expression](errorMsg: String): ParsedExpr[T] = Some(Left(ParsingError(errorMsg)))
  def empty[T <: Expression]: ParsedExpr[T] = Option.empty[Either[Error, T]]
  def unused[T <: Expression]: ParsedExpr[T] = Some(Left(ParsingError("Unused expression")))
