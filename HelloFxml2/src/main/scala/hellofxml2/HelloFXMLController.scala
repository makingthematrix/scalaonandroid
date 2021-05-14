package hellofxml2

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}

final class HelloFXMLController {

  @FXML private var button: Button = _

  @FXML private var label: Label = _

  def initialize(): Unit = {
    button.setOnAction((_: ActionEvent) => {
      HelloFXML.signal.onUi { signalValue =>
        val foo = Foo.randomFoo()
        val text =
          s"""JavaFX hello ${System.getProperty("javafx.version")},
             |signal value: $signalValue,
             |foo: $foo
             |""".stripMargin
        println(text)
        label.setText(text)
      }
      label.setVisible(!label.isVisible)
    })
  }
}
