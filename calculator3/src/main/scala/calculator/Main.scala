package calculator

import com.gluonhq.attach.display.DisplayService
import com.gluonhq.attach.util.Platform
import com.gluonhq.charm.glisten.application.MobileApplication
import com.gluonhq.charm.glisten.mvc.View
import com.gluonhq.charm.glisten.visual.Swatch
import javafx.fxml.FXMLLoader
import javafx.geometry.Dimension2D
import javafx.scene.Scene
import javafx.scene.layout.BorderPane

import scala.jdk.FunctionConverters.enrichAsJavaFunction

object Main:
  def main(args: scala.Array[String]): Unit = javafx.application.Application.launch(classOf[Main], args: _*)

  val DEFAULT_WIDTH: Int = 550
  val DEFAULT_HEIGHT: Int = 650

final class Main extends MobileApplication:
  import Main._

  override def init(): Unit =
    val root = FXMLLoader.load[BorderPane](classOf[Main].getResource("main.fxml"))
    addViewFactory(MobileApplication.HOME_VIEW, () => new View(root))

  override def postInit(scene: Scene): Unit =
    Swatch.AMBER.assignTo(scene)
    scene.getStylesheets.add(classOf[Main].getResource("styles.css").toExternalForm)
    val dim =
      if (Platform.isDesktop) then
        new Dimension2D(DEFAULT_WIDTH, DEFAULT_HEIGHT)
      else
        DisplayService.create
          .map(((ds: DisplayService) => ds.getDefaultDimensions).asJava)
          .orElse(new Dimension2D(DEFAULT_WIDTH, DEFAULT_HEIGHT))
    scene.getWindow.setWidth(dim.getWidth)
    scene.getWindow.setHeight(dim.getHeight)
