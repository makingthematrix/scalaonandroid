package calculator.eval

import scala.annotation.tailrec

object Eval {
  val operators: Set[Char] = Set('+', '-', '*', '/')
  val numbers: Set[Char] = Set('1', '2', '3', '4', '5', '6', '7', '8', '9', '0')

  sealed trait ExprNode {
    def evaluate: Double
  }

  final case class Number(number: Double) extends ExprNode {
    override def evaluate: Double = number
  }

  final case class Add(nodes: Seq[ExprNode]) extends ExprNode {
    override def evaluate: Double = nodes.map(_.evaluate).sum
  }

  final case class Substract(nodes: Seq[ExprNode]) extends ExprNode {
    override def evaluate: Double = nodes.map(_.evaluate).reduce[Double] { case (l, r) => l - r }
  }

  final case class Multiply(nodes: Seq[ExprNode]) extends ExprNode {
    override def evaluate: Double = nodes.map(_.evaluate).reduce[Double] { case (l, r) => l * r }
  }

  final case class Divide(nodes: Seq[ExprNode]) extends ExprNode {
    override def evaluate: Double =
      nodes.map(_.evaluate).reduce[Double] {
        case (_, r) if r.isNaN || r == 0.0 => Double.NaN
        case (l, r) => l / r
      }
  }
// "1 + 2 + 3"
  final case class Expression(expression: String) extends ExprNode {
    override def evaluate: Double = parse(expression).evaluate

  // ((1+2)+3)
    private def findMatchingParens(expr: String): Int =
      expr.drop(1).foldLeft((0, 1)) {
        case ((index, 0), _)                         => (index, 0)
        case ((index, counter), '(')                 => (index + 1, counter + 1)
        case ((index, counter), ')') if counter == 0 => (index, counter - 1)
        case ((index, counter), ')')                 => (index + 1, counter - 1)
        case ((index, counter), _)                   => (index + 1, counter)
      }._1

  // ((1+2)+3)
    private def makeParens(expr: String): ExprNode = {
      val opening = expr.indexOf("(")
      val closing = opening + findMatchingParens(expr.substring(opening))
      val inside = expr.substring(opening + 1, closing)
      val insideExpr = parse(inside)
      val result = insideExpr.evaluate
      parse(expr.replace(s"($inside)", result.toString)) // (1+4)*2 -> 5*2
    }

  // (1+4)*(2+1) // Multiply(Seq(Expression("1+4"), Expression("2+1"))
    private def parse(expr: String): ExprNode =
      if (expr.lastOption.exists(operators.contains)) parse(expr.init)
      else if (expr.contains('(')) makeParens(expr)
      else if (expr.contains('+')) Add(expr.split('+').map(parse)) // "1 + 2 + 3" ->"1" , "2", "3"
      else if (expr.contains('-')) {
        if (expr.startsWith("--")) parse(expr.drop(2))
        else if (expr.head == '-') parse(s"0$expr")
        else if (expr.contains("+-") || expr.contains("--") || expr.contains("*-") || expr.contains("/-"))
          parse(bubbleMinusUp(expr))
        else Substract(expr.split('-').map(parse))
      }
      else if (expr.contains('*')) Multiply(expr.split('*').map(parse))
      else if (expr.contains('/')) Divide(expr.split('/').map(parse))
      else Number(expr.toDoubleOption.getOrElse(Double.NaN))

    // https://drive.google.com/file/d/1XEJy1LkOoYD5SdRQDR2rtZcVPpMuFhhV/view?usp=sharing
    @tailrec private def bubbleMinusUp(expr: String): String = {
      val simplifiedExpr =
        expr
          .replaceAll("^--", "")
          .replaceAll("\\+-", "-")
          .replaceAll("--", "+")

      val digraph =
        if (simplifiedExpr.contains("*-")) "*-"
        else if (simplifiedExpr.contains("/-")) "/-"
        else ""

      if (digraph == "") simplifiedExpr
      else  {
        val diRemoved =
          if (digraph == "*-")
            simplifiedExpr.replaceFirst("\\*-", "*")
          else
            simplifiedExpr.replaceFirst("/-", "/")

        val prefix = diRemoved.substring(0, simplifiedExpr.indexOf(digraph))

        prefix.findLast(operators.contains) match {
          case None =>
            bubbleMinusUp(s"-$diRemoved")
          case Some(op) =>
            val (before, after) = diRemoved.splitAt(prefix.lastIndexOf(op) + 1)
            bubbleMinusUp(s"$before-$after")
        }
      }
    }
  }

  // 1 + 2 + 3
  def apply(expression: String): ExprNode = Expression(expression)
}
