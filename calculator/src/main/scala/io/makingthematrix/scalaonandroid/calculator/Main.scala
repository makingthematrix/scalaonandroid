package io.makingthematrix.scalaonandroid.calculator

import com.gluonhq.charm.glisten.application.MobileApplication
import com.gluonhq.charm.glisten.control.AppBar
import com.gluonhq.charm.glisten.mvc.View
import com.gluonhq.charm.glisten.visual.Swatch
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.BorderPane

// look into https://stackoverflow.com/questions/63527596/how-to-solve-fxml-loading-exceptions-in-compiled-javafx-project-using-gluonhq-cl

object Main {
  def main(args: Array[String]): Unit = javafx.application.Application.launch(classOf[Main], args: _*)
}

class Main extends MobileApplication {
  private lazy val calc = FXMLLoader.load[BorderPane](getClass.getResource("calculator.fxml"))

  override def init(): Unit = addViewFactory(MobileApplication.HOME_VIEW, () => {
    new View(calc) {
      override protected def updateAppBar(appBar: AppBar): Unit = {
        appBar.setTitleText("Scala Calculator")
      }
    }
  })

  override def postInit(scene: Scene): Unit = {
    Swatch.LIGHT_GREEN.assignTo(scene)
    scene.getStylesheets.add(this.getClass.getResource("styles.css").toExternalForm)
    scene.getWindow.setWidth(480)
    scene.getWindow.setHeight(640)
  }
}