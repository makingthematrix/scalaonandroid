package calculator

import calculator.replcalc.{Parser, run}
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, OverrunStyle}

import scala.math.round
import scala.util.chaining.scalaUtilChainingOps

object MainController:
  private val idsToSigns = Map(
    "b1" -> '1', "b2" -> '2', "b3" -> '3', "b4" -> '4', "b5" -> '5', "b6" -> '6', "b7" -> '7', "b8" -> '8', "b9" -> '9', "b0" -> '0',
    "bPoint" -> '.', "bAdd" -> '+', "bSubstract" -> '-', "bMultiply" -> '*', "bDivide" -> '/',
    "lParens" -> '(', "rParens" -> ')', "bPower" -> '^'
  )
  private val operators = Set('+', '-', '*', '/', '^', '(', ')')
  private val numbers = Set('1', '2', '3', '4', '5', '6', '7', '8', '9', '0')

  def createParser(): Parser =
    Parser().tap { parser =>
      Storage.readFunctions.foreach(parser.parse)
    }

final class MainController:
  import MainController.*

  @FXML private var expression: Label = _

  private var history = Seq.empty[String]
  private var clearExpression = true

  private val parser = createParser()

  def initialize(): Unit =
    expression.setText("0")
    expression.setTextOverrun(OverrunStyle.CLIP)

  def onEvaluate(event: ActionEvent): Unit =
    val text = expression.getText
    run(parser, text) match
      case Right(result) =>
        history :+= s"$text = $result"
        expression.setText(result)
      case Left(error) =>
        expression.setText(error)
        clearExpression = true

  def onHistory(event: ActionEvent): Unit =
    val result = FunctionsListController.showDialog(history)
    if result.nonEmpty then expression.setText(s"${expression.getText}$result")


  def onFx(event: ActionEvent): Unit = {
    val result = FunctionEditor.showDialog()
    if result.nonEmpty then expression.setText(result)
  }
  
  def onClear(event: ActionEvent): Unit =
    expression.setText("0")

  def onBackspace(event: ActionEvent): Unit = expression.getText match
    case ""   =>
    case text => expression.setText(text.init)

  def onOperator(event: ActionEvent): Unit =
    idsToSigns.get(event.getSource.asInstanceOf[Button].getId) match
      case Some(op) if isOperatorAllowed(op) => updateExpression(op)
      case _ =>

  def onNumber(event: ActionEvent): Unit =
    idsToSigns.get(event.getSource.asInstanceOf[Button].getId).foreach(updateExpression)

  def onPoint(event: ActionEvent): Unit =
    if isPointAllowed then updateExpression('.')

  private def updateExpression(newSign: Char): Unit =
    expression.getText match
      case currentExpr if clearExpression || currentExpr == "0" =>
        expression.setText(newSign.toString)
        clearExpression = false
      case currentExpr =>
        expression.setText(s"$currentExpr$newSign")

  private def isPointAllowed: Boolean =
    val currentExpr = expression.getText
    val commaIndex = currentExpr.lastIndexOf('.')
    if (commaIndex > -1) then
      operators.map(currentExpr.lastIndexOf(_)).max > commaIndex
    else
      currentExpr.lastOption.forall(numbers.contains)

  private def isOperatorAllowed(operator: Char): Boolean = (expression.getText, operator) match
    case (".", _)                                               => false
    case (currentExpr, _) if currentExpr == Double.NaN.toString => false
    case (currentExpr, '-')                                     => !currentExpr.lastOption.contains('-')
    case ("0", _)                                               => false
    case (currentExpr, '(')                                     => currentExpr.lastOption.forall(operators.contains)
    case (currentExpr, _)                                       => !currentExpr.lastOption.forall(operators.contains)
