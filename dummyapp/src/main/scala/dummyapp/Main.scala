package dummyapp

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

import scala.util.chaining.scalaUtilChainingOps

object Main {
  def main(args: Array[String]): Unit = javafx.application.Application.launch(classOf[Main], args: _*)
}

final class Main extends MobileApplication {
  override def init(): Unit = addViewFactory(MobileApplication.HOME_VIEW, () => {
    val imageView = new ImageView(new Image(this.getClass.getResourceAsStream("openduke.png"))).tap { view =>
      view.setFitHeight(200)
      view.setPreserveRatio(true)
    }
    new View(new VBox(20, imageView, new Label("Hello, Gluon Mobile!")).tap { _.setAlignment(Pos.CENTER) }) {
      override protected def updateAppBar(appBar: AppBar): Unit = {
        appBar.setTitleText("Gluon Mobile and Scala 2.13")
      }
    }.tap { view =>
      new FloatingActionButton(MaterialDesignIcon.SEARCH.text, (_: ActionEvent) => println("log something from Scala")).tap { _.showOn(view) }
    }
  })

  override def postInit(scene: Scene): Unit = {
    Swatch.LIGHT_GREEN.assignTo(scene)
    scene.getStylesheets.add(this.getClass.getResource("styles.css").toExternalForm)
    if (Platform.isDesktop) {
      import scala.jdk.FunctionConverters._
      val defaultDimensions = (ds: DisplayService) => ds.getDefaultDimensions
      val dimension2D = DisplayService.create.map(defaultDimensions.asJava).orElse(new Dimension2D(640, 480))
      scene.getWindow.setWidth(dimension2D.getWidth)
      scene.getWindow.setHeight(dimension2D.getHeight)
    }
  }
}