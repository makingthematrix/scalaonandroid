package fxcalculator.logic

import fxcalculator.logic.expressions.{Assignment, Error, Expression}

import scala.collection.mutable

object Evaluator:
  type EvaluationResult = Error | Double | Assignment

  final case class EvaluationInfo(result: EvaluationResult, assignments: Seq[(Assignment, String)])

  def evaluate(parser: Parser, text: String):  EvaluationInfo =
    val lines = split(text)
    if lines.isEmpty then
      EvaluationInfo(Error.ParsingError(s"Can't parse: $text"), Nil)
    else
      val tempParser = parser.copy()
      object Failed:
        def unapply(line: String): Option[Error] = run(tempParser, line) match
          case error: Error => Some(error)
          case _            => None
      lines.collectFirst { case Failed(error) => error } match
        case Some(error) => EvaluationInfo(error, Nil)
        case None        =>
          val results = lines.map(line => run(parser, line) -> line)
          EvaluationInfo(
            result = round(results.last._1),
            assignments = results.collect { case (ass: Assignment, line) => ass -> line }
          )

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

  private[logic] def round(number: Double): String =
    val v = scala.math.floor(number)
    if v == number then
      number.toString
    else
      val vabs = scala.math.abs(v)
      val restStr =
        (scala.math.round((scala.math.abs(number) - vabs) * 1000000.0) / 1000000.0)
          .toString
          .drop(2)
          .reverse
          .dropWhile(_ == '0')
          .reverse
      if scala.math.signum(number) == 1.0 then
        s"${vabs.toLong}.$restStr"
      else
        s"-${vabs.toLong}.$restStr"
