package calculator

import com.gluonhq.charm.glisten.control.Dialog
import javafx.event.{ActionEvent, EventHandler}
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Node, Scene}
import javafx.scene.control.{Button, Label, TextArea}
import javafx.scene.input.{KeyCode, KeyEvent}

import scala.jdk.CollectionConverters.*
import scala.jdk.OptionConverters.*
import scala.util.chaining.scalaUtilChainingOps

object AdvancedEditor:
  private lazy val loader = new FXMLLoader(classOf[AdvancedEditor].getResource("advancededitor.fxml"))

  private lazy val dialog = new Dialog[String]().tap { d =>
    d.setTitle(new Label("Advanced Editor"))
    d.setContent(loader.load)
    d.getButtons.add(new Button("OK").tap { c =>
      c.setDefaultButton(true)
      c.setOnAction { (_: ActionEvent) => close(loader.getController[AdvancedEditor].functionEditor.getText) }
    })
    d.getButtons.add(new Button("Cancel").tap { c =>
      c.setCancelButton(true)
      c.setOnAction { (_: ActionEvent) => close("") }
    })
  }

  private def close(text: String): Unit =
    dialog.setResult(text)
    dialog.hide()

  def showDialog(): String = dialog.showAndWait().toScala.getOrElse("")

final class AdvancedEditor:
  import AdvancedEditor.{dialog, close}

  @FXML private var functionEditor: TextArea = _

  def initialize(): Unit =
    functionEditor.setOnKeyReleased { (key: KeyEvent) =>
      if key.getCode == KeyCode.ENTER then close(functionEditor.getText)
    }
