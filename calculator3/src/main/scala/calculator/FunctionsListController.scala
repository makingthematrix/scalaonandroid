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

object FunctionsListController:
  private lazy val loader = new FXMLLoader(classOf[FunctionsListController].getResource("history.fxml"))

  private lazy val dialog = new Dialog[String]().tap { d =>
    d.setTitle(new Label("Functions List"))
    d.setContent(loader.load)
    val cancelButton = new Button("Cancel").tap { c =>
      c.setCancelButton(true)
      c.setOnAction { (_: ActionEvent) => d.hide() }
    }
    d.getButtons.add(cancelButton)
  }

  def showDialog(history: Seq[String]): String =
    dialog
    loader.getController[FunctionsListController].fillFunctionsList(history)
    dialog.showAndWait().toScala.getOrElse("")

final class FunctionsListController:
  import FunctionsListController.dialog

  @FXML private var functionsList: ListView[String] = _

  def fillFunctionsList(history: Seq[String]): Unit =
    functionsList.getItems.setAll(history.asJavaCollection)

  def initialize(): Unit =
    functionsList.setOnMouseClicked { (_: MouseEvent) =>
      functionsList.getSelectionModel.getSelectedItems.asScala.headOption.foreach { expr =>
        dialog.setResult(expr.split("=").last.trim)
        dialog.hide()
      }
    }
