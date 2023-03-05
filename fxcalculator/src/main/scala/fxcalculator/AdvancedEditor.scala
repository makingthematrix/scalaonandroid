package fxcalculator

import com.gluonhq.charm.glisten.control.{CharmListView, Dialog}
import javafx.event.{ActionEvent, EventHandler, EventType}
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{Button, Label, TextArea}
import javafx.scene.input.{KeyCode, KeyEvent, MouseEvent}

import scala.jdk.CollectionConverters.*
import scala.jdk.OptionConverters.*
import scala.jdk.FunctionConverters.*
import scala.util.chaining.scalaUtilChainingOps
import fxcalculator.logic.Dictionary
import fxcalculator.logic.expressions.{Assignment, Constant, FunctionAssignment, NativeFunction}
import fxcalculator.functions.{FunctionCell, FunctionEntry}
import io.github.makingthematrix.signals3.Signal
import io.github.makingthematrix.signals3.ui.UiDispatchQueue.*
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.collections.FXCollections
import javafx.collections.transformation.FilteredList
import javafx.scene.Node

object AdvancedEditor:
  private val loader = new FXMLLoader(classOf[AdvancedEditor].getResource("advancededitor.fxml"))
  private val root: Node = loader.load[Node]()

  def showDialog(dictionary: Dictionary): String = loader.getController[AdvancedEditor].run(dictionary)

final class AdvancedEditor:
  import AdvancedEditor.root
  import io.github.makingthematrix.signals3.EventContext.Implicits.global

  @FXML private var textArea: TextArea = _
  @FXML private var functions: CharmListView[FunctionEntry, String] = _

  private val selectedEntry = Signal[FunctionEntry]()
  selectedEntry.onUi { entry =>
    val text = textArea.getText
    val caret = textArea.getCaretPosition
    textArea.setText(text.substring(0, caret) + entry.declaration + text.substring(caret))
  }

  private var dictionary: Dictionary = _

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
  }

  private def close(text: String): Unit =
    dialog.setResult(text)
    dialog.hide()

  def initialize(): Unit =
    textArea.setFocusTraversable(true)
    textArea.setOnKeyReleased { (key: KeyEvent) =>
      if key.getCode == KeyCode.ENTER then
        val text = textArea.getText.replaceAll("\n", "")
        textArea.setText(text)
        close(text)
    }
    functions.selectedItemProperty().addListener(new ChangeListener[FunctionEntry] {
      override def changed(observableValue: ObservableValue[_ <: FunctionEntry], t: FunctionEntry, t1: FunctionEntry): Unit =
          selectedEntry ! observableValue.getValue
    })

  private def populateFunctionsList(dictionary: Dictionary): Unit =
    val entries: Seq[FunctionEntry] =
      dictionary.expressions.collect {
        case (_, expr: Assignment)         => FunctionEntry(expr)
        case (_, expr: FunctionAssignment) => FunctionEntry(expr)
        case (_, expr: NativeFunction)     => FunctionEntry(expr)
      }.toSeq
    val filteredList = new FilteredList(
      FXCollections.observableArrayList(entries.asJavaCollection),
      (_: FunctionEntry) => true
    )
    functions.setCellFactory((_: CharmListView[FunctionEntry, String]) => FunctionCell())
    functions.setItems(filteredList)

  def run(dictionary: Dictionary): String =
    this.dictionary = dictionary
    populateFunctionsList(dictionary)
    dialog.showAndWait().toScala.getOrElse("")