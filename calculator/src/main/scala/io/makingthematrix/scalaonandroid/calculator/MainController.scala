package io.makingthematrix.scalaonandroid.calculator

import io.makingthematrix.scalaonandroid.calculator.eval.Eval
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import javafx.scene.input.MouseEvent

class MainController {

  @FXML private var expression: Label = _

  def onNumberOrOperator(event: MouseEvent): Unit = {
    val button = event.getSource.asInstanceOf[Button]
    val currentExpr = expression.getText

    convert(button.getId).foreach {
      case c if currentExpr == "0" || currentExpr == "NaN" => expression.setText(c)
      case c => expression.setText(s"$currentExpr$c")
    }
  }

  def onEvaluate(event: MouseEvent): Unit = {
    val expr = Eval.Expression(expression.getText)
    val res = expr.evaluate
    expression.setText(if (res.toInt == res) res.toInt.toString else res.toString)
  }

  def onClear(event: MouseEvent): Unit = expression.setText("0")

  def initialize(): Unit = {
    expression.setText("0")
  }

  private def convert(buttonId: String) = buttonId match {
    case "b1" => Some("1")
    case "b2" => Some("2")
    case "b3" => Some("3")
    case "b4" => Some("4")
    case "b5" => Some("5")
    case "b6" => Some("6")
    case "b7" => Some("7")
    case "b8" => Some("8")
    case "b9" => Some("9")
    case "b0" => Some("0")
    case "bComma" => Some(".")
    case "bAdd"       => Some("+")
    case "bSubstract" => Some("-")
    case "bMultiply"  => Some("*")
    case "bDivide"    => Some("/")
    case _ => None
  }
}
