package calculator

import com.gluonhq.charm.glisten.control.Dialog
import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{Button, Label, ListView}
import javafx.scene.input.MouseEvent

import scala.jdk.CollectionConverters._
import scala.jdk.OptionConverters._
import scala.util.chaining.scalaUtilChainingOps

object HistoryController:
  private lazy val loader = new FXMLLoader(classOf[HistoryController].getResource("history.fxml"))

  private lazy val dialog = new Dialog[String]().tap { d =>
    d.setTitle(new Label("History Dialog"))
    d.setContent(loader.load)
    val cancelButton = new Button("Cancel").tap { c =>
      c.setCancelButton(true)
      c.setOnAction { (_: ActionEvent) => d.hide() }
    }
    d.getButtons.add(cancelButton)
  }

  def showHistoryDialog(history: Seq[String]): String =
    dialog
    loader.getController[HistoryController].fillHistoryList(history)
    dialog.showAndWait().toScala.getOrElse("")

final class HistoryController:
  import HistoryController.dialog

  @FXML private var historyList: ListView[String] = _

  def fillHistoryList(history: Seq[String]): Unit =
    historyList.getItems.setAll(history.asJavaCollection)

  def initialize(): Unit =
    historyList.setOnMouseClicked { (_: MouseEvent) =>
      historyList.getSelectionModel.getSelectedItems.asScala.headOption.foreach { expr =>
        dialog.setResult(expr.split("=").last.trim)
        dialog.hide()
      }
    }
