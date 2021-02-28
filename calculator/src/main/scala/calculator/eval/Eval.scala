package calculator.eval

object Eval {
  sealed trait ExprNode {
    def evaluate: Double
  }

  final case class Number(number: Double) extends ExprNode {
    override def evaluate: Double = {
      println(s"evaluate $this => $number")
      number
    }
  }

  final case class Add(nodes: Seq[ExprNode]) extends ExprNode {
    override def evaluate: Double = {
      val res = nodes.map(_.evaluate).sum
      println(s"evaluate $this => $res")
      res
    }
  }

  final case class Substract(nodes: Seq[ExprNode]) extends ExprNode {
    override def evaluate: Double = {
      val res = nodes.map(_.evaluate).reduce[Double] { case (l, r) => l - r }
      println(s"evaluate $this => $res")
      res
    }
  }

  final case class Multiply(nodes: Seq[ExprNode]) extends ExprNode {
    override def evaluate: Double = {
      val res = nodes.map(_.evaluate).reduce[Double] { case (l, r) => l * r }
      println(s"evaluate $this => $res")
      res
    }
  }

  final case class Divide(nodes: Seq[ExprNode]) extends ExprNode {
    override def evaluate: Double = {
      val res = nodes.map(_.evaluate).reduce[Double] {
        case (_, r) if r.isNaN || r == 0.0 => Double.NaN
        case (l, r) => l / r
      }
      println(s"evaluate $this => $res")
      res
    }
  }

  final case class Expression(expression: String) extends ExprNode {
    override def evaluate: Double = parse(expression).evaluate
  }

  def parse(expression: String): ExprNode = {
    val res = if (expression.contains('+')) Add(expression.split('+').map(parse))
    else if (expression.contains('-')) {
      if (expression.startsWith("-")) Substract(Seq(Number(0.0), parse(expression.drop(1))))
      else Substract(expression.split('-').map(parse))
    }
    else if (expression.contains('*')) Multiply(expression.split('*').map(parse))
    else if (expression.contains('/')) Divide(expression.split('/').map(parse))
    else Number(expression.toDoubleOption.getOrElse(Double.NaN))
    println(s"parse($expression) => $res")
    res
  }
}
