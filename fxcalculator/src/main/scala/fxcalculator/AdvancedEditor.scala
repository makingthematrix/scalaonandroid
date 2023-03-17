package fxcalculator

import com.gluonhq.attach.util.Platform
import com.gluonhq.charm.glisten.control.{CharmListView, Dialog}
import fxcalculator.Resource.*
import fxcalculator.functions.{FunctionCell, FunctionEntry}
import fxcalculator.logic.Dictionary
import fxcalculator.logic.expressions.*
import io.github.makingthematrix.signals3.EventStream
import io.github.makingthematrix.signals3.ui.UiDispatchQueue.*
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.collections.FXCollections
import javafx.collections.transformation.FilteredList
import javafx.event.{ActionEvent, EventHandler, EventType}
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.Node
import javafx.scene.control.{Alert, Button, ButtonType, Label, ListView, TextArea}
import javafx.scene.input.{KeyCode, KeyEvent, MouseEvent}

import java.net.URL
import java.util.ResourceBundle
import scala.concurrent.Future
import scala.jdk.CollectionConverters.*
import scala.jdk.FunctionConverters.*
import scala.jdk.OptionConverters.*
import scala.util.chaining.scalaUtilChainingOps

object AdvancedEditor:
  private val loader = new FXMLLoader(url(AdvancedEditorFxml))
  private val root: Node = loader.load[Node]()
  private val isFullscreen: Boolean = !Platform.isDesktop

  def showDialog(dictionary: Dictionary): String = loader.getController[AdvancedEditor].run(dictionary)

final class AdvancedEditor extends Initializable:
  import AdvancedEditor.*
  import io.github.makingthematrix.signals3.EventContext.Implicits.global
  import io.github.makingthematrix.signals3.ui.UiDispatchQueue.Ui

  @FXML private var textArea: TextArea = _
  @FXML private var functions: CharmListView[FunctionEntry, String] = _
  private var dictionary: Dictionary = _

  private val selectedEntry = EventStream[FunctionEntry]()
  selectedEntry.onUi { entry =>
    val text = textArea.getText
    val selection = textArea.getSelection
    textArea.setText(text.substring(0, selection.getStart) + entry.declaration + text.substring(selection.getEnd))
    if entry.declaration.contains("(") then
      textArea.selectRange(selection.getStart + entry.declaration.indexOf('(') + 1, selection.getStart + entry.declaration.length - 1)
    else
      textArea.selectPositionCaret(selection.getStart + entry.declaration.length)
    textArea.requestFocus()
  }

  private val deletedEntry = EventStream[FunctionEntry]()
  deletedEntry.foreach { entry =>
    val usages = functionUsages(entry)
    if usages.isEmpty then deleteEntry(entry)
    else
      val alert = new Alert(Alert.AlertType.CONFIRMATION)
      alert.setContentText(
        s"""
           | The function ${entry.declaration} which you are about to delete
           | is used by the following functions:
           | ${usages.map(_.name).mkString(", ")}
           | Do you want to continue?
           |""".stripMargin
      )
      alert.setResult(ButtonType.YES)
      alert.showAndWait().toScala.foreach(_ => deleteEntry(entry))
  }

  private lazy val dialog = new Dialog[String](isFullscreen).tap { d =>
    d.setTitleText("Fx Calculator")
    d.setContent(root)

    d.getButtons.add(new Button("Run").tap { c =>
      c.setDefaultButton(true)
      c.setOnAction { (_: ActionEvent) => close(textArea.getText) }
    })
  }

  private def deleteEntry(entry: FunctionEntry): Unit =
    dictionary.delete(entry.name)
    Future { populateFunctionsList() }(Ui)

  private def functionUsages(entry: FunctionEntry): Seq[FunctionAssignment] =
    val entryName = entry.name
    dictionary.list.collect {
      case f: FunctionAssignment if f.textForm.contains(s"$entryName(") => f
    }
  
  private def close(text: String): Unit =
    dialog.setResult(text)
    dialog.hide()

  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit =
    textArea.setFocusTraversable(true)
    textArea.requestFocus()
    functions.setCellFactory((_: CharmListView[FunctionEntry, String]) => FunctionCell(selectedEntry.publish, deletedEntry.publish))

  private def populateFunctionsList(): Unit =
    val exprs = dictionary.list
    val entries =
      exprs.collect { case expr: ConstantAssignment => FunctionEntry(expr) } ++
      exprs.collect { case expr: NativeFunction     => FunctionEntry(expr) } ++
      exprs.collect { case expr: FunctionAssignment => FunctionEntry(expr) }
    val filteredList = new FilteredList(
      FXCollections.observableArrayList(entries.asJavaCollection),
      (_: FunctionEntry) => true
    )
    functions.setItems(filteredList)

  def run(dictionary: Dictionary): String =
    this.dictionary = dictionary
    populateFunctionsList()
    dialog.showAndWait().toScala.getOrElse("")
