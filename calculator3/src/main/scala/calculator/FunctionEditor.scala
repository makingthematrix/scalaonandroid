package calculator

import com.gluonhq.charm.glisten.control.Dialog
import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.{Node, Scene}
import javafx.scene.control.{Button, Label}

import scala.jdk.CollectionConverters.*
import scala.jdk.OptionConverters.*
import scala.util.chaining.scalaUtilChainingOps

object FunctionEditor:
  private lazy val loader = new FXMLLoader(classOf[FunctionEditor].getResource("functioneditor.fxml"))

  private lazy val dialog = new Dialog[String]().tap { d =>
    d.setTitle(new Label("Function Editor"))
    d.setContent(loader.load)
    d.getContent.setStyle(classOf[FunctionEditor].getResource("styles.css").toExternalForm)
    val cancelButton = new Button("Cancel").tap { c =>
      c.setCancelButton(true)
      c.setOnAction { (_: ActionEvent) => d.hide() }
    }

    val okButton = new Button("OK").tap { c =>
      c.setDefaultButton(true)
      c.setOnAction { (_: ActionEvent) => d.hide() }
    }

    d.getButtons.add(okButton)
    d.getButtons.add(cancelButton)
  }

  def showDialog(): String =
    dialog
    dialog.showAndWait().toScala.getOrElse("")
final class FunctionEditor:
  import FunctionEditor.dialog

  def initialize(): Unit = {
    //dialog
  }