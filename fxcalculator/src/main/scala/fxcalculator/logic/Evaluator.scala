package fxcalculator.logic

import fxcalculator.logic.expressions.{Assignment, Error, Expression}

import scala.collection.mutable

final class Evaluator(parser: Parser):
  def evaluateLine(line: String): EvaluationResults =
    parser.parse(line) match
      case None                          => EvaluationResults(error = Some(Error.ParsingError(s"Can't parse: $line")))
      case Some(Left(error))             => EvaluationResults(error = Some(error))
      case Some(Right(expr: Assignment)) => EvaluationResults(newAssignments = List(expr))
      case Some(Right(expr)) =>
        expr.run(parser.dictionary) match
          case Right(res)  => EvaluationResults(result = Some(res))
          case Left(error) => EvaluationResults(error = Some(error))

  def evaluateMultiLine(text: String): EvaluationResults =
    val tempParser = parser.copy()
    val lines = Evaluator.split(text)
    val (expressions, error) = lines.foldLeft[(List[Expression], Option[Error])](Nil, None) {
      case ((_, Some(err)), _) =>
        (Nil, Some(err))
      case ((exprs, None), line) =>
        tempParser.parse(line) match
          case Some(Right(expr)) => (expr :: exprs, None)
          case Some(Left(err))   => (Nil, Some(err))
          case None              => (Nil, Some(Error.ParsingError(s"Can't parse: $line")))

    }

    error match
      case Some(_) => EvaluationResults(error = error)
      case None if expressions.isEmpty => EvaluationResults()
      case None =>
        val newAssignments = expressions.tail.reverse.collect { case expr: Assignment => expr }
        val lastExpr = expressions.head // the folding above creates the list of expression in the reversed order
        lastExpr match
          case _: Assignment => EvaluationResults(newAssignments = newAssignments)
          case expr =>
            expr.run(tempParser.dictionary) match
              case Left(err) => EvaluationResults(error = Some(err))
              case Right(res) => EvaluationResults(result = Some(res), newAssignments = newAssignments)

object Evaluator:
  def split(text: String): Seq[String] =
    val lines = new mutable.ArrayBuffer[String]
    val sb = new StringBuilder

    def add(): Unit =
      val line = sb.toString.trim
      if line.nonEmpty then lines.addOne(line)
      sb.clear()

    var parens = 0
    text.toCharArray.foreach {
      case '\n' if parens == 0 => add()
      case '\n' => // ignore
      case ' ' => // ignore
      case '\t' => // ignore
      case c =>
        sb.addOne(c)
        if c == '(' then parens += 1
        else if c == ')' then parens -= 1
    }
    add()
    lines.toSeq
