package calculator

import com.gluonhq.attach.display.DisplayService
import com.gluonhq.attach.util.Platform
import com.gluonhq.charm.glisten.mvc.View
import com.gluonhq.charm.glisten.visual.Swatch
import javafx.fxml.FXMLLoader
import javafx.geometry.Dimension2D
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

import scala.jdk.FunctionConverters.enrichAsJavaFunction

object Main:
  def main(args: scala.Array[String]): Unit = javafx.application.Application.launch(classOf[Main], args: _*)

  val DEFAULT_WIDTH: Int = 550
  val DEFAULT_HEIGHT: Int = 650

final class Main extends javafx.application.Application:
  import Main._

  private def dimensions =
    if Platform.isDesktop then
      new Dimension2D(DEFAULT_WIDTH, DEFAULT_HEIGHT)
    else
      DisplayService.create
        .map(((ds: DisplayService) => ds.getDefaultDimensions).asJava)
        .orElse(new Dimension2D(DEFAULT_WIDTH, DEFAULT_HEIGHT))


  override def start(stage: Stage): Unit =
    val dim = dimensions
    val scene = new Scene(FXMLLoader.load[BorderPane](classOf[Main].getResource("main.fxml")), dim.getWidth, dim.getHeight)
    Swatch.AMBER.assignTo(scene)
    Option(classOf[Main].getResource("styles.css")).foreach { url =>
      scene.getStylesheets.add(url.toExternalForm)
    }
    stage.setScene(scene)
    stage.show()

