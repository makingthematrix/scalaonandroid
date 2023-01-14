package calculator

import calculator.replcalc.expressions.{Assignment, Constant, Expression, FunctionAssignment}

package object replcalc:
  def list(dictionary: Dictionary): Unit =
    dictionary
      .expressions
      .toSeq.sortBy(_._1).map(_._2)
      .map(replForm(dictionary, _))
      .foreach(println)

  def run(parser: Parser, line: String): Either[String, String] =
    parser.parse(line) match {
      case Some(Right(expr)) => Right(replForm(parser.dictionary, expr))
      case Some(Left(error)) => Left(error.toString)
      case None              => Left(s"Can't parse: $line")
    }

  private def replForm(dictionary: Dictionary, expression: Expression): String =
    expression match
      case FunctionAssignment(name, args, _) =>
        s"$name(${args.mkString(", ")}) -> Function"
      case Assignment(name, Constant(number)) =>
        s"$name -> $number"
      case expr =>
        expr.run(dictionary) match
          case Right(result) => result.toString
          case Left(error)   => error.toString

