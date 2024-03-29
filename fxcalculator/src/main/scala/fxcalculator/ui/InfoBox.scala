package fxcalculator.ui

import com.gluonhq.charm.glisten.control.{Dialog, TextField}
import io.github.makingthematrix.signals3.ui.UiDispatchQueue.*
import javafx.event.ActionEvent
import javafx.scene.control.{Button, ScrollPane}

import scala.concurrent.Future
import scala.util.chaining.scalaUtilChainingOps

object InfoBox:
  def showText(text: String): Unit =
    val dialog = new Dialog[Unit]().tap { dialog =>
      dialog.setAutoHide(true)
      dialog.getButtons.add(new Button("Ok").tap { b =>
        b.setDefaultButton(true)
        b.setOnAction { (_: ActionEvent) => dialog.hide() }
      })
      dialog.setContentText(text)
    }
    Future { dialog.showAndWait() }(Ui)

