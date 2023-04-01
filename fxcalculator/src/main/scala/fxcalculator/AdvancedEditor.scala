package fxcalculator

import com.gluonhq.attach.util.Platform
import com.gluonhq.charm.glisten.control.{CharmListView, Dialog}
import fxcalculator.Resource.*
import fxcalculator.functions.{FunctionCell, FunctionEntry, Storage}
import fxcalculator.logic.{Dictionary, Evaluator, Parser}
import fxcalculator.logic.expressions.*
import io.github.makingthematrix.signals3.Stream
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
  private val isFullscreen: Boolean = true //!Platform.isDesktop

  def showDialog(parser: Parser): Option[Double] = loader.getController[AdvancedEditor].run(parser)

final class AdvancedEditor extends Initializable:
  import AdvancedEditor.*
  import io.github.makingthematrix.signals3.EventContext.Implicits.global
  import io.github.makingthematrix.signals3.ui.UiDispatchQueue.Ui

  @FXML private var textArea: TextArea = _
  @FXML private var functions: CharmListView[FunctionEntry, String] = _
  private var parser: Parser = _

  private val selectedEntry = Stream[FunctionEntry]()
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

  private val deletedEntry = Stream[FunctionEntry]()
  deletedEntry.foreach { entry =>
    println(s"delete: $entry")
    val usages = functionUsages(entry)
    println(s"usages: $usages")
    if usages.isEmpty then
      deleteEntry(entry)
    else
      val alert = new Alert(Alert.AlertType.WARNING)
      alert.setContentText(
        s"""
           | The function ${entry.declaration} which you are about to delete
           | is used by the following functions:
           | ${usages.map(_.name).mkString(", ")}
           | Do you want to continue?
           |""".stripMargin
      )
      alert.setResult(ButtonType.OK)
      alert.showAndWait().toScala.foreach(_ => deleteEntry(entry))
  }

  private lazy val dialog = new Dialog[Double](isFullscreen).tap { d =>
    d.setTitleText("Fx Calculator")
    d.setContent(root)
    d.getButtons.add(new Button("Run").tap { c =>
      c.setDefaultButton(true)
      c.setOnAction { (_: ActionEvent) => runScript(textArea.getText).foreach(close) }
    })
  }

  private def runScript(text: String): Option[Double] =
    Evaluator.evaluate(parser, text) match
      case ass: Assignment =>
        val alert = new Alert(Alert.AlertType.INFORMATION)
        alert.setContentText(s"You created a new assignment: ${ass.textForm}")
        Future { populateFunctionsList() }(Ui)
        alert.showAndWait()
        Storage.dump(parser.dictionary)
        None
      case result: Double =>
        Some(result)
      case error: Error =>
        val alert = new Alert(Alert.AlertType.ERROR)
        alert.setContentText(error.toString)
        alert.setResult(ButtonType.CLOSE)
        alert.showAndWait()
        None

  private def deleteEntry(entry: FunctionEntry): Unit =
    println(s"deleteEntry($entry)")
    parser.dictionary.delete(entry.name)
    Future { populateFunctionsList() }(Ui)
    Storage.dump(parser.dictionary)

  private def functionUsages(entry: FunctionEntry): Seq[FunctionAssignment] =
    val entryName = entry.name
    parser.dictionary.chronologicalList.collect {
      case f: FunctionAssignment if f.definition.contains(s"$entryName(") => f
    }
  
  private def close(result: Double): Unit =
    dialog.setResult(result)
    Storage.dump(parser.dictionary)
    dialog.hide()

  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit =
    textArea.setFocusTraversable(true)
    textArea.requestFocus()
    functions.setCellFactory((_: CharmListView[FunctionEntry, String]) => FunctionCell(selectedEntry.publish, deletedEntry.publish))

  private def populateFunctionsList(): Unit =
    val exprs = parser.dictionary.list
    val entries =
      exprs.collect { case expr: ConstantAssignment => FunctionEntry(expr) } ++
      exprs.collect { case expr: NativeFunction     => FunctionEntry(expr) } ++
      exprs.collect { case expr: FunctionAssignment => FunctionEntry(expr) }
    val filteredList = new FilteredList(
      FXCollections.observableArrayList(entries.asJavaCollection),
      (_: FunctionEntry) => true
    )
    functions.setItems(filteredList)

  def run(parser: Parser): Option[Double] =
    this.parser = parser
    populateFunctionsList()
    dialog.showAndWait().toScala
