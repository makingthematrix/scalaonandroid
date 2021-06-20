package calculator.eval

import scala.annotation.tailrec

sealed trait ExprNode:
  def evaluate: Double

final case class Number(number: Double) extends ExprNode:
  override def evaluate: Double = number

final case class Add(nodes: Seq[ExprNode]) extends ExprNode:
  override def evaluate: Double = nodes.map(_.evaluate).sum

final case class Substract(nodes: Seq[ExprNode]) extends ExprNode:
  override def evaluate: Double = nodes.map(_.evaluate).reduce[Double] { case (l, r) => l - r }

final case class Multiply(nodes: Seq[ExprNode]) extends ExprNode:
  override def evaluate: Double = nodes.map(_.evaluate).reduce[Double] { case (l, r) => l * r }

final case class Divide(nodes: Seq[ExprNode]) extends ExprNode:
  override def evaluate: Double =
    nodes.map(_.evaluate).reduce[Double] {
      case (_, r) if r.isNaN || r == 0.0 => Double.NaN
      case (l, r) => l / r
    }

final case class Expression(expression: String) extends ExprNode:
  import Eval.operators
  
  override def evaluate: Double = parse(expression).evaluate

  private def parse(expr: String): ExprNode =
    if (expr.lastOption.exists(operators.contains)) then parse(expr.init)
    else if (expr.contains('+')) then Add(expr.split('+').map(parse).toIndexedSeq)
    else if (expr.contains('-')) then
      if (expr.startsWith("--")) then parse(expr.drop(2))
      else if (expr.head == '-') then parse(s"0$expr")
      else if (expr.contains("+-") || expr.contains("--") || expr.contains("*-") || expr.contains("/-")) then
        parse(bubbleMinusUp(expr))
      else Substract(expr.split('-').map(parse).toIndexedSeq)
    else if (expr.contains('*')) then Multiply(expr.split('*').map(parse).toIndexedSeq)
    else if (expr.contains('/')) then Divide(expr.split('/').map(parse).toIndexedSeq)
    else Number(expr.toDoubleOption.getOrElse(Double.NaN))

  // https://drive.google.com/file/d/1XEJy1LkOoYD5SdRQDR2rtZcVPpMuFhhV/view?usp=sharing
  @tailrec private def bubbleMinusUp(expr: String): String =
    val simplifiedExpr =
      expr
        .replaceAll("^--", "")
        .replaceAll("\\+-", "-")
        .replaceAll("--", "+")

    val digraph =
      if (simplifiedExpr.contains("*-")) then "*-"
      else if (simplifiedExpr.contains("/-")) then "/-"
      else ""

    if (digraph == "") then simplifiedExpr
    else
      val diRemoved =
        if (digraph == "*-") then
          simplifiedExpr.replaceFirst("\\*-", "*")
        else
          simplifiedExpr.replaceFirst("/-", "/")

      val prefix = diRemoved.substring(0, simplifiedExpr.indexOf(digraph))

      prefix.findLast(operators.contains) match
        case None =>
          bubbleMinusUp(s"-$diRemoved")
        case Some(op) =>
          val (before, after) = diRemoved.splitAt(prefix.lastIndexOf(op) + 1)
          bubbleMinusUp(s"$before-$after")

object Eval:
  val operators: Set[Char] = Set('+', '-', '*', '/')
  val numbers  : Set[Char] = Set('1', '2', '3', '4', '5', '6', '7', '8', '9', '0')

  def apply(expression: String): ExprNode = Expression(expression)
