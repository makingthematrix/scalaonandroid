package fxcalculator.ui

import com.gluonhq.attach.util.{Platform, Services}
import com.gluonhq.charm.glisten.control.{CharmListView, Dialog}
import fxcalculator.logic.expressions.*
import fxcalculator.logic.{AssignmentEntry, Dictionary, Evaluator, Parser}
import fxcalculator.utils.Logger.*
import fxcalculator.utils.Resource.*
import fxcalculator.utils.Storage
import fxcalculator.ui.{ AssignmentCell, InfoBox}
import io.github.makingthematrix.signals3.Stream
import io.github.makingthematrix.signals3.ui.UiDispatchQueue.*
import javafx.beans.value.{ChangeListener, ObservableValue}
import javafx.collections.FXCollections
import javafx.collections.transformation.FilteredList
import javafx.event.{ActionEvent, EventHandler, EventType}
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.input.{InputMethodEvent, KeyCode, KeyEvent, MouseEvent}
import javafx.stage.Popup

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

  def showDialog(parser: Parser): Option[String] = loader.getController[AdvancedEditor].run(parser)

final class AdvancedEditor extends Initializable:
  import AdvancedEditor.*
  import io.github.makingthematrix.signals3.EventContext.Implicits.global
  import io.github.makingthematrix.signals3.ui.UiDispatchQueue.Ui

  @FXML private var textArea: TextArea = _
  @FXML private var assignments: CharmListView[AssignmentEntry, String] = _
  @FXML private var removeAll: Button = _
  private var parser: Parser = _

  private val selectedEntry = Stream[AssignmentEntry]()
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

  private val deletedEntry = Stream[AssignmentEntry]()
  deletedEntry.foreach { entry =>
    val usages = functionUsages(entry)
    if usages.isEmpty then
      deleteEntry(entry)
    else
      val alert = new Alert(Alert.AlertType.WARNING)
      alert.setContentText(
        s"""
           | The function ${entry.declaration} which you are about to delete
           | is used by the following assignments:
           | ${usages.map(_.name).mkString(", ")}
           | Do you want to continue?
           |""".stripMargin
      )
      alert.setResult(ButtonType.OK)
      alert.showAndWait().toScala.foreach(_ => deleteEntry(entry))
  }

  private lazy val dialog: Dialog[String] = new Dialog[String](isFullscreen).tap { d =>
    d.setTitleText("Fx Calculator")
    d.setContent(root)
    d.getButtons.add(new Button("Run").tap { c =>
      c.setDefaultButton(true)
      c.setOnAction { (_: ActionEvent) =>
        runScript(textArea.getText) match
          case Some(result) => close(result)
          case None => textArea.requestFocus()
      }
    })
    if !isFullscreen then
      d.getButtons.add(new Button("Cancel").tap { c =>
        c.setCancelButton(true)
        c.setOnAction { (_: ActionEvent) => dialog.hide() }
      })
  }

  private def runScript(text: String): Option[String] =
    val evInfo = Evaluator.evaluate(parser, text)
    info(s"runScript done: $text")
    if evInfo.assignments.nonEmpty then
      parser.store(evInfo.assignments.map(_._2))
      Future { populateList() }(Ui)
    evInfo.result match
      case _: Double =>
        Some(evInfo.resultAsString)
      case _: Assignment =>
        InfoBox.show(s"You created a new assignment: ${evInfo.assignments.last._2}")
        None
      case error: Error =>
        InfoBox.show(error.toString)
        None

  private def deleteEntry(entry: AssignmentEntry): Unit =
    if parser.delete(entry.name) then Future { populateList() }(Ui)

  private def functionUsages(entry: AssignmentEntry): Seq[FunctionAssignment] =
    val entryName = entry.name
    parser.dictionary.list.collect {
      case f: FunctionAssignment if f.definition.contains(s"$entryName(") => f
    }
  
  private def close(result: String): Unit =
    dialog.setResult(result)
    dialog.hide()

  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit =
    textArea.setFocusTraversable(true)
    textArea.requestFocus()
    assignments.setCellFactory((_: CharmListView[AssignmentEntry, String]) => AssignmentCell(selectedEntry.publish, deletedEntry.publish))
    removeAll.setOnAction { (_: ActionEvent) => removeAllCustomEntries() }

  private def removeAllCustomEntries(): Unit =
    parser.reset()
    Future { populateList() }(Ui)
    Storage.reset()

  private def populateList(): Unit =
    val filteredList = new FilteredList(
      FXCollections.observableArrayList(parser.assignments.asJavaCollection),
      (_: AssignmentEntry) => true
    )
    assignments.setItems(filteredList)

  def run(parser: Parser): Option[String] =
    this.parser = parser
    populateList()
    dialog.showAndWait().toScala
