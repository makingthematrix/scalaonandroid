package calculator

import calculator.eval.Eval
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, OverrunStyle}

object MainController {
  private val operators = Set('+', '-', '*', '/')

  private val idsToSigns = Map(
    "b1" -> "1", "b2" -> "2", "b3" -> "3", "b4" -> "4", "b5" -> "5", "b6" -> "6", "b7" -> "7", "b8" -> "8", "b9" -> "9", "b0" -> "0",
    "bPoint" -> ".", "bAdd" -> "+", "bSubstract" -> "-", "bMultiply" -> "*", "bDivide" -> "/"
  )
}

final class MainController {
  import MainController._

  @FXML private var expression: Label = _

  private var history = Seq.empty[String]

  def initialize(): Unit = {
    expression.setText("0")
    expression.setTextOverrun(OverrunStyle.CLIP)
  }

  def onEvaluate(event: ActionEvent): Unit = {
    val text = expression.getText
    val res = Eval.Expression(text).evaluate
    val resStr = if (res.toInt == res) res.toInt.toString else res.toString
    history :+= s"$text = $resStr"
    expression.setText(resStr)
  }

  def onHistory(event: ActionEvent): Unit = {
    val result = HistoryController.showHistoryDialog(history)
    if (result.nonEmpty) expression.setText(s"${expression.getText}$result")
  }

  def onClear(event: ActionEvent): Unit = expression.setText("0")

  def onNumberOrOperator(event: ActionEvent): Unit = {
    val currentExpr = expression.getText
    val button = event.getSource.asInstanceOf[Button]
    idsToSigns.get(button.getId).foreach {
      case "." if !pointAllowed(currentExpr) =>
      case c if currentExpr == "0" || currentExpr == "NaN" => expression.setText(c)
      case c => expression.setText(s"$currentExpr$c")
    }
  }

  private def pointAllowed(currentExpr: String): Boolean = {
    val commaIndex = currentExpr.lastIndexOf('.')
    if (commaIndex > -1) operators.map(currentExpr.lastIndexOf(_)).max > commaIndex
    else true
  }
}
