package hellofxml

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}

class HelloFXMLController {
  @FXML private var button: Button = _

  @FXML private var label: Label = _

  def initialize():Unit =
    button.setOnAction((_: ActionEvent) => {
      label.setText("JavaFX hello " + System.getProperty("javafx.version"))
      label.setVisible(!label.isVisible)
    })
}
