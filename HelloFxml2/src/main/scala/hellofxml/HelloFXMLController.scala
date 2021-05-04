package hellofxml

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}

final class HelloFXMLController {
  @FXML private var button: Button = _

  @FXML private var label: Label = _

  def initialize(): Unit =
    button.setOnAction((_: ActionEvent) => {
      HelloFXML.signal.onUi { signalValue =>
        label.setText(s"JavaFX hello ${System.getProperty("javafx.version")},\nsignal value: $signalValue")
      }
      label.setVisible(!label.isVisible)
    })
}
