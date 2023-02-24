package fxcalculator

import com.gluonhq.charm.glisten.control.Dialog
import fxcalculator.AdvancedEditor.loader
import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Node
import javafx.scene.control.{Button, Label, ListView}
import javafx.scene.input.MouseEvent

import scala.collection.mutable.ArrayBuffer
import scala.jdk.CollectionConverters.*
import scala.jdk.OptionConverters.*
import scala.util.chaining.scalaUtilChainingOps

object DictionaryDialog:
  private val loader = new FXMLLoader(classOf[DictionaryDialog].getResource("dictionarydialog.fxml"))
  private val root: Node = loader.load[Node]()

  def showDialog(functionsList: Seq[String]): String =
    loader.getController[DictionaryDialog].run(functionsList)

final class DictionaryDialog:
  import DictionaryDialog.root

  @FXML private var functionsList: ListView[String] = _

  private lazy val dialog = new Dialog[String]().tap { d =>
    d.setTitle(new Label("Functions List"))
    d.setContent(root)
    val cancelButton = new Button("Cancel").tap { c =>
      c.setCancelButton(true)
      c.setOnAction { (_: ActionEvent) => d.hide() }
    }
    d.getButtons.add(cancelButton)
  }

  def initialize(): Unit =
    functionsList.setOnMouseClicked { (_: MouseEvent) =>
      functionsList.getSelectionModel.getSelectedItems.asScala.headOption.foreach { expr =>
        dialog.setResult(expr.split("=").head.trim)
        dialog.hide()
      }
    }

  def run(list: Seq[String]): String =
    functionsList.getItems.setAll(list.asJavaCollection)
    dialog.showAndWait().toScala.getOrElse("")