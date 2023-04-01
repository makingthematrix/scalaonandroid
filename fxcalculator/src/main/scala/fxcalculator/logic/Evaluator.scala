package fxcalculator.logic

import fxcalculator.logic.expressions.{Assignment, Error, Expression}

import scala.collection.mutable

object Evaluator:
  type EvaluationResult = Error | Double | Assignment
  def evaluate(parser: Parser, text: String):  EvaluationResult =
    val lines = split(text)
    if lines.isEmpty then
      Error.ParsingError(s"Can't parse: $text")
    else
      val tempParser = parser.copy()
      object Failed:
        def unapply(line: String): Option[Error] = run(tempParser, line) match
          case error: Error => Some(error)
          case _            => None
      lines.collectFirst { case Failed(error) => error } match
        case Some(error) => error
        case None        => lines.map(run(parser, _)).last

  private[logic] def run(parser: Parser, line: String):  EvaluationResult =
    parser.parse(line) match
      case Some(Right(ass: Assignment)) => ass
      case Some(Right(expr)) =>
        expr.run(parser.dictionary) match
          case Left(error)   => error
          case Right(result) => result
      case Some(Left(error)) => error
      case None              => Error.ParsingError(s"Can't parse: $line")

  private[logic] def split(text: String): Seq[String] =
    val lines = new mutable.ArrayBuffer[String]
    val sb = new StringBuilder

    def add(): Unit =
      val line = sb.toString.trim
      if line.nonEmpty then lines.addOne(line)
      sb.clear()

    var p = 0
    text.toCharArray.foreach {
      case '\n' if p == 0 => add()
      case '\n'           => // ignore
      case ' '            => // ignore
      case '\t'           => // ignore
      case c              =>
        sb.addOne(c)
        if c == '(' then p += 1
        else if c == ')' then p -= 1
    }
    add()
    lines.toSeq
