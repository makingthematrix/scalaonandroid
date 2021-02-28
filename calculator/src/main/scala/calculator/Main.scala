package calculator

import com.gluonhq.attach.display.DisplayService
import com.gluonhq.charm.glisten.application.MobileApplication
import com.gluonhq.charm.glisten.mvc.View
import com.gluonhq.charm.glisten.visual.Swatch
import javafx.fxml.FXMLLoader
import javafx.geometry.Dimension2D
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import com.gluonhq.attach.util.Platform

import scala.jdk.FunctionConverters.enrichAsJavaFunction

object Main {
  def main(args: Array[String]): Unit = javafx.application.Application.launch(classOf[Main], args: _*)
}

final class Main extends MobileApplication {
  override def init(): Unit = {
    lazy val root = FXMLLoader.load[BorderPane](classOf[Main].getResource("calculator.fxml"))
    addViewFactory(MobileApplication.HOME_VIEW, () => new View(root))
  }

  override def postInit(scene: Scene): Unit = {
    Swatch.AMBER.assignTo(scene)
    scene.getStylesheets.add(this.getClass.getResource("styles.css").toExternalForm)
    if (Platform.isDesktop) {
      val dimension2D =
        DisplayService.create.map(((ds: DisplayService) => ds.getDefaultDimensions).asJava).orElse(new Dimension2D(640, 720))
      scene.getWindow.setWidth(dimension2D.getWidth)
      scene.getWindow.setHeight(dimension2D.getHeight)
    }
  }
}