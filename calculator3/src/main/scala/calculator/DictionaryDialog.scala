package calculator

import com.gluonhq.charm.glisten.control.Dialog
import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{Button, Label, ListView}
import javafx.scene.input.MouseEvent

import scala.collection.mutable.ArrayBuffer
import scala.jdk.CollectionConverters.*
import scala.jdk.OptionConverters.*
import scala.util.chaining.scalaUtilChainingOps

object DictionaryDialog:
  private lazy val loader = new FXMLLoader(classOf[DictionaryDialog].getResource("dictionarydialog.fxml"))

  private lazy val dialog = new Dialog[String]().tap { d =>
    d.setTitle(new Label("Functions List"))
    d.setContent(loader.load)
    val cancelButton = new Button("Cancel").tap { c =>
      c.setCancelButton(true)
      c.setOnAction { (_: ActionEvent) => d.hide() }
    }
    d.getButtons.add(cancelButton)
  }

  def showDialog(functionsList: Seq[String]): String =
    dialog
    loader.getController[DictionaryDialog].fillFunctionsList(functionsList)
    dialog.showAndWait().toScala.getOrElse("")

final class DictionaryDialog:
  import DictionaryDialog.dialog

  @FXML private var functionsList: ListView[String] = _

  def fillFunctionsList(list: Seq[String]): Unit =
    functionsList.getItems.setAll(list.asJavaCollection)

  def initialize(): Unit =
    functionsList.setOnMouseClicked { (_: MouseEvent) =>
      functionsList.getSelectionModel.getSelectedItems.asScala.headOption.foreach { expr =>
        dialog.setResult(expr.split("=").last.trim)
        dialog.hide()
      }
    }
