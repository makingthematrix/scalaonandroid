package hellofxml2

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}

import com.wire.signals.ui.UiDispatchQueue._

final class HelloFXMLController {
  @FXML private var button: Button = _

  @FXML private var label: Label = _

  def initialize(): Unit =
    button.setOnAction((_: ActionEvent) => {
      HelloFXML.signal.onUi { signalValue =>
        val foo = randomFoo()
        val fooJson = foo.toJson
        val fooBack = fromJson(fooJson)
        val text =
          s"""JavaFX hello ${System.getProperty("javafx.version")},
             |signal value: $signalValue,
             |foo: $foo
             |foo as json string:
             |$fooJson
             |foo json string converted back:
             |$fooBack
             |""".stripMargin
        println(text)
        label.setText(text)
      }
      label.setVisible(!label.isVisible)
    })
}
