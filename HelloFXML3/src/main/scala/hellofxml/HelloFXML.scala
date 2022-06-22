package hellofxml

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage

object HelloFXML:
  def main(args: Array[String]): Unit = Application.launch(classOf[HelloFXML], args: _*)

final class HelloFXML extends Application:
  val hello: HelloEnum = HelloEnum.HELLO
  
  override def start(primaryStage: Stage): Unit =
    val root = FXMLLoader.load[AnchorPane](classOf[HelloFXML].getResource("hello.fxml"))
    primaryStage.setScene(new Scene(root, 400, 600))
    primaryStage.show()
    
