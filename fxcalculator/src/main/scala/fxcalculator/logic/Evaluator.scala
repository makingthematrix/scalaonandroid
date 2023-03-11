package fxcalculator.logic

import fxcalculator.logic.expressions.{Assignment, Error, Expression}

import scala.collection.mutable

trait Evaluator:
  def evaluate(text: String): EvaluationResults

final class EvaluatorImpl(parser: Parser) extends Evaluator:
  import Evaluator.split
  
  def evaluate(text: String): EvaluationResults =
    if text.contains('\n') then evaluateMultiLine(text) else evaluateLine(text)
  
  private def evaluateLine(line: String): EvaluationResults =
    parser.parse(line) match
      case None                          => EvaluationResults(error = Some(Error.ParsingError(s"Can't parse: $line")))
      case Some(Left(error))             => EvaluationResults(error = Some(error))
      case Some(Right(expr: Assignment)) => EvaluationResults(newAssignments = List(expr))
      case Some(Right(expr))             =>
        expr.run(parser.dictionary) match
          case Right(res)  => EvaluationResults(result = Some(res))
          case Left(error) => EvaluationResults(error = Some(error))

  private def evaluateMultiLine(text: String): EvaluationResults =
    val tempParser = parser.copy()
    val expressions = split(text).foldLeft[Either[Error, List[Expression]]](Right(Nil)) {
      case (Left(err), _)       => Left(err)
      case (Right(exprs), line) =>
        tempParser.parse(line) match
          case Some(Right(expr)) => Right(expr :: exprs)
          case Some(Left(err))   => Left(err)
          case None              => Left(Error.ParsingError(s"Can't parse: $line"))

    }
    expressions match
      case Left(error)  => EvaluationResults(error = Some(error))
      case Right(Nil)   => EvaluationResults()
      case Right(exprs) =>
        lazy val newAssignments = exprs.tail.reverse.collect { case expr: Assignment => expr }
        exprs.head match
          case _: Assignment => EvaluationResults(newAssignments = newAssignments)
          case expr          =>
            expr.run(tempParser.dictionary) match
              case Left(err)  => EvaluationResults(error = Some(err))
              case Right(res) => EvaluationResults(result = Some(res), newAssignments = newAssignments)

object Evaluator:
  def apply(parser: Parser): Evaluator = EvaluatorImpl(parser)
  
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
