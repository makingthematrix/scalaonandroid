package hellofxgl

import com.almasb.fxgl.app.GameApplication
import com.gluonhq.attach.display.DisplayService
import com.gluonhq.attach.util.Platform
import com.gluonhq.charm.glisten.application.MobileApplication
import com.gluonhq.charm.glisten.control.{AppBar, FloatingActionButton}
import com.gluonhq.charm.glisten.mvc.View
import com.gluonhq.charm.glisten.visual.{MaterialDesignIcon, Swatch}
import javafx.application.Application
import javafx.event.ActionEvent
import javafx.geometry.Dimension2D
import javafx.scene.Scene

import scala.util.chaining.scalaUtilChainingOps

object Main {
  def main(args: Array[String]): Unit = Application.launch(classOf[Main], args: _*)
}

final class Main extends MobileApplication {
  override def init(): Unit = addViewFactory(MobileApplication.HOME_VIEW, () =>
    new View(GameApplication.embeddedLaunch(new FXGLApp)) {
      override protected def updateAppBar(appBar: AppBar): Unit = appBar.setTitleText("Gluon Mobile and Scala 2.13")
    }.tap { view =>
      new FloatingActionButton(MaterialDesignIcon.SEARCH.text, (_: ActionEvent) => println("Hello from Scala")).tap { _.showOn(view) }
    }
  )

  override def postInit(scene: Scene): Unit = {
    Swatch.LIGHT_GREEN.assignTo(scene)
    scene.getStylesheets.add(this.getClass.getResource("styles.css").toExternalForm)
    if (Platform.isDesktop) {
      import scala.jdk.FunctionConverters._
      scene.getWindow.tap { window =>
        val defaultDimensions = (ds: DisplayService) => ds.getDefaultDimensions
        val dimension2D = DisplayService.create.map(defaultDimensions.asJava).orElse(new Dimension2D(640, 480))
        window.setWidth(dimension2D.getWidth)
        window.setHeight(dimension2D.getHeight)
      }
    }
  }
}