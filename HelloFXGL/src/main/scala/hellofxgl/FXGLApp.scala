package hellofxgl

import com.almasb.fxgl.app.{GameApplication, GameSettings}
import com.almasb.fxgl.dsl.FXGL
import javafx.scene.paint.Color
import javafx.scene.text.{Font, Text}

import scala.util.chaining.scalaUtilChainingOps

final class FXGLApp extends GameApplication {
  override def initSettings(gameSettings: GameSettings): Unit = {}

  override protected def initUI(): Unit = {
    val uiText = new Text("Hello from FXGL").tap { text =>
      text.setFill(Color.WHITE)
      text.setFont(Font.font("Verdana", 20))
    }
    FXGL.addUINode(uiText, 100, 100)
  }
}
