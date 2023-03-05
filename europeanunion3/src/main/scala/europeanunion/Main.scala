package europeanunion

import com.gluonhq.attach.display.DisplayService
import com.gluonhq.attach.util.Platform
import com.gluonhq.charm.glisten.application.AppManager
import com.gluonhq.charm.glisten.application.AppManager.HOME_VIEW
import com.gluonhq.charm.glisten.mvc.View
import com.gluonhq.charm.glisten.visual.Swatch
import javafx.application.Application
import javafx.stage.Stage
import javafx.geometry.Dimension2D
import javafx.scene.Scene
import javafx.scene.layout.BorderPane

import scala.jdk.FunctionConverters.*

object Main:
  def main(args: Array[String]): Unit = Application.launch(classOf[Main], args: _*)

final class Main extends Application:
  private val appManager = AppManager.initialize(postInit)
  
  override def init(): Unit =
    appManager.addViewFactory(HOME_VIEW, () => BasicView())

  override def start(stage: Stage): Unit = appManager.start(stage)

  private def postInit(scene: Scene): Unit =
    Swatch.BLUE.assignTo(scene)
    scene.getStylesheets.add(getClass.getResource("style.css").toExternalForm)
    if Platform.isDesktop then
      val dimension2D =
        DisplayService.create.map(((ds: DisplayService) => ds.getDefaultDimensions).asJava).orElse(new Dimension2D(640, 480))
      scene.getWindow.setWidth(dimension2D.getWidth)
      scene.getWindow.setHeight(dimension2D.getHeight)
