package calculator

import calculator.eval.Eval
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, OverrunStyle}
import scala.math.round

object MainController {
  private val idsToSigns = Map(
    "b1" -> '1', "b2" -> '2', "b3" -> '3', "b4" -> '4', "b5" -> '5', "b6" -> '6', "b7" -> '7', "b8" -> '8', "b9" -> '9', "b0" -> '0',
    "bPoint" -> '.', "bAdd" -> '+', "bSubstract" -> '-', "bMultiply" -> '*', "bDivide" -> '/'
  )
}

final class MainController {
  import MainController.idsToSigns
  import Eval.{operators, numbers}

  @FXML private var expression: Label = _

  private var history = Seq.empty[String]

  def initialize(): Unit = {
    expression.setText("0")
    expression.setTextOverrun(OverrunStyle.CLIP)
  }

  def onEvaluate(event: ActionEvent): Unit = {
    val text = expression.getText
    val res = Eval(text).evaluate
    if (res.isNaN) expression.setText(Double.NaN.toString)
    else {
      val resStr = if (res.toInt == res) res.toInt.toString else (round(res * 10000.0) / 10000.0).toString
      history :+= s"$text = $resStr"
      expression.setText(resStr)
    }
  }

  def onHistory(event: ActionEvent): Unit = {
    val result = HistoryController.showHistoryDialog(history)
    if (result.nonEmpty) expression.setText(s"${expression.getText}$result")
  }

  def onClear(event: ActionEvent): Unit = expression.setText("0")

  def onBackspace(event: ActionEvent): Unit = expression.getText match {
    case ""   =>
    case text => expression.setText(text.init)
  }

  def onOperator(event: ActionEvent): Unit =
    idsToSigns.get(event.getSource.asInstanceOf[Button].getId) match {
      case Some(op) if isOperatorAllowed(op) => updateExpression(op)
      case _ =>
    }

  def onNumber(event: ActionEvent): Unit =
    idsToSigns.get(event.getSource.asInstanceOf[Button].getId).foreach(updateExpression)

  def onPoint(event: ActionEvent): Unit =
    if (isPointAllowed) updateExpression('.')

  private def updateExpression(newSign: Char): Unit = expression.getText match {
    case currentExpr if numbers.contains(newSign) && (currentExpr == "0" || currentExpr == Double.NaN.toString) =>
      expression.setText(newSign.toString)
    case currentExpr =>
      expression.setText(s"$currentExpr$newSign")
  }

  private def isPointAllowed: Boolean = {
    val currentExpr = expression.getText
    val commaIndex = currentExpr.lastIndexOf('.')
    if (commaIndex > -1)
      operators.map(currentExpr.lastIndexOf(_)).max > commaIndex
    else
      currentExpr.lastOption.forall(numbers.contains)
  }

  private def isOperatorAllowed(operator:Char): Boolean = (expression.getText, operator) match {
    case (".", _)                                               => false
    case (currentExpr, _) if currentExpr == Double.NaN.toString => false
    case (currentExpr, '-')                                     => !currentExpr.lastOption.contains('-')
    case ("0", _)                                               => false
    case (currentExpr, _)                                       => !currentExpr.lastOption.forall(operators.contains)
  }
}
