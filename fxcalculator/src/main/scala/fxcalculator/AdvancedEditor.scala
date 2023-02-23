package fxcalculator

import com.gluonhq.charm.glisten.control.Dialog
import javafx.event.{ActionEvent, EventHandler}
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{Button, Label, TextArea}
import javafx.scene.input.{KeyCode, KeyEvent}

import scala.jdk.CollectionConverters.*
import scala.jdk.OptionConverters.*
import scala.util.chaining.scalaUtilChainingOps
import fxcalculator.logic.Dictionary
import fxcalculator.logic.expressions.{Assignment, Constant, FunctionAssignment, NativeFunction}
import javafx.scene.Node

object AdvancedEditor:
  private val loader = new FXMLLoader(classOf[AdvancedEditor].getResource("advancededitor.fxml"))
  private val root: Node = loader.load[Node]()

  def showDialog(dictionary: Dictionary): String =
    loader.getController[AdvancedEditor].run(dictionary)

final class AdvancedEditor:
  import AdvancedEditor.root

  @FXML private var textArea: TextArea = _

  private var dictionary: Option[Dictionary] = None

  private lazy val dialog = new Dialog[String]().tap { d =>
    d.setTitle(new Label("Advanced Editor"))
    d.setContent(root)
    d.getButtons.add(new Button("OK").tap { c =>
      c.setDefaultButton(true)
      c.setOnAction { (_: ActionEvent) => close(textArea.getText) }
    })

    d.getButtons.add(new Button("Cancel").tap { c =>
      c.setCancelButton(true)
      c.setOnAction { (_: ActionEvent) => close("") }
    })

    d.getButtons.add(new Button("Functions").tap { c =>
      c.setOnAction { (_: ActionEvent) =>
        val expressions = dictionary.map {
          _.expressions.collect {
            case (_, expr: Assignment)         => expr.textForm
            case (_, expr: FunctionAssignment) => expr.textForm
            case (_, expr: NativeFunction)     => expr.textForm
          }.toSeq
        }.getOrElse(Nil).sorted
        val result = DictionaryDialog.showDialog(expressions).split("=")(0).trim
        if result.nonEmpty then
          val text = textArea.getText
          val caret = textArea.getCaretPosition
          textArea.setText(text.substring(0, caret) + result + text.substring(caret))
      }
    })
  }

  private def close(text: String): Unit =
    dialog.setResult(text)
    dialog.hide()

  def initialize(): Unit =
    textArea.setOnKeyReleased { (key: KeyEvent) =>
      if key.getCode == KeyCode.ENTER then close(textArea.getText)
    }

  def run(dictionary: Dictionary): String =
    this.dictionary = Some(dictionary)
    dialog.showAndWait().toScala.getOrElse("")