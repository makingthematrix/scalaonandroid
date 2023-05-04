package fxcalculator

import com.gluonhq.charm.glisten.control.Dialog
import javafx.event.ActionEvent
import javafx.scene.control.Button

import scala.util.chaining.scalaUtilChainingOps
import scala.concurrent.Future

import io.github.makingthematrix.signals3.ui.UiDispatchQueue.*

object InfoBox:
  def show(text: String): Unit =
    val dialog = new Dialog[Unit]()
    dialog.setContentText(text)
    dialog.setAutoHide(true)
    dialog.getButtons.add(new Button("Ok").tap { b =>
      b.setDefaultButton(true)
      b.setOnAction { (_: ActionEvent) => dialog.hide() }
    })
    Future { dialog.showAndWait() }(Ui)
