package fxcalculator.utils

import java.io.InputStream
import java.net.URL
import fxcalculator.Main

enum Resource(val value: String):
  case StylesCss          extends Resource("styles.css")
  case TrashPng           extends Resource("trash.png")
  case ClosePng           extends Resource("close.png")
  case MainFxml           extends Resource("main.fxml")
  case AdvancedEditorFxml extends Resource("advancededitor.fxml")

object Resource:
  def url(res: Resource): URL = classOf[Main].getResource(res.value)
  def stream(res: Resource): InputStream = classOf[Main].getResourceAsStream(res.value)
