package calculator.eval

object Eval {
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

  final case class Expression(expression: String) extends ExprNode {
    override def evaluate: Double = parse(expression).evaluate
  }

  def parse(expr: String): ExprNode =
    if (expr.contains('+')) Add(expr.split('+').map(parse))
    else if (expr.contains('-')) {
      if (expr.startsWith("-")) Substract(Seq(Number(0.0), parse(expr.drop(1))))
      else Substract(expr.split('-').map(parse))
    }
    else if (expr.contains('*')) Multiply(expr.split('*').map(parse))
    else if (expr.contains('/')) Divide(expr.split('/').map(parse))
    else Number(expr.toDoubleOption.getOrElse(Double.NaN))
}
