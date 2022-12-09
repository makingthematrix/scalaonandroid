package calculator

import calculator.replcalc.expressions.{Assignment, Constant, Expression, FunctionAssignment}

package object replcalc:
  def list(dictionary: Dictionary): Unit =
    dictionary
      .expressions
      .toSeq.sortBy(_._1).map(_._2)
      .map(replForm(dictionary, _))
      .foreach(println)

  def run(parser: Parser, line: String): Option[String] =
    parser
      .parse(line)
      .map {
        case Right(expr) => replForm(parser.dictionary, expr)
        case Left(error) => error.toString
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

