package hellofxml2

import com.wire.signals.Signal
import com.wire.signals.ui.UiDispatchQueue
import javafx.application.{Application, Platform}
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage

object HelloFXML {
  val signal = Signal(false)

  def main(args: Array[String]): Unit = Application.launch(classOf[HelloFXML], args: _*)
}

final class HelloFXML extends Application {

  override def start(primaryStage: Stage): Unit = {
    UiDispatchQueue.setUi(Platform.runLater)

    val root = FXMLLoader.load[AnchorPane](classOf[HelloFXML].getResource("hello.fxml"))
    primaryStage.setScene(new Scene(root, 400, 600))
    primaryStage.show()

    HelloFXML.signal ! true
  }
}
