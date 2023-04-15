package fxcalculator

import fxcalculator.logic.{Dictionary, Evaluator, Parser, Preprocessor}
import fxcalculator.logic.expressions.{Assignment, Constant, ConstantAssignment, Error, Expression, FunctionAssignment, NativeFunction}
import fxcalculator.functions.Storage
import javafx.event.ActionEvent
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Alert, Button, Label, OverrunStyle}

import java.net.URL
import java.util.ResourceBundle
import scala.util.chaining.scalaUtilChainingOps

object MainController:
  private val idsToSigns = Map(
    "b1" -> '1', "b2" -> '2', "b3" -> '3', "b4" -> '4', "b5" -> '5', "b6" -> '6', "b7" -> '7', "b8" -> '8', "b9" -> '9', "b0" -> '0',
    "bPoint" -> '.', "bAdd" -> '+', "bSubstract" -> '-', "bMultiply" -> '*', "bDivide" -> '/',
    "lParens" -> '(', "rParens" -> ')', "bPower" -> '^'
  )
  private val operators = Set('+', '-', '*', '/', '^', '(', ')')
  private val numbers = Set('1', '2', '3', '4', '5', '6', '7', '8', '9', '0')

final class MainController extends Initializable:
  import MainController.*

  @FXML private var expression: Label = _

  private var clearExpression = true
  private var memory: Option[String] = None

  private lazy val parser = ParserCreator.createParser(withNativeFunctions = true, withConstants = true, withStorage = true)

  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit =
    expression.setText("0.0")
    expression.setTextOverrun(OverrunStyle.CLIP)

  def onEvaluate(event: ActionEvent): Unit =
    evaluate(expression.getText) match
      case Right(result) =>
        expression.setText(result)
        clearExpression = true
      case Left(error) =>
        showError(error)

  def onMemoryPlus(event: ActionEvent): Unit =
    evaluate(expression.getText) match
      case Right(result) => memory = Some(result)
      case Left(error) => 
        expression.setText(error)
        clearExpression = true

  def onMemoryReveal(event: ActionEvent): Unit = memory.foreach(updateExpression)

  def onFx(event: ActionEvent): Unit =
    AdvancedEditor.showDialog(parser).foreach(result => updateExpression(Evaluator.round(result)))
  
  def onClear(event: ActionEvent): Unit =
    expression.setText("0.0")

  def onBackspace(event: ActionEvent): Unit = expression.getText match
    case ""   =>
    case text => expression.setText(text.init)

  def onOperator(event: ActionEvent): Unit =
    idsToSigns.get(event.getSource.asInstanceOf[Button].getId) match
      case Some(op) if isOperatorAllowed(op) => updateExpression(op)
      case _ =>

  def onNumber(event: ActionEvent): Unit =
    idsToSigns.get(event.getSource.asInstanceOf[Button].getId).foreach(updateExpression(_))

  def onPoint(event: ActionEvent): Unit =
    if isPointAllowed then updateExpression('.')

  private def showError(error: String): Unit =
    val alert = new Alert(Alert.AlertType.ERROR)
    alert.setContentText(error)
    alert.showAndWait()
  
  private def evaluate(text: String): Either[String, String] =
    Evaluator.evaluate(parser, text) match
      case res: Double     => Right(Evaluator.round(res))
      case err: Error      => Left(err.toString)
      case ass: Assignment => Right(ass.textForm)
  
  inline private def updateExpression(newSign: Char): Unit = updateExpression(newSign.toString)

  private def updateExpression(newStr: String): Unit = expression.getText match
    case currentExpr if newStr.length == 1 && (operators.contains(newStr.head) || newStr.head == '.') =>
      expression.setText(s"$currentExpr$newStr")
      clearExpression = false
    case currentExpr if currentExpr.isEmpty || clearExpression || currentExpr == "0.0" =>
      expression.setText(newStr)
      clearExpression = false
    case currentExpr if operators.contains(currentExpr.last) || (newStr.length == 1 && numbers.contains(newStr.head)) =>
      expression.setText(s"$currentExpr$newStr")
    case currentExpr if numbers.contains(currentExpr.last) && (newStr.length == 1 && (operators.contains(newStr.head) || newStr.head == '.')) =>
      expression.setText(s"$currentExpr$newStr")
    case _ =>
      expression.setText(newStr)

  private def isPointAllowed: Boolean =
    val currentExpr = expression.getText
    val commaIndex = currentExpr.lastIndexOf('.')
    if commaIndex > -1 then
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
