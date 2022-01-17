package calculator

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, OverrunStyle}

import scala.math.round

import calculator.eval.*
import calculator.eval.expressions.*

object MainController:
  private val idsToSigns = Map(
    "b1" -> '1', "b2" -> '2', "b3" -> '3', "b4" -> '4', "b5" -> '5', "b6" -> '6', "b7" -> '7', "b8" -> '8', "b9" -> '9', "b0" -> '0',
    "bPoint" -> '.', "bAdd" -> '+', "bSubstract" -> '-', "bMultiply" -> '*', "bDivide" -> '/'
  )

  private val numbers = Set('1', '2', '3', '4', '5', '6', '7', '8', '9', '0')

final class MainController:
  import MainController.{idsToSigns, numbers}

  private val parser = Parser()

  @FXML private var expression: Label = _

  private var history = Seq.empty[String]

  def initialize(): Unit =
    expression.setText("0")
    expression.setTextOverrun(OverrunStyle.CLIP)

  def onEvaluate(event: ActionEvent): Unit =
    val text = expression.getText
    val res = run(text).getOrElse("")
    if res.isEmpty then
      expression.setText("")
    else
      history :+= s"$text = $res"
      expression.setText(res)

  def onHistory(event: ActionEvent): Unit =
    val result = HistoryController.showHistoryDialog(history)
    if (result.nonEmpty) then expression.setText(s"${expression.getText}$result")

  def onClear(event: ActionEvent): Unit = expression.setText("0")

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
    if (isPointAllowed) then updateExpression('.')

  private def updateExpression(newSign: Char): Unit = expression.getText match
    case currentExpr if numbers.contains(newSign) && (currentExpr == "0" || currentExpr == Double.NaN.toString) =>
      expression.setText(newSign.toString)
    case currentExpr =>
      expression.setText(s"$currentExpr$newSign")

  private def isPointAllowed: Boolean =
    val currentExpr = expression.getText
    val commaIndex = currentExpr.lastIndexOf('.')
    if (commaIndex > -1) then
      Parser.operators.map(currentExpr.lastIndexOf(_)).max > commaIndex
    else
      currentExpr.lastOption.forall(numbers.contains)

  private def isOperatorAllowed(operator:Char): Boolean = (expression.getText, operator) match
    case (".", _)                                               => false
    case (currentExpr, _) if currentExpr == Double.NaN.toString => false
    case (currentExpr, '-')                                     => !currentExpr.lastOption.contains('-')
    case ("0", _)                                               => false
    case (currentExpr, _)                                       => !currentExpr.lastOption.forall(Parser.operators.contains)

  private def run(line: String): Option[String] =
    parser
      .parse(line)
      .map {
        case Right(expr) => replForm(parser.dictionary, expr)
        case Left(error) => error.toString
      }

  private def replForm(dictionary: Dictionary, expression: Expression): String =
    expression match
      case FunctionAssignment(name, args, _) =>
        s"$name(${args.mkString(", ")}) -> Function"
      case Assignment(name, Constant(number)) =>
        s"$name -> $number"
      case expr =>
        expr.run(dictionary) match
          case Right(result) => result.toString
          case Left(error)   => error.toString