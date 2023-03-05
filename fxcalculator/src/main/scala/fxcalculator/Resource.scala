package fxcalculator

import java.io.InputStream
import java.net.URL

enum Resource(val value: String):
  case StylesCss extends Resource("styles.css")
  case TrashPng extends Resource("trash.png")
  case MainFxml extends Resource("main.fxml")
  case AdvancedEditorFxml extends Resource("advancededitor.fxml")

object Resource {
  def url(res: Resource): URL = classOf[Resource].getResource(res.value)
  def stream(res: Resource): InputStream = classOf[Resource].getResourceAsStream(res.value)
}
