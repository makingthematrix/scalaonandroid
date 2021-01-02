package io.makingthematrix.dummyapp

import com.gluonhq.attach.display.DisplayService
import com.gluonhq.attach.util.Platform
import com.gluonhq.charm.glisten.application.MobileApplication
import com.gluonhq.charm.glisten.control.{AppBar, FloatingActionButton}
import com.gluonhq.charm.glisten.mvc.View
import com.gluonhq.charm.glisten.visual.{MaterialDesignIcon, Swatch}
import javafx.event.ActionEvent
import javafx.geometry.{Dimension2D, Pos}
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.VBox

object Main {
  def main(args: Array[String]): Unit = {
    javafx.application.Application.launch(classOf[Main], args: _*)
  }
}

class Main extends MobileApplication {
  def logSomething(): Unit = {
    println("log something from Scala")
  }

  override def init(): Unit = {
    addViewFactory(MobileApplication.HOME_VIEW, () => {
      val fab = new FloatingActionButton(MaterialDesignIcon.SEARCH.text, (e: ActionEvent) => logSomething())
      val imageView = new ImageView(new Image(this.getClass.getResourceAsStream("openduke.png")))
      imageView.setFitHeight(200)
      imageView.setPreserveRatio(true)
      val label = new Label("Hello, Gluon Mobile!")
      val root = new VBox(20, imageView, label)
      root.setAlignment(Pos.CENTER)
      val view = new View(root) {
        override protected def updateAppBar(appBar: AppBar): Unit = {
          appBar.setTitleText("Gluon Mobile")
        }
      }
      fab.showOn(view)
      view
    })
  }

  override def postInit(scene: Scene): Unit = {
    Swatch.LIGHT_GREEN.assignTo(scene)
    scene.getStylesheets.add(this.getClass.getResource("styles.css").toExternalForm)
    if (Platform.isDesktop) {
      import scala.jdk.FunctionConverters._
      val f = (ds: DisplayService) => ds.getDefaultDimensions
      val dimension2D = DisplayService.create.map(f.asJava).orElse(new Dimension2D(640, 480))
      scene.getWindow.setWidth(dimension2D.getWidth)
      scene.getWindow.setHeight(dimension2D.getHeight)
    }
  }

}