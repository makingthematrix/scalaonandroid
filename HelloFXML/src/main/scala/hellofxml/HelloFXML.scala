package hellofxml

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage

object HelloFXML {
  def main(args: Array[String]): Unit = javafx.application.Application.launch(classOf[HelloFXML], args: _*)
}

class HelloFXML extends Application {
  override def start(primaryStage: Stage): Unit = {
    val root = FXMLLoader.load[AnchorPane](classOf[HelloFXML].getResource("hello.fxml"))
    primaryStage.setScene(new Scene(root, 400, 600))
    primaryStage.show()
  }
}
