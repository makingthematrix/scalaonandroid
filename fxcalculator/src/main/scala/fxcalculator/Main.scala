package fxcalculator

import com.gluonhq.attach.display.DisplayService
import com.gluonhq.attach.util.Platform
import com.gluonhq.charm.glisten.application.AppManager
import com.gluonhq.charm.glisten.mvc.View
import com.gluonhq.charm.glisten.visual.Swatch
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.geometry.Dimension2D
import javafx.scene.Scene
import javafx.scene.layout.BorderPane

import scala.jdk.FunctionConverters.enrichAsJavaFunction
import com.gluonhq.charm.glisten.application.AppManager.HOME_VIEW
import javafx.stage.Stage

import io.github.makingthematrix.signals3.ui.UiDispatchQueue

object Main:
  def main(args: scala.Array[String]): Unit = Application.launch(classOf[Main], args: _*)

  val DEFAULT_WIDTH: Int = 620
  val DEFAULT_HEIGHT: Int = 900

final class Main extends Application:
  import Main._

  private val appManager = AppManager.initialize(postInit)

  override def init(): Unit =
    UiDispatchQueue.setUi(javafx.application.Platform.runLater)
    val root = FXMLLoader.load[BorderPane](classOf[Main].getResource("main.fxml"))
    appManager.addViewFactory(HOME_VIEW, () => new View(root))

  override def start(stage: Stage): Unit = appManager.start(stage)

  private def postInit(scene: Scene): Unit =
    Swatch.AMBER.assignTo(scene)
    scene.getStylesheets.add(classOf[Main].getResource("styles.css").toExternalForm)
    val dim =
      if Platform.isDesktop then
        new Dimension2D(DEFAULT_WIDTH, DEFAULT_HEIGHT)
      else
        DisplayService.create
          .map(((ds: DisplayService) => ds.getDefaultDimensions).asJava)
          .orElse(new Dimension2D(DEFAULT_WIDTH, DEFAULT_HEIGHT))
    scene.getWindow.setWidth(dim.getWidth)
    scene.getWindow.setHeight(dim.getHeight)
